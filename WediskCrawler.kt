package crawlers

import org.jsoup.Jsoup
import org.openqa.selenium.By
import utils.ChromeDriverHelper
import utils.ExcelHelper
import java.lang.Thread.sleep

class WediskCrawler {
    private val webHardTitle = "WeDisk"

    private val rootUrl = "https://www.wedisk.co.kr/"

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

            driver.findElement(By.cssSelector("#main_08")).click()
            sleep(1000)


            var flag = true

            while(flag) {
                val html = driver.pageSource
                val doc = Jsoup.parse(html)

                val css = "#data_list > tr"

                val trElements = doc.select(css)

                for (i in 1 until trElements.size) {
                    val tr = trElements[i]
                    try {
                        val titleElement = tr.select("div > div.data_title.adult_check > a").first()
                        titleElement?.also {
                            val title = it.text()
                            postList.add(title)
                            println("Count: ${postList.size}, Title: $title")
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                if (currentPage == 1735) {
                    break
                }

                val buttonCss = "#contents > div.data_list_box > div > div > ul > li"

                try {
                    val nextButton = driver.findElements(By.cssSelector(buttonCss))
                    val btn = nextButton[nextButton.size - 3]
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