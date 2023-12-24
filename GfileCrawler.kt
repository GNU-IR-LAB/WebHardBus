package crawlers

import utils.ChromeDriverHelper
import org.jsoup.Jsoup
import org.openqa.selenium.By
import utils.ExcelHelper

class GfileCrawler {
    // 기본 설정
    private val webHardTitle = "Sharebox"


    // 크롤링 할 url
    private val catMangaUrl = "https://sharebox.co.kr/storage/storage.php?section=DOC_001"
    private val catNovelUrl = "https://sharebox.co.kr/storage/storage.php?section=DOC_005"
    private val catMangaAdultUrl = "https://sharebox.co.kr/storage/storage.php?section=DOC_003"
    private val catNovelAdultUrl = "https://sharebox.co.kr/storage/storage.php?section=DOC_006"
    private val catNovelEtcUrl = "https://sharebox.co.kr/storage/storage.php?section=DOC_004"

    private val driver = ChromeDriverHelper.getInstance()

    // 크롤링된 제목들
    private val postList = mutableListOf<String>()



    fun start() {
        mangaCrawling()

        novelCrawling()

        mangaAdultCrawling()

        novelAdultCrawling()

        novelEtcCrawling()
    }

    private fun mangaCrawling() {
        try {
            // 페이지 선택 인덱스
            var currentPage = 0

            // 크롬 드라이버 접속
            driver.get(catMangaUrl)

            for (i in 0 until 1000000) {
                currentPage += 1
                ChromeDriverHelper.executeScroll()

                // 페이지 소스 파싱
                val pageSource = driver.pageSource
                val doc = Jsoup.parse(pageSource)
                val cntList = doc.select("#contents_list")
                val contents = cntList[0].select(".bbs_list")

                for (content in contents) {
                    val titleElement = content.select("td.alignL.clickable > span")
                    val title = titleElement.attr("title")

                    postList.add(title)
                    println("Count: ${postList.size}, Title: $title")
                }


                if (currentPage % 10 == 0) {
                    val nextButton = driver.findElement(By.cssSelector("#contents_list > div > img.mar_left5"))
                    nextButton.click()
                }

                val pageButtons = driver.findElements(By.cssSelector("#contents_list > div > span"))

                for (buttonSpan in pageButtons) {
                    if (buttonSpan.text.toInt() == currentPage + 1) {
                        buttonSpan.click()
                        break
                    }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val excelHelper = ExcelHelper()
        excelHelper.createExcel(webHardTitle + "_manga", webHardTitle, postList)

    }

    private fun novelCrawling() {
        try {
            // 페이지 선택 인덱스
            var currentPage = 0

            // 크롬 드라이버 접속
            driver.get(catNovelUrl)

            for (i in 0 until 1000000) {
                currentPage += 1
                ChromeDriverHelper.executeScroll()

                // 페이지 소스 파싱
                val pageSource = driver.pageSource
                val doc = Jsoup.parse(pageSource)
                val cntList = doc.select("#contents_list")
                val contents = cntList[0].select(".bbs_list")

                for (content in contents) {
                    val titleElement = content.select("td.alignL.clickable > span")
                    val title = titleElement.attr("title")

                    postList.add(title)
                    println("Count: ${postList.size}, Title: $title")
                }


                if (currentPage % 10 == 0) {
                    val nextButton = driver.findElement(By.cssSelector("#contents_list > div > img.mar_left5"))
                    nextButton.click()
                }

                val pageButtons = driver.findElements(By.cssSelector("#contents_list > div > span"))

                for (buttonSpan in pageButtons) {
                    if (buttonSpan.text.toInt() == currentPage + 1) {
                        buttonSpan.click()
                        break
                    }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val excelHelper = ExcelHelper()
        excelHelper.createExcel(webHardTitle + "_novel", webHardTitle, postList)

    }

    private fun mangaAdultCrawling() {
        try {
            // 페이지 선택 인덱스
            var currentPage = 0

            // 크롬 드라이버 접속
            driver.get(catMangaAdultUrl)

            for (i in 0 until 1000000) {
                currentPage += 1
                ChromeDriverHelper.executeScroll()

                // 페이지 소스 파싱
                val pageSource = driver.pageSource
                val doc = Jsoup.parse(pageSource)
                val cntList = doc.select("#contents_list")
                val contents = cntList[0].select(".bbs_list")

                for (content in contents) {
                    val titleElement = content.select("td.alignL.clickable > span")
                    val title = titleElement.attr("title")

                    postList.add(title)
                    println("Count: ${postList.size}, Title: $title")
                }


                if (currentPage % 10 == 0) {
                    val nextButton = driver.findElement(By.cssSelector("#contents_list > div > img.mar_left5"))
                    nextButton.click()
                }

                val pageButtons = driver.findElements(By.cssSelector("#contents_list > div > span"))

                for (buttonSpan in pageButtons) {
                    if (buttonSpan.text.toInt() == currentPage + 1) {
                        buttonSpan.click()
                        break
                    }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val excelHelper = ExcelHelper()
        excelHelper.createExcel(webHardTitle + "_manga_adult", webHardTitle, postList)

    }


    private fun novelAdultCrawling() {
        try {
            // 페이지 선택 인덱스
            var currentPage = 0

            // 크롬 드라이버 접속
            driver.get(catNovelAdultUrl)

            for (i in 0 until 1000000) {
                currentPage += 1
                ChromeDriverHelper.executeScroll()

                // 페이지 소스 파싱
                val pageSource = driver.pageSource
                val doc = Jsoup.parse(pageSource)
                val cntList = doc.select("#contents_list")
                val contents = cntList[0].select(".bbs_list")

                for (content in contents) {
                    val titleElement = content.select("td.alignL.clickable > span")
                    val title = titleElement.attr("title")

                    postList.add(title)
                    println("Count: ${postList.size}, Title: $title")
                }


                if (currentPage % 10 == 0) {
                    val nextButton = driver.findElement(By.cssSelector("#contents_list > div > img.mar_left5"))
                    nextButton.click()
                }

                val pageButtons = driver.findElements(By.cssSelector("#contents_list > div > span"))

                for (buttonSpan in pageButtons) {
                    if (buttonSpan.text.toInt() == currentPage + 1) {
                        buttonSpan.click()
                        break
                    }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val excelHelper = ExcelHelper()
        excelHelper.createExcel(webHardTitle + "_novel_adult", webHardTitle, postList)

    }


    private fun novelEtcCrawling() {
        try {
            // 페이지 선택 인덱스
            var currentPage = 0

            // 크롬 드라이버 접속
            driver.get(catNovelEtcUrl)

            for (i in 0 until 1000000) {
                currentPage += 1
                ChromeDriverHelper.executeScroll()

                // 페이지 소스 파싱
                val pageSource = driver.pageSource
                val doc = Jsoup.parse(pageSource)
                val cntList = doc.select("#contents_list")
                val contents = cntList[0].select(".bbs_list")

                for (content in contents) {
                    val titleElement = content.select("td.alignL.clickable > span")
                    val title = titleElement.attr("title")

                    postList.add(title)
                    println("Count: ${postList.size}, Title: $title")
                }


                if (currentPage % 10 == 0) {
                    val nextButton = driver.findElement(By.cssSelector("#contents_list > div > img.mar_left5"))
                    nextButton.click()
                }

                val pageButtons = driver.findElements(By.cssSelector("#contents_list > div > span"))

                for (buttonSpan in pageButtons) {
                    if (buttonSpan.text.toInt() == currentPage + 1) {
                        buttonSpan.click()
                        break
                    }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val excelHelper = ExcelHelper()
        excelHelper.createExcel(webHardTitle + "_novel_etc", webHardTitle, postList)

    }

}