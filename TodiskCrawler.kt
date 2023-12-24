package crawlers

import org.jsoup.Jsoup
import org.openqa.selenium.By
import utils.ChromeDriverHelper
import utils.ExcelHelper

class TodiskCrawler {
    private val webHardTitle = "Todisk"

    private val catMangaUrl = "https://www.todisk.com/_main/storage.php?section=COM&search_keyword=&search=&search_type=&sub_sec=&liststate=&p=1&search_sort=&search_re2=&search_radio2=&list_count=100"
    private val catNovelUrl = "https://www.todisk.com/_main/storage.php?section=DOC&sub_sec=CTN_002&liststate=&list_count=100"

    fun start() {
        crawling(catMangaUrl, "_manga")
        crawling(catNovelUrl, "_novel")
    }

    private fun crawling(url: String, cat: String) {
        val postList = mutableListOf<String>()

        var addCount = 0

        try {
            // 페이지 선택 인덱스
            var currentPage = 0
            val driver = ChromeDriverHelper.getInstance()
            var flag = true

            driver.get(url)

            ChromeDriverHelper.executeScroll()

            while(flag) {

                val html = driver.pageSource
                val doc = Jsoup.parse(html)

                val cntList = doc.select("#list_sort > table.list_category_list > tbody")
                val trElements = cntList[0].select("tr")

                try {
                    for (tr in trElements) {
                        tr.select("span").first()?.text()?.let {
                            postList.add(it)
                            println("Count: ${postList.size}, Title: $it")
                        }
                    }
                } catch (_: Exception) {}

                currentPage += 1

                if (currentPage == 299) {
                    flag = false
                }

                val buttonList = driver.findElements(By.cssSelector("#list_sort > table:nth-child(2) > tbody > tr > td > table > tbody > tr > td"))

                if (buttonList.size == 13 || buttonList.size == 14) {
                    val button = buttonList[buttonList.size - 2]
                    button.click()
                } else {
                    if (addCount == buttonList.size - 3) {
                        flag = false
                    }
                    buttonList[buttonList.size-1].click()
                    addCount += 1

                }

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

        val excelHelper = ExcelHelper()
        excelHelper.createExcel(webHardTitle + cat, webHardTitle, postList)
    }


}