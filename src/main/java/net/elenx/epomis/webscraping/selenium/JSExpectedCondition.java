package net.elenx.epomis.webscraping.selenium;

import java.util.logging.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 *
 * @author Piotr Karasiński
 */
public class JSExpectedCondition {

    private static final Logger LOG = Logger.getLogger(JSExpectedCondition.class.getName());

    public static ExpectedCondition<Boolean> getJQueryExpectedCondition(WebDriver driver) {
        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    // no jQuery present
                    return true;
                }
            }
        };
        return jQueryLoad;
    }

    public static ExpectedCondition<Boolean> getJavaScriptExpectedCondition(WebDriver driver) {

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState")
                        .toString().equals("complete");
            }
        };
        return jsLoad;
    }
}
