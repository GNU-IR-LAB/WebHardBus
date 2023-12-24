package crawlers

import org.jsoup.Jsoup
import org.openqa.selenium.By
import utils.ChromeDriverHelper
import utils.ExcelHelper

class GdiskCrawler {
    private val webHardTitle = "G-Disk"

    private val catMangaUrl = "https://g-disk.co.kr/contents/?category1=IMG&category2=IMG_005&s_column=&s_word=&ccode=&show_type=0&rows=50&sort=&show_type=0&page="
    private val catMangaAdultUrl = "https://g-disk.co.kr/contents/?category1=IMG&category2=IMG_003&s_column=&s_word=&ccode=&show_type=0&rows=50&sort=&show_type=0&page="
    private val catMangaAdultBLUrl = "https://g-disk.co.kr/contents/?category1=IMG&category2=IMG_002&s_column=&s_word=&ccode=&show_type=0&rows=50&sort=&show_type=0&page="
    private val catNovelUrl = "https://g-disk.co.kr/contents/?category1=ETC&category2=&s_column=&s_word=&ccode=&show_type=0&rows=50&sort=&show_type=0&page="

    fun start() {
        login()
        crawling(catMangaUrl, "_manga")
        crawling(catMangaAdultUrl, "_manga_adult")
        crawling(catMangaAdultBLUrl, "_manga_adult_bl")
        crawling(catNovelUrl, "_novel")
    }

    private fun login() {
        val driver = ChromeDriverHelper.getInstance()
        driver.get("https://g-disk.co.kr/")

        ChromeDriverHelper.handleAlert()

        driver.findElement(By.cssSelector("#m_id")).sendKeys("aing3773")
        driver.findElement(By.cssSelector("#m_pwd")).sendKeys("gg3773demo!z")
        driver.findElement(By.cssSelector("#mainLoginForm > table > tbody > tr:nth-child(2) > td:nth-child(2) > input[type=image]")).click()


        ChromeDriverHelper.handleAlert()
    }

    private fun crawling(url: String, cat: String) {
        val postList = mutableListOf<String>()

        try {
            // 페이지 선택 인덱스
            var currentPage = 0
            val driver = ChromeDriverHelper.getInstance()
            var flag = true

            while(flag) {

                driver.get(url + currentPage)
                val html = driver.pageSource
                val doc = Jsoup.parse(html)

                val cntList = doc.select("body > table > tbody > tr > td > table:nth-child(4) > tbody > tr > td:nth-child(3) > table.boardtype2 > tbody")
                val trElements = cntList[0].select("tr")

                for (tr in trElements) {
                    val titleElement = tr.select("td.title").first()
                    titleElement?.let {
                        val title = it.text()
                        postList.add(title)
                        println("Count: ${postList.size}, Title: $title")
                    }
                }

                currentPage += 1

                if (html.contains("count(): Parameter must be an array or an object that implements Countable")) {
                    println("경고 메시지가 감지되었습니다. 프로그램을 종료합니다.")
                    flag = false
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

        val excelHelper = ExcelHelper()
        excelHelper.createExcel(webHardTitle + cat, webHardTitle, postList)
    }


}