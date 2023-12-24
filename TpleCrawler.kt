package crawlers

import org.jsoup.Jsoup
import org.openqa.selenium.By
import utils.ChromeDriverHelper
import utils.ExcelHelper

class TpleCrawler {
    private val webHardTitle = "Tple"

    private val catMangaUrl = "https://www.tple.co.kr/_renew/storage.php?code=13"
    private val catNovelUrl = "https://www.tple.co.kr/_renew/storage.php?code=8_2"

    private val driver = ChromeDriverHelper.getInstance()

    private val postList = mutableListOf<String>()

    fun start() {
        crawling(catMangaUrl, "_manga")
        crawling(catNovelUrl, "_novel")
    }

    private fun crawling(url: String, cat: String) {
        try {
            // 페이지 선택 인덱스
            var currentPage = 0

            driver.get(url)

            var flag = true

            while(flag) {
                currentPage += 1
                ChromeDriverHelper.executeScroll()

                // 페이지 소스 파싱
                val pageSource = driver.pageSource
                val doc = Jsoup.parse(pageSource)
                val cntList = doc.select("#divFileList > table > tbody")
                val contents = cntList[0].select("tr")

                for (content in contents) {
                    val titleElement = content.select("td > div > a")
                    val title = titleElement.text()

                    postList.add(title)
                    println("Count: ${postList.size}, Title: $title")
                }

                if (currentPage % 10 == 0) {
                    val nextButton = driver.findElement(By.cssSelector("#divFileList > div.paginate > span > a.next"))
                    nextButton.click()
                }

                val pageButtons = driver.findElements(By.cssSelector("#divFileList > div.paginate > span > a"))

                for (buttonSpan in pageButtons) {
                    try {
                        val classAttribute = buttonSpan.getAttribute("class")

                        // 'next'와 'disabled' 클래스를 모두 가지고 있는지 확인
                        if (classAttribute.contains("next") && classAttribute.contains("disabled")) {
                            flag = false
                            break
                        }

                        if (buttonSpan.text.toInt() == currentPage + 1) {
                            buttonSpan.click()
                            break
                        }

                    } catch (_: Exception) { }
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

        val excelHelper = ExcelHelper()
        excelHelper.createExcel(webHardTitle + cat, webHardTitle, postList)
    }


}