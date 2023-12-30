package utils
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoAlertPresentException
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.FileOutputStream

class Me2Disk {
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
        val sheet = workbook.createSheet("me_2_disk manga")

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
            val outputStream = FileOutputStream("Me_2_Disk_titles.xlsx")
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

        try {
            driver.get("https://me2disk.com/contents/index.htm?category1=CTN&rows=100#1")

            Thread.sleep(3000)

            val jse = driver as JavascriptExecutor

            var nextButton = driver.findElement(By.id("next_btn_img"))

            Thread.sleep(3000)

            for (i in 0..252) {
                try {
                jse.executeScript("window.scrollTo(0, document.body.scrollHeight / 2)")
                var list = driver.findElement(By.id("contentsListWrap")) // CSS 쿼리를 사용하여 요소 선택
                var titles = list.findElements(By.cssSelector(".ellipsis > a"))
                for (element in titles!!) {
                    data.add(element.getAttribute("title"))
                    println(element.getAttribute("title"))
                }
                nextButton.click()
                if (data[data.size - 101] == titles[0].getAttribute("title")) {
                    jse.executeScript("window.scrollTo(0, document.body.scrollHeight / 2)")
                    list = driver.findElement(By.id("contentsListWrap")) // CSS 쿼리를 사용하여 요소 선택
                    titles = list.findElements(By.cssSelector(".ellipsis > a"))
                    for (element in titles!!) {
                        data.add(element.getAttribute("title"))
                        println(element.getAttribute("title"))
                    }
                }
                Thread.sleep(3000)
                nextButton = driver.findElement(By.id("next_btn_img"))
                } catch (e: Exception) {
                    Thread.sleep(5000)
                    jse.executeScript("window.scrollTo(0, document.body.scrollHeight / 2)")
                    var list = driver.findElement(By.id("contentsListWrap")) // CSS 쿼리를 사용하여 요소 선택
                    var titles = list.findElements(By.cssSelector(".ellipsis > a"))
                    for (element in titles!!) {
                        data.add(element.getAttribute("title"))
                        println(element.getAttribute("title"))
                    }
                    nextButton.click()
                    if (data[data.size - 101] == titles[0].getAttribute("title")) {
                        list = driver.findElement(By.id("contentsListWrap")) // CSS 쿼리를 사용하여 요소 선택
                        titles = list.findElements(By.cssSelector(".ellipsis > a"))
                        for (element in titles!!) {
                            data.add(element.getAttribute("title"))
                            println(element.getAttribute("title"))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("connecting to error")
            println(e.message)
            println("================================================================================================================================================")
            e.printStackTrace()
        }

        return data
    }
}


fun main() {
    val me2Disk = Me2Disk()
    val data = me2Disk.crawling()
    me2Disk.createWorkbook(data)
}