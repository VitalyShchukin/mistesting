package brizplus.mistesting;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BornSertificatesTest {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static NavigateMIS mis;
    private static NavigateN2O n2o;
    private static String startUrl;
    private static String username;
    private static String password;

    @BeforeClass
    public static void authorizationAndGoToCertificates_success() throws SQLException, ClassNotFoundException {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        mis = new NavigateMIS();
        n2o = new NavigateN2O();
        startUrl = "https://mis-test-app.tambov.gov.ru/";
        username = "user3";
        password = "Qwe147asd";

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(startUrl); //link to checking page
        mis.authorization(driver, username, password); //авторизация

        mis.goToPharmacy(driver); //переход в Аптека. Документы (для вызова контекста)

        n2o.selectionRCK(driver); //вызов контекста и установка РЦК для "Луки"
        n2o.setupMedOrg(driver);

        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        n2o.goToCertificates(driver);//переход в Электронные родовые сертификаты
    }

    @AfterClass
    public static void finishTesting_success() {
        driver.quit();
    }

    @Test
    public void walkAlongN2O() throws SQLException, ClassNotFoundException {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        DataForTests dataForTests = new DataForTests();
        n2o.createNewCertificate(driver,
                dataForTests.checkDate(),
                dataForTests.getXDate(),
                dataForTests.getGestAge(),
                dataForTests.getPatient());//создание нового сертификата


        String birthDateFullNameSnilsDB = dataForTests.getDataFromDB();
        System.out.println(birthDateFullNameSnilsDB);

        String birthDateFullNameSnilsInterf=n2o.findFields(driver, dataForTests.getSnils());
        System.out.println(birthDateFullNameSnilsInterf);

        if(birthDateFullNameSnilsDB.equals(birthDateFullNameSnilsInterf)){
            System.out.println("Данные в базе и в интерфейсе идентичны");
        } else {
            System.out.println("Данные в базе и в интерфейсе разные");
        }

        n2o.sendToFSS(driver);
    }
}
