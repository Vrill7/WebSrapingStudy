package net.elenx.epomis.webscraping.selenium;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import java.io.IOException;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import net.elenx.epomis.webscraping.LinkedinStatics;

import org.openqa.selenium.support.ui.WebDriverWait;

import static net.elenx.epomis.webscraping.LinkedinStatics.*;

public class LinkedIn {

    private static final int TIMEOUT_MILLIS = 5000;
    private static final int TIMEOUT_SEC = 5;
    private WebDriver driver;

    public static void main(String[] args) {
        LinkedIn linkedIn = new LinkedIn();
    }

    public LinkedIn() {
        testHtmlUnitStrict();
//        driver = new HtmlUnitDriver(BrowserVersion.INTERNET_EXPLORER, true);
//        driver.get(BASE_URL);
//        System.out.println("start Url is: " + driver.getCurrentUrl());
    }

    private void findCoursesSeleniumHtmlUnit(String text) throws InterruptedException {

        List<WebElement> findElements = driver.findElements(By.cssSelector("a.search-course-card--card__title--1moSD"));
//        List<WebElement> findElements = driver.findElements(By.tagName("a"));
        System.out.println("findElements:" + findElements.size());
        for (WebElement element : findElements) {
            System.out.println(element.getAttribute("href"));
        }
        //Close the browser
        driver.quit();
    }

    LinkedIn login(String login, String password) {
        System.out.println("login");
        WebElement loginTextField = driver.findElement(By.id(INPUT_LOGIN_ID));
        WebElement passwordTextField = driver.findElement(By.id(INPUT_PASSWORD_ID));
        // Enter something to search for
        loginTextField.sendKeys(login);
        passwordTextField.sendKeys(password);
        // Now submit the form. WebDriver will find the form for us from the element
        passwordTextField.submit();
        waiting();
        System.out.println("after login Url is: " + driver.getCurrentUrl());
        return this;
    }

    LinkedIn search(String java) {
        System.out.println("searching...");

        waiting();
        System.out.println("after search Url is: " + driver.getCurrentUrl());
        return this;
    }

    void showResults() {
        waiting();
        System.out.println("results:");
    }

    void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    private void waiting() {
        System.out.println("Waiting...");
        //        Thread.sleep(TIMEOUT_MILLIS); // webdriverwait nie radzi sobie 
        WebDriverWait driverWait = new WebDriverWait(driver, TIMEOUT_SEC);
        driverWait.until(JSExpectedCondition.getJavaScriptExpectedCondition(driver));
    }


    private void testHtmlUnitStrict() {
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        webClient.getCookieManager().setCookiesEnabled(true);
        Cookie langCookie = new Cookie(".linkedin.com", "lang","\"v=2&lang=pl-pl\"");
        Cookie bcookie = new Cookie(".linkedin.com", "bcookie","\"v=2&88d1f2f2-85f3-4ee7-84f4-823694080a9c\"");
        webClient.getCookieManager().addCookie(langCookie);
//        webClient.getCookieManager().addCookie(bcookie);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        try {
            final HtmlPage basePage = webClient.getPage(BASE_URL);
            System.out.println(basePage.getBaseURI());


            DomElement elementById = basePage.getElementById("global-alert-queue");
            System.out.println(elementById.getTextContent());


            List<HtmlForm> forms = basePage.getForms();
            System.out.println("forms count:" + forms.size());
            for (HtmlForm form : forms) {
                System.out.println(form.toString());
            }
            if (forms.size() >= 1) {
                HtmlForm form = forms.get(0);
                System.out.println(form.toString());
                HtmlInput session_key = form.getInputByName("session_key");
                HtmlInput session_password = form.getInputByName("session_password");
                session_key.setValueAttribute("");
                session_password.setValueAttribute("");
                HtmlSubmitInput submitInput = (HtmlSubmitInput) basePage.getElementById(INPUT_SUBMIT_ID);
                HtmlPage coursesPage = submitInput.click();

                webClient.waitForBackgroundJavaScript(TIMEOUT_MILLIS);

                DomElement alert = coursesPage.getElementById("global-alert-queue");
                System.out.println(alert.getTextContent());

//                System.out.println(coursesPage.getBaseURI());
//                List<HtmlAnchor> anchors = coursesPage.getAnchors();
//                for (HtmlAnchor anchor : anchors) {
//                    System.out.println(anchor);
//                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
