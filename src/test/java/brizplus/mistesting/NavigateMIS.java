package brizplus.mistesting;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NavigateMIS {

    public void authorization(WebDriver driver, String username, String password) {
        driver.findElement(By.cssSelector("#username"))
                .sendKeys(username);
        driver.findElement(By.cssSelector("#password"))
                .sendKeys(password);
        driver.findElement(By.cssSelector("#submit"))
                .click();
    }

    public void goToPharmacy(WebDriver driver) {
        driver.findElement(By.linkText("Аптека. Документы"))
                .click();
    }
}
