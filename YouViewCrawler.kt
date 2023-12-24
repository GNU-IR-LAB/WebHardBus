package crawlers

import org.jsoup.Jsoup
import org.openqa.selenium.By
import utils.ChromeDriverHelper
import utils.ExcelHelper
import java.lang.Thread.sleep

class YouViewCrawler {

    private val webHardTitle = "YouView"

    private val driver = ChromeDriverHelper.getInstance()

    private val catMangaUrl = "https://www.youview.co.kr/login.do"

    // 크롤링된 제목들
    private val postList = mutableListOf<String>()

    fun start() {
        mangaCrawling()
    }

    private fun mangaCrawling() {
        driver.get(catMangaUrl)

        driver.findElement(By.cssSelector("#UserId")).sendKeys("aing3773")
        sleep(1000)
        driver.findElement(By.cssSelector("#UserPass")).sendKeys("gg3909demo!z")
        sleep(1000)
        driver.findElement(By.cssSelector("#user_login > input.btn.btn-primary.btn-block")).click()
        sleep(1000)

        try {
            for (i in 1 until  97) {
                val nextUrl = "https://www.youview.co.kr/list.do?mi=52&page=${i}&mt=6&skey=&keyword=&pi=pds&hi="
                println(i)
                driver.get(nextUrl)

                ChromeDriverHelper.executeScroll()

                // 페이지 소스 파싱
                val pageSource = driver.pageSource
                val doc = Jsoup.parse(pageSource)
                val cntList = doc.select("#page_content > form > table > tbody:nth-child(2)")
                val contents = cntList[0].select("tr")


                for (content in contents) {
                    try {
                        if (content.attr("class") == "pds_notice" ||
                            content.attr("class") == "pds_request") {
                            continue
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    val title = content.select("td > a").text()
                    postList.add(title)
                    println("Count: ${postList.size}, Title: $title")

                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val excelHelper = ExcelHelper()
        excelHelper.createExcel(webHardTitle + "_manga", webHardTitle, postList)

    }


}