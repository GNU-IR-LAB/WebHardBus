package utils
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.lang.Thread.sleep
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.openqa.selenium.support.ui.Select
import java.io.FileOutputStream

class FileMaru() {
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


    fun createWorkbook(data: List<String>, flag: Boolean) {
        val workbook = XSSFWorkbook()
        if (flag) {
            // 새 시트 생성
            val sheet = workbook.createSheet("file_maru manga")

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
                val outputStream = FileOutputStream("File_Maru_titles.xlsx")
                workbook.write(outputStream)
                workbook.close()
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            // 새 시트 생성
            val sheet = workbook.createSheet("file_maru novel")

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
                val outputStream = FileOutputStream("File_Maru_Novel_titles.xlsx")
                workbook.write(outputStream)
                workbook.close()
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun crawling(): MutableList<String> {
        val titlesList = mutableListOf<String>()

        val driver = ChromeDriverCreator.createDriver()
        try {
            driver.get("https://m.filemaru.com/")
            sleep(4000)
            val categoryBar = driver.findElement(By.id("smenu_box"))

            val categoryButton = categoryBar.findElement(By.id("COM"))
            categoryButton.click()

            sleep(4000)

            var nextButton = driver.findElement(By.xpath("//*[@id=\"infScrollList\"]/ul/div/a[6]"))
            var onclickAttribute = nextButton.getAttribute("onclick")
            while(onclickAttribute != null) {
                nextButton.click()
                sleep(5000)

                val table = driver.findElement(By.id("infScrollList"))
                var titlesElements: List<WebElement> = table.findElements(By.className("b_tit2"))

                // 각 요소의 텍스트를 추출하여 리스트에 추가
                for (titleElement in titlesElements) {
                    titlesList.add(titleElement.text)
                    println(titleElement.text)
                }

                nextButton = driver.findElement(By.xpath("//*[@id=\"infScrollList\"]/ul/div/a[6]"))
                onclickAttribute = nextButton.getAttribute("onclick")
            }


        } catch (e: Exception) {
            println("connecting to error")
            println(e.message)
            println("================================================================================================================================================")
            e.printStackTrace()
        } finally {
            // 작업이 끝나면 드라이버를 종료합니다.
            driver.quit()
        }
        println("titleList: $titlesList")
        return titlesList
    }

    fun crawlingNovel(): MutableList<String> {
        val titlesList = mutableListOf<String>()

        val driver = ChromeDriverCreator.createDriver()
        try {
            driver.get("https://m.filemaru.com/")
            sleep(4000)
            val categoryBar = driver.findElement(By.id("smenu_box"))

            val categoryButton = categoryBar.findElement(By.id("COM"))
            categoryButton.click()

            sleep(4000)

            var nextButton = driver.findElement(By.xpath("//*[@id=\"infScrollList\"]/ul/div/a[6]"))
            var onclickAttribute = nextButton.getAttribute("onclick")
            while(onclickAttribute != null) {
                nextButton.click()
                sleep(5000)

                val table = driver.findElement(By.id("infScrollList"))
                var titlesElements: List<WebElement> = table.findElements(By.className("b_tit2"))

                // 각 요소의 텍스트를 추출하여 리스트에 추가
                for (titleElement in titlesElements) {
                    titlesList.add(titleElement.text)
                    println(titleElement.text)
                }

                nextButton = driver.findElement(By.xpath("//*[@id=\"infScrollList\"]/ul/div/a[6]"))
                onclickAttribute = nextButton.getAttribute("onclick")
            }


        } catch (e: Exception) {
            println("connecting to error")
            println(e.message)
            println("================================================================================================================================================")
            e.printStackTrace()
        } finally {
            // 작업이 끝나면 드라이버를 종료합니다.
            driver.quit()
        }
        println("titleList: $titlesList")
        return titlesList
    }
}

fun main() {
    val fileMaru = FileMaru()
    val data = fileMaru.crawlingNovel()
    fileMaru.createWorkbook(data,false)
}