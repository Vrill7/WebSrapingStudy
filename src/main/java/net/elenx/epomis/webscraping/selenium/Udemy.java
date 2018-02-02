package net.elenx.epomis.webscraping.selenium;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import static net.elenx.epomis.webscraping.UdemyStatics.*;

public class Udemy {

    private static final int TIMEOUT_MILLIS = 5000;
    private static final int TIMEOUT_SEC = 5;

    public static void main(String[] args) {
        Udemy udemy = new Udemy();
    }

    public Udemy() {
        try {
            findCoursesSeleniumHtmlUnit("java");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void findCoursesSeleniumHtmlUnit(String text) throws InterruptedException {
        WebDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_52, Boolean.TRUE);
        driver.get(BASE_URL);

        WebElement textField = driver.findElement(By.name("q"));
        // Enter something to search for
        textField.sendKeys(text);
        System.out.println("start Url is: " + driver.getCurrentUrl());
        // Now submit the form. WebDriver will find the form for us from the element
        textField.submit();
        System.out.println("Url after submit is: " + driver.getCurrentUrl());

        // Wait for the page to load, timeout after 10 seconds
        Thread.sleep(TIMEOUT_MILLIS);// webdriverwait nie radzi sobie 
        WebDriverWait driverWait = new WebDriverWait(driver, TIMEOUT_SEC);
        WebElement searchWrapper = driver.findElement(By.cssSelector("div.ud-angular-loaded"));
        // wait for Javascript to load
//        ExpectedCondition angularLoaded = ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.ud-angular-loaded"));
        ExpectedCondition angularLoaded = ExpectedConditions.presenceOfElementLocated(By.cssSelector("a.search-course-card--card__title--1moSD"));
//        driverWait.until(angularLoaded); // za wczesnie reaguje zwraca tylko np. 3 lub 5 wynikow z 12
//        driverWait.until(JSExpectedCondition.getJavaScriptExpectedCondition(driver));
//        driverWait.until(JSExpectedCondition.getJQueryExpectedCondition(driver));

//        System.out.println("driver.getPageSource()" + driver.getPageSource());
        List<WebElement> findElements = driver.findElements(By.cssSelector("a.search-course-card--card__title--1moSD"));
        System.out.println("findElements:" + findElements.size());
        for (WebElement element : findElements) {
            System.out.println(element.getAttribute("href"));
        }
        //Close the browser
        driver.quit();
    }
}
