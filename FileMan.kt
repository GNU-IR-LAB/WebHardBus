package utils
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.lang.Thread.sleep
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

class FileMan {
    object ChromeDriverCreator {
        fun createDriver(): ChromeDriver {
            // 크롬 드라이버 경로 설정
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\HAEDAL\\Desktop\\HAEDAL\\연구실 관련\\저작권\\vscode\\file_man\\untitled\\src\\main\\kotlin\\chromedriver-win64\\chromedriver.exe")

            // 디버거 크롬 옵션 추가
            val chromeOptions = ChromeOptions()

            // 크롬 실행 파일 경로 설정
            chromeOptions.setBinary("C:\\Users\\HAEDAL\\Desktop\\chrome-win32\\chrome.exe")

            chromeOptions.addArguments("--remote-allow-origins=*")

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
        val sheet = workbook.createSheet("file_man manga")

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
            val outputStream = FileOutputStream("File_Man_titles.xlsx")
            workbook.write(outputStream)
            workbook.close()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun crawlingWebtoon(): MutableList<String> {
        val titlesList = mutableListOf<String>()

        val driver = ChromeDriverCreator.createDriver()
        try {
            driver.get("https://www.fileman.co.kr/")
            val usernameField = driver.findElement(By.id("m_id"))
            val passwordField = driver.findElement(By.id("m_pwd"))

            usernameField.sendKeys("jang4282002")
            passwordField.sendKeys("0130lee")

            val loginButton = driver.findElement(By.xpath("//*[@id=\"mainLoginForm\"]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/input"))
            loginButton.click()

            sleep(4000)

            val categoryButton = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table[2]/tbody/tr[1]/td[2]/table/tbody/tr/td/table/tbody/tr[1]/td[2]/a[9]"))
            categoryButton.click()

            sleep(4000)

            val mangaButton = driver.findElement(By.xpath("//*[@id=\"layer_MVO\"]/div/a[3]/span"))
            mangaButton.click()

            sleep(4000)

            var titlesElements: List<WebElement> = driver.findElements(By.className("cenlin_new"))

            // 각 요소의 텍스트를 추출하여 리스트에 추가
            for (titleElement in titlesElements) {
                titlesList.add(titleElement.text)
            }
            sleep(1000)

            val page2Button = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table[1]/tbody/tr/td[1]/div/a[1]"))

            page2Button.click()
            sleep(1000)
            titlesElements = driver.findElements(By.className("cenlin_new"))

            // 각 요소의 텍스트를 추출하여 리스트에 추가
            for (titleElement in titlesElements) {
                titlesList.add(titleElement.text)
            }

            val page3Button = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table[1]/tbody/tr/td[1]/div/a[2]"))

            page3Button.click()
            sleep(1000)
            titlesElements = driver.findElements(By.className("cenlin_new"))

            // 각 요소의 텍스트를 추출하여 리스트에 추가
            for (titleElement in titlesElements) {
                titlesList.add(titleElement.text)
            }


        } catch (e: Exception) {
            e.printStackTrace()
            print("connecting to error")
        } finally {
            // 작업이 끝나면 드라이버를 종료합니다.
            driver.quit()
        }
        return titlesList
    }
}


fun main() {
    val fileMan = FileMan()
    var data = fileMan.crawlingWebtoon()
    fileMan.createWorkbook(data)


}