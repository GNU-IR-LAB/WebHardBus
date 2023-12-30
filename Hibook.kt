package utils
import org.jsoup.Jsoup
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoAlertPresentException
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.FileOutputStream
import java.lang.Thread.sleep

class Hibook {
    object ChromeDriverCreator {
        fun createDriver(): ChromeDriver {
            // 크롬 드라이버 경로 설정
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\HAEDAL\\Desktop\\HAEDAL\\연구실 관련\\저작권\\vscode\\file_man\\untitled\\src\\main\\kotlin\\chromedriver-win64\\chromedriver.exe")

            // 디버거 크롬 옵션 추가
            val chromeOptions = ChromeOptions()

            // 크롬 실행 파일 경로 설정
            chromeOptions.setBinary("C:\\Users\\HAEDAL\\Desktop\\chrome-win32\\chrome.exe")

            chromeOptions.addArguments("--remote-allow-origins=*");

            // 크롬 브라우저 꺼짐 방지
            chromeOptions.setExperimentalOption("detach", true)

            // 불필요한 에러 메시지 없애기
            chromeOptions.setExperimentalOption("excludeSwitches", listOf("enable-logging"))

            // ChromeDriver 생성자에 chromeOptions 객체 전달
            val driver = ChromeDriver(chromeOptions)

            // 창을 최대화
            driver.manage().window().maximize()

            // 암시적 대기 시간을 설정
            driver.manage().timeouts().implicitlyWait(10L, java.util.concurrent.TimeUnit.SECONDS)

            return driver
        }
    }

    fun createWorkbook(data: List<String>) {
        val workbook = XSSFWorkbook()

            // 새 시트 생성
            val sheet = workbook.createSheet("hi_toon manga")

            val list = mutableListOf(
                listOf("number", "title"),
            )

            for (i in data.indices) {
                list.add(listOf((i+1).toString(), data[i]))
            }

            for ((rowNum, dataRow) in list.withIndex()) {
                val row = sheet.createRow(rowNum)
                for ((cellNum, value) in dataRow.withIndex()) {
                    val cell = row.createCell(cellNum)
                    cell.setCellValue(value)
                }
            }

            try {
                val outputStream = FileOutputStream("Hi_Toon_titles.xlsx")
                workbook.write(outputStream)
                workbook.close()
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }

    fun crawling(): MutableList<String> {
        val data = mutableListOf<String>()
        val driver = ChromeDriverCreator.createDriver()
        val jse = driver as JavascriptExecutor

        try {
            driver.get("https://fileis.com/index.php")
            val id = driver.findElement(By.xpath("//*[@id=\"m_id\"]"))
            val pw = driver.findElement(By.xpath("//*[@id=\"m_pwd\"]"))
            id.sendKeys("jang4282002")
            pw.sendKeys("0130lee@")

            val login = driver.findElement(By.xpath("//*[@id=\"mainLoginForm\"]/div/div[2]/input[2]"))
            login.click()

            sleep(3000)

            driver.switchTo().alert().accept()

            val novelTab = driver.findElement(By.xpath("//*[@id=\"header_wrap\"]/div[3]/div/div/ul[1]/li[15]"))
            novelTab.click()

            driver.switchTo().frame("HIBOOK")

            val ranking = driver.findElement(By.xpath("//*[@id=\"bodyCover\"]/div[7]/div/div[2]/ul/li[4]"))
            ranking.click()

            val next = driver.findElement(By.xpath("//*[@id=\"bodyCover\"]/div[8]/div/div[3]/div"))
            for ( i in 1..50) {
                next.click()
                sleep(3000)
            }

            val list = driver.findElements(By.ByClassName("bookTitle"))
            for (i in list) {
                data.add(i.text)
                println(i.text)
            }

        } catch (e: Exception) {
            println("connecting to error = fileis")
            println(e.message)
            println("===================================================================================================")
            e.printStackTrace()
        }
        return data
    }
}

fun main() {
    val hibook = Hibook()

    val data = hibook.crawling()
    hibook.createWorkbook(data)
}