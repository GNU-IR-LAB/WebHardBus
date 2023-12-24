package crawlers

import org.openqa.selenium.By
import utils.ChromeDriverHelper
import utils.ExcelHelper
import java.lang.Thread.sleep

class FileKukiCrawler {
    private val webHardTitle = "FileKuki"

    private val rootUrl = "https://www.filekuki.com/"

    fun start() {
        val driver = ChromeDriverHelper.getInstance()
        driver.get(rootUrl)
        driver.switchTo().frame("main")
        // crawling("_manga")
        crawling("_novel")
    }

    private fun crawling(cat: String) {
        val postList = mutableListOf<String>()

        try {
            // 페이지 선택 인덱스
            var currentPage = 1

            val driver = ChromeDriverHelper.getInstance()
            val bookCat = driver.findElement(
                By.cssSelector(
                    "#TOP_menu_book"
                )
            )

            bookCat.click()
            sleep(1000L)


            if (cat == "_manga") {
                driver.findElement(By.cssSelector("#sMenu_box > ul > li:nth-child(5) > a")).click()
            } else {
                driver.findElement(By.cssSelector("#sMenu_box > ul > li:nth-child(4) > a")).click()
            }

            sleep(1000L)

            var flag = true

            while(flag) {

                var buttonListChildren = driver.findElements(By.cssSelector("#rank_movie > table > tbody > tr > td > table > tbody > tr > td > table > tbody > tr > *"))

                var immediatelyExitFlag = false
                for (button in buttonListChildren) {
                    try {
                        if (button.getAttribute("id") == "navi_click" && button.text == (currentPage - 1).toString()) {
                            immediatelyExitFlag = true
                            break
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                if (immediatelyExitFlag) break

                val trElements = driver.findElements(By.cssSelector("#rank_movie > form:nth-child(1) > table > tbody > tr"))

                for (i in trElements.indices step 2) {
                    val tr = trElements[i]
                    try {
                        val titleElement = tr.findElement(By.cssSelector("td:nth-child(2) > a > span"))
                        val title = titleElement.text
                        postList.add(title)
                        println("Count: ${postList.size}, Title: $title")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


                buttonListChildren = driver.findElements(By.cssSelector("#rank_movie > table > tbody > tr > td > table > tbody > tr > td > table > tbody > tr > *"))

                val nextPage = currentPage + 1

                for (button in buttonListChildren) {
                    try {
                        if (button.text == nextPage.toString()) {
                            button.click()
                            break
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        flag = false
                        break
                    }
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