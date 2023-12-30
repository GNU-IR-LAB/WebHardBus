package utils
import org.jsoup.Jsoup
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.openqa.selenium.By
import org.openqa.selenium.NoAlertPresentException
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.FileOutputStream
import java.lang.Thread.sleep

class FileIs {
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

        if(flag) {
            // 새 시트 생성
            val sheet = workbook.createSheet("file_is manga")

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
                val outputStream = FileOutputStream("File_Is_titles.xlsx")
                workbook.write(outputStream)
                workbook.close()
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            val sheet = workbook.createSheet("file_is novel")

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
        }

        try {
            val outputStream = FileOutputStream("File_Is_Novel_titles.xlsx")
            workbook.write(outputStream)
            workbook.close()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun crawlingToon(): MutableList<String> {
        val data = mutableListOf<String>()
        val driver = ChromeDriverCreator.createDriver()

        try {
            driver.get("https://fileis.com/contents/index.htm?category1=CTN&rows=100&viewTab=new#1")
            var nextButton = driver.findElement(By.id("next_btn_img"))
            for (i in 1..500) {
                val list = driver.findElement(By.id("contentsListWrap")) // CSS 쿼리를 사용하여 요소 선택
                val titles = list.findElements(By.className("title"))
                for (element in titles!!) {
                    data.add(element.getAttribute("title"))
                    println(element.getAttribute("title"))
                }
                nextButton.click()
                sleep(2000)
                nextButton = driver.findElement(By.id("next_btn_img"))
            }
        } catch (e: Exception) {
            println("connecting to error")
            println(e.message)
            println("===================================================================================================")
            e.printStackTrace()
        }

        return data
    }

    fun crawlingNovel(): MutableList<String> {
        val data = mutableListOf<String>()
        val driver = ChromeDriverCreator.createDriver()

        try {
            driver.get("https://fileis.com/contents/index.htm?category1=DOC&rows=100&viewTab=new#1")
            var nextButton = driver.findElement(By.id("next_btn_img"))
            for (i in 1..500) {
                sleep(2000)
                val list = driver.findElement(By.id("contentsListWrap")) // CSS 쿼리를 사용하여 요소 선택
                val titles = list.findElements(By.className("title"))
                for (element in titles!!) {
                    data.add(element.getAttribute("title"))
                    println(element.getAttribute("title"))
                }
                sleep(2000)
                nextButton.click()
                sleep(2000)
                nextButton = driver.findElement(By.id("next_btn_img"))
            }
        } catch (e: Exception) {
            println("connecting to error")
            println(e.message)
            println("===================================================================================================")
            e.printStackTrace()
        }

        return data
    }
}

fun main() {
    val fileIs = FileIs()
//    var data = fileIs.crawlingToon()
//    fileIs.createWorkbook(data, true)

    val data = fileIs.crawlingNovel()
    fileIs.createWorkbook(data, false)
}