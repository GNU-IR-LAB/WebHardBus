package crawlers

import org.jsoup.Jsoup
import utils.ChromeDriverHelper
import utils.ExcelHelper

class AppleFileCrawler {
    private val webHardTitle = "AppleFile"

    private val catMangaUrl = "https://www.applefile.tv/contents/#tab=CAT&limit=100&pn="
    private val mangaMaxPage = 3
    private val catNovelUrl = "https://www.applefile.tv/contents/#tab=DOC&limit=100&pn="
    private val novelMaxPage = 2

    fun start() {
        crawling(catMangaUrl, "_manga", mangaMaxPage)
        crawling(catNovelUrl, "_novel", novelMaxPage)
    }

    private fun crawling(url: String, cat: String, maxPage: Int) {
        val postList = mutableListOf<String>()

        try {
            // 페이지 선택 인덱스
            var currentPage = 0
            val driver = ChromeDriverHelper.getInstance()
            var flag = true

            while(flag) {

                driver.get(url + currentPage)

                val doc = Jsoup.parse(driver.pageSource)

                val cntList = doc.select("#table > tbody")
                val contents = cntList[0].select("tr")

                for (i in 1 until contents.size) {
                    val tr = contents[i]

                    val titleElement = tr.select("td.title").first()
                    titleElement?.let { it ->
                        val title = if (it.select("b").isNotEmpty()) {
                            // <b> 태그가 있는 경우
                            it.select("b").first()?.text()
                        } else {
                            // <b> 태그가 없는 경우
                            it.text()

                        }

                        title?.let { inner ->
                            postList.add(inner)
                            println("Count: ${postList.size}, Title: $inner")
                        }
                    }
                }

                currentPage += 1
                if (currentPage == maxPage) flag = false
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

        val excelHelper = ExcelHelper()
        excelHelper.createExcel(webHardTitle + cat, webHardTitle, postList)
    }


}