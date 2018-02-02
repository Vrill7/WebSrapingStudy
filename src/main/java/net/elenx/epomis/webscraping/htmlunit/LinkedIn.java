package net.elenx.epomis.webscraping.htmlunit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.openqa.selenium.Keys;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static net.elenx.epomis.webscraping.LinkedinStatics.*;

public class LinkedIn {

    private static final int TIMEOUT_MILLIS = 5000;
    private static final int TIMEOUT_SEC = 5;

    public static void main(String[] args) {
        LinkedIn linkedIn = new LinkedIn();
    }

    public LinkedIn() {
        testHtmlUnitStrict();
    }


    private void testHtmlUnitStrict() {
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setTimeout(2000);
        webClient.getOptions().setUseInsecureSSL(true);
        // overcome problems in JavaScript
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        try {
            final HtmlPage basePage = webClient.getPage(BASE_URL);
            System.out.println(basePage.getBaseURI());
            HtmlUnitHelper.printCookies(webClient);

            DomElement elementById = basePage.getElementById("global-alert-queue");
            System.out.println(elementById.getTextContent());

            HtmlForm form = basePage.getForms().get(0);
            System.out.println(form.toString());
            HtmlInput session_key = form.getInputByName("session_key");
            HtmlInput session_password = form.getInputByName("session_password");
            session_key.setValueAttribute("login@login.pl");
            session_password.setValueAttribute("password");
            // nie wydsyla formularza
//            HtmlPage coursesPage = (HtmlPage) session_password.type(KeyboardEvent.DOM_VK_RETURN);
            HtmlSubmitInput submitInput = (HtmlSubmitInput) basePage.getElementById(INPUT_SUBMIT_ID);
            HtmlPage coursesPage = submitInput.click();

            webClient.waitForBackgroundJavaScript(TIMEOUT_MILLIS);

            System.out.println(coursesPage.getBody().asText());
            System.out.println(coursesPage.getBody().asXml());

            List<HtmlElement> identity_welcome_message = coursesPage.getBody().getElementsByAttribute(HtmlDivision.TAG_NAME, "data-control-name", "identity_welcome_message");
            System.out.println(identity_welcome_message.toString());
            System.out.println(coursesPage.getBaseURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
