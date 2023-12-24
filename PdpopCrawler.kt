package crawlers

import org.jsoup.Jsoup
import org.openqa.selenium.By
import utils.ChromeDriverHelper
import utils.ExcelHelper
import java.lang.Thread.sleep

class PdpopCrawler {
    private val webHardTitle = "Pdpop"

    private val catNovelUrl = "https://bbs.pdpop.com/board_re.php?code=F_13"
    private val novelMaxCount = 482
    private val catMangaUrl = "https://bbs.pdpop.com/board_re.php?code=F_21"
    private val mangaMaxCount = 598

    fun start() {
        crawling(catMangaUrl, "_manga")
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
                val html = driver.pageSource
                val doc = Jsoup.parse(html)

                val css = "#layerList > ul > li"

                val trElements = doc.select(css)

                for (i in 1 until trElements.size) {
                    val tr = trElements[i]
                    try {
                        val titleElement = tr.select("span.sbj > a").first()
                        titleElement?.also {
                            val title = it.text()
                            postList.add(title)
                            println("Count: ${postList.size}, Title: $title")
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                if (cat == "_manga") {
                    if (currentPage == mangaMaxCount) {
                        break
                    }
                } else {
                    if (currentPage == novelMaxCount) {
                        break
                    }
                }

                val buttonCss = "#layerPageNavigation > *"

                val nextPage = currentPage + 1

                try {
                    val nextButton = driver.findElements(By.cssSelector(buttonCss))

                    if(currentPage % 5 == 0) {
                        val btn = nextButton[nextButton.size - 2]
                        btn.click()
                        sleep(1000)

                    } else {
                        for (next in nextButton) {
                            if (next.tagName == "strong" && next.findElement(By.cssSelector("span")).text == (currentPage-1).toString()) {
                                flag = false
                                break
                            }

                            try {
                                if (next.getAttribute("title") == nextPage.toString()) {
                                    next.click()
                                    sleep(1000)
                                    break
                                }
                            } catch (_: Exception) {}
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    flag = false
                }

                currentPage = nextPage

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

        val excelHelper = ExcelHelper()
        excelHelper.createExcel(webHardTitle + cat, webHardTitle, postList)
    }


}