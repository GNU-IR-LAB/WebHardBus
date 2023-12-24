package crawlers

import org.jsoup.Jsoup
import org.openqa.selenium.By
import utils.ChromeDriverHelper
import utils.ExcelHelper
import java.lang.Thread.sleep

class FileNoriCrawler {
    private val webHardTitle = "FileNori"

    private val rootUrl = "https://www.filenori.com/"

    fun start() {
        crawling(rootUrl, "_book")
    }

    private fun crawling(url: String, cat: String) {
        val postList = mutableListOf<String>()

        try {
            // 페이지 선택 인덱스
            var currentPage = 1

            val driver = ChromeDriverHelper.getInstance()
            driver.get(url)
            driver.switchTo().frame("main")
            sleep(1000L)
            driver.findElement(By.cssSelector("#cate_08")).click()
            sleep(1000L)

            var flag = true

            while(flag) {
                val html = driver.pageSource
                val doc = Jsoup.parse(html)

                val css = "#contents > table > tbody > tr"

                val trElements = doc.select(css)

                for (i in 1 until trElements.size) {
                    val tr = trElements[i]
                    try {
                        val titleElement = tr.select("div.fl.ellipsis > span").first()
                        titleElement?.also {
                            val title = it.text()
                            postList.add(title)
                            println("Count: ${postList.size}, Title: $title")
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                val buttonCss = "#pageInfo > div > a"

                try {
                    val nextButton = driver.findElements(By.cssSelector(buttonCss))
                    val btn = nextButton[nextButton.size - 3]
                    if (btn.getAttribute("onclick").isNullOrBlank()) {
                        flag = false
                    } else {
                        btn.click()
                        //sleep(1000)
                    }
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