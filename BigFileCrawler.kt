package crawlers

import org.jsoup.Jsoup
import org.openqa.selenium.By
import utils.ChromeDriverHelper
import utils.ExcelHelper
import java.lang.Thread.sleep

class BigFileCrawler {
    private val webHardTitle = "BigFile"

    private val catMangaUrl = "https://www.bigfile.co.kr/content/main.php?cateGory=0009,0001"
    private val catMangaFinUrl = "https://www.bigfile.co.kr/content/main.php?cateGory=0009,0002"
    private val catMangaBLUrl = "https://www.bigfile.co.kr/content/main.php?cateGory=0009,0003"
    private val catMangaAdult = "https://www.bigfile.co.kr/content/main.php?cateGory=0009,0009"
    private val catNovelUrl = "https://www.bigfile.co.kr/content/main.php?cateGory=0014,0002"

    fun start() {
        // crawling(catMangaUrl, "_manga")
        // crawling(catMangaFinUrl, "_manga_fin")
        // crawling(catMangaBLUrl, "_manga_bl")
        // crawling(catMangaAdult, "_manga_adult")
        crawling(catNovelUrl, "_novel")
    }

    private fun crawling(url: String, cat: String) {
        val postList = mutableListOf<String>()

        try {
            // 페이지 선택 인덱스
            var currentPage = 1

            val driver = ChromeDriverHelper.getInstance()
            driver.get(url)

            var flag = true

            while(flag) {
                ChromeDriverHelper.executeScroll()
                val html = driver.pageSource
                val doc = Jsoup.parse(html)

                val css = "#printTableView > table:nth-child(3) > tbody > tr"

                val trElements = doc.select(css)

                for (i in 1 until trElements.size) {
                    val tr = trElements[i]
                    try {
                        val titleElement = tr.select("td:nth-child(2) > a").first()
                        titleElement?.also {
                            val title = it.text()
                            postList.add(title)
                            println("Count: ${postList.size}, Title: $title")
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                val next = currentPage + 1

                val buttonCSS = "#printTableView > table:nth-child(4) > tbody > tr:nth-child(2) > td > div > div > div > span.page_num > *"
                val pageButtons = driver.findElements(By.cssSelector(buttonCSS))

                if (currentPage % 10 == 0) {
                    val nextButton = driver.findElement(By.cssSelector("#printTableView > table:nth-child(4) > tbody > tr:nth-child(2) > td > div > div > div > span:nth-child(3) > a"))
                    if (nextButton.getAttribute("href").isNullOrBlank()) {
                        flag = false
                    }

                    nextButton.click()
                    sleep(1)

                } else {
                    try {
                        for (button in pageButtons) {
                            if (button.text.replace(" ", "") == next.toString()) {
                                button.click()
                                sleep(1)
                            }
                        }
                    } catch (_: Exception) {}
                }

                currentPage = next

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

        val excelHelper = ExcelHelper()
        excelHelper.createExcel(webHardTitle + cat, webHardTitle, postList)
    }


}