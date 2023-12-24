package crawlers

import org.jsoup.Jsoup
import org.openqa.selenium.By
import utils.ChromeDriverHelper
import utils.ExcelHelper
import utils.PageController
import java.lang.Thread.sleep

class SmartFileCrawler {
    private val webHardTitle = "SmartFile"

    private val rootUrl = "https://smartfile.co.kr/"
    private val catMangaUrl = "https://smartfile.co.kr/contents/?category1=CAT"
    private val catNovelUrl = "https://smartfile.co.kr/contents/?category1=DOC&category2=DOC_001&limit=0"

    fun start() {
        val driver = ChromeDriverHelper.getInstance()
        driver.get(rootUrl)
        sleep(1000)

        driver.findElement(By.cssSelector("#wrap > div.wrap-nav-wrap > div > ul.depth1 > li.menutop_CAT.m8")).click()

        crawling(catMangaUrl, "_manga")

        driver.findElement(By.cssSelector("#wrap > div.wrap-nav-wrap > div > ul.depth1 > li.menutop_DOC.m9")).click()

        crawling(catNovelUrl, "_novel")
    }

    private fun crawling(url: String, cat: String) {
        val postList = mutableListOf<String>()

        try {
            // 페이지 선택 인덱스
            var currentPage = 1
            val driver = ChromeDriverHelper.getInstance()
            var flag = true

            while(flag) {

                driver.get(url + currentPage)
                val html = driver.pageSource
                val doc = Jsoup.parse(html)

                val cntList = doc.select("#search_list")
                val trElements = cntList[0].select("tr")

                for (tr in trElements) {
                    val titleElement = tr.select("font").first()
                    titleElement?.let {
                        val title = it.text()
                        postList.add(title)
                        println("Count: ${postList.size}, Title: $title")
                    }
                }

                val pageController = PageController().apply {
                    pageButtonListCSS = "#pageing_area > div"
                    currentPageButtonCSS = "strong"
                    uod = 15
                    nextText = "다음"
                }

                try {
                    pageController.next(currentPage)
                } catch (_: Exception) {
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