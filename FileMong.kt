package utils
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.lang.Thread.sleep
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.support.ui.Select
import java.io.FileOutputStream

class FileMong() {
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
            val sheet = workbook.createSheet("file_mong manga")

            val list = mutableListOf(
                listOf("number", "title"),
            )

            for (i in data.indices) {
                list.add(listOf((i + 1).toString(), data[i]))
            }

            for ((rowNum, dataRow) in list.withIndex()) {
                val row = sheet.createRow(rowNum)
                for ((cellNum, value) in dataRow.withIndex()) {
                    val cell = row.createCell(cellNum)
                    cell.setCellValue(value)
                }
            }

            try {
                val outputStream = FileOutputStream("File_Mong_titles.xlsx")
                workbook.write(outputStream)
                workbook.close()
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            val sheet = workbook.createSheet("file_mong novel")

            val list = mutableListOf(
                listOf("number", "title"),
            )

            for (i in data.indices) {
                list.add(listOf((i + 1).toString(), data[i]))
            }

            for ((rowNum, dataRow) in list.withIndex()) {
                val row = sheet.createRow(rowNum)
                for ((cellNum, value) in dataRow.withIndex()) {
                    val cell = row.createCell(cellNum)
                    cell.setCellValue(value)
                }
            }

            try {
                val outputStream = FileOutputStream("File_Mong_novel_titles.xlsx")
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
            driver.get("https://filemong.com/contents/list.html?section=DOC#fix")
            val jse = driver as JavascriptExecutor
            sleep(4000)

            val category = driver.findElement(By.xpath("//*[@id=\"category\"]/ul[1]/li[6]/a/i"))
            category.click()

            sleep(2000)

            val mangaButton = driver.findElement(By.xpath("//*[@id=\"content\"]/header/ul/li[1]/a"))
            mangaButton.click()

            for (i in 1..149) {
                val table = driver.findElement(By.className("content-list"))
                var titlesElements: List<WebElement> = table.findElements(By.className("ctit"))

                // 각 요소의 텍스트를 추출하여 리스트에 추가
                for (titleElement in titlesElements) {
                    titlesList.add(titleElement.text)
                    println(titleElement.text)
                }

                sleep(3000)
                jse.executeScript("set_contents_option('paging', $i);")
                jse.executeScript("get_contents_list('reload');")
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
        return titlesList
    }

    fun crawlingNovel(): MutableList<String> {
        val titlesList = mutableListOf<String>()

        val driver = ChromeDriverCreator.createDriver()
        try {
            driver.get("https://filemong.com/contents/list.html?section=DOC#fix")
            val jse = driver as JavascriptExecutor
            sleep(4000)

            val category = driver.findElement(By.xpath("//*[@id=\"category\"]/ul[1]/li[6]/a/i"))
            category.click()

            sleep(2000)

            val novelButton = driver.findElement(By.xpath("//*[@id=\"content\"]/header/ul/li[2]/a"))
            novelButton.click()

            for (i in 1..149) {
                val table = driver.findElement(By.className("content-list"))
                var titlesElements: List<WebElement> = table.findElements(By.className("ctit"))

                // 각 요소의 텍스트를 추출하여 리스트에 추가
                for (titleElement in titlesElements) {
                    titlesList.add(titleElement.text)
                    println(titleElement.text)
                }

                sleep(3000)
                jse.executeScript("set_contents_option('paging', $i);")
                jse.executeScript("get_contents_list('reload');")
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
        return titlesList
    }
}

fun main() {
    val fileMong = FileMong()
//    var data = fileMong.crawling()
//    fileMong.createWorkbook(data, true)

    var data = fileMong.crawlingNovel()

    fileMong.createWorkbook(data, false)
}