package com.example.niooii.google.classroom;

import com.example.niooii.chromedriver.ChromeDriverInit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Classroom {
    private final String email;
    private final String password;
    private final WebDriver driver;

    //Our constructor. Sets our username and password and does some client config.
    Classroom(String email, String password){
        this.email = email;
        this.password = password;
        driver =  new ChromeDriverInit().getChromeDriver();
    }

    public void login() {
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://accounts.google.com/signin");

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='identifierId']")))
                .sendKeys(email);
        driver.findElement(By.id("identifierNext")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='Passwd']")))
                .sendKeys(password);
        driver.findElement(By.id("passwordNext")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@class='BBRNg']")));
    }

    public void homepage(){
        driver.get("https://classroom.google.com/");
    }

    public void quit(){
        driver.manage().deleteAllCookies();
        driver.quit();
    }
}