package crawlers

import org.jsoup.Jsoup
import org.openqa.selenium.By
import utils.ChromeDriverHelper
import utils.ExcelHelper
import java.lang.Thread.sleep

class FileJoCrawler {
    private val webHardTitle = "FileJo"

    private val rootUrl = "https://www.filejo.com/"

    fun start() {
        val driver = ChromeDriverHelper.getInstance()
        driver.get(rootUrl)
        driver.findElement(By.cssSelector("body > div.tophide > div > div.top_evbtn > a")).click()
        sleep(1000L)

        driver.switchTo().frame("mainFrame")

        crawling("_manga")
        crawling("_novel")
    }

    private fun crawling(cat: String) {
        val postList = mutableListOf<String>()

        try {
            // 페이지 선택 인덱스
            var currentPage = 1

            val driver = ChromeDriverHelper.getInstance()

            if (cat == "_manga") {
                driver.findElement(By.cssSelector("#aa > table > tbody > tr:nth-child(3) > td > table:nth-child(2) > tbody > tr:nth-child(2) > td > table > tbody > tr > td > table > tbody > tr > td > div > div > ul > li:nth-child(7)")).click()
            } else {
                driver.findElement(By.cssSelector("#aa > table > tbody > tr:nth-child(3) > td > table:nth-child(2) > tbody > tr:nth-child(2) > td > table > tbody > tr > td > table > tbody > tr > td > div > div > ul > li:nth-child(9)")).click()
            }

            sleep(1000L)

            var flag = true

            while(flag) {
                val html = driver.pageSource
                val doc = Jsoup.parse(html)

                val css = if (currentPage > 1) {
                    "#list_sort > table > tbody > tr:nth-child(5) > td > table > tbody > tr > td > table:nth-child(2) > tbody > tr > td > table > tbody > tr"
                } else {
                    "#list_sort > table > tbody > tr:nth-child(12) > td > table > tbody > tr > td > table:nth-child(2) > tbody > tr > td > table > tbody > tr"

                }

                val trElements = doc.select(css)

                for (i in trElements.indices step 2) {
                    val tr = trElements[i]
                    try {
                        val titleElement = tr.select("table > tbody > tr > td:nth-child(3) > a > span").first()
                        titleElement?.also {
                            val title = it.text()
                            postList.add(title)
                            println("Count: ${postList.size}, Title: $title")
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                val buttonCss = if (currentPage > 1) {
                    "#list_sort > table > tbody > tr:nth-child(6) > td > table > tbody > tr"
                } else {
                    "#list_sort > table > tbody > tr:nth-child(13) > td > table > tbody > tr"
                }

                try {
                    val nextButton = driver.findElements(By.cssSelector("$buttonCss > td"))
                    val btn = nextButton[nextButton.size - 2]
                    btn.click()
                    sleep(1000)
                } catch (e: Exception) {
                    e.printStackTrace()
                    flag = false
                }

                currentPage += 1

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

        val excelHelper = ExcelHelper()
        excelHelper.createExcel(webHardTitle + cat, webHardTitle, postList)
    }


}