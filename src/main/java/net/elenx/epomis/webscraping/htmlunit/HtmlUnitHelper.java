package net.elenx.epomis.webscraping.htmlunit;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import java.io.IOException;

import java.util.List;
import java.util.Set;

/**
 * Więcej info http://htmlunit.sourceforge.net/gettingStarted.html
 *
 * @author Piotr Karasiński email: parzno@o2.pl
 */
class HtmlUnitHelper {

    static final String CLASS = "class";
    static final int TIMEOUT_MILLIS = 7000;
    static final int TIMEOUT_SEC = 5;

    static HtmlPage getAjaxPage(WebClient webClient, String pageUrl) throws IOException {
        final HtmlPage htmlPage = webClient.getPage(pageUrl);
        System.out.println("### - waiting");
        webClient.waitForBackgroundJavaScript(TIMEOUT_MILLIS);
        return htmlPage;
    }

    static HtmlForm getFormById(HtmlPage page, String formId) throws ElementNotFoundException {
        List<HtmlForm> forms = page.getForms();
        for (HtmlForm form : forms) {
            if (form.getId().equals(formId)) {
                return form;
            }
        }
        throw new ElementNotFoundException(HtmlForm.TAG_NAME, "formId", formId);
    }

    static HtmlButton getSubmitButtonByAttribute(HtmlForm form, String attributeName, String attributeValue) throws ElementNotFoundException {
        List<HtmlButton> submitButtons = form.getElementsByAttribute(HtmlButton.TAG_NAME, attributeName, attributeValue);
        if (submitButtons.isEmpty()) {
            throw new ElementNotFoundException(HtmlButton.TAG_NAME, "attributeName", attributeName);
        }
        return submitButtons.get(0);
    }

    static HtmlButton getSubmitButtonById(HtmlForm form, String buttonId) throws ElementNotFoundException {
        DomNodeList<HtmlElement> submitButtons = form.getElementsByTagName(HtmlButton.TAG_NAME);
        if (submitButtons.isEmpty()) {
            throw new ElementNotFoundException(HtmlButton.TAG_NAME, "attributeName", "");
        }
        for (HtmlElement submitButton : submitButtons) {
            if (submitButton.getAttribute("id").equals(buttonId)) {
                return (HtmlButton) submitButton;
            } 
        }
        return null;
    }

    static List<HtmlElement> getHtmlElementByClass(HtmlPage page, String tagName, String attributeValue) throws ElementNotFoundException {
        HtmlElement body = page.getBody();
        List<HtmlElement> elementsByAttribute = body.getElementsByAttribute(tagName, CLASS, attributeValue);
        return elementsByAttribute;
    }

    static List<HtmlElement> getHtmlElementByAttribute(HtmlPage page, String tagName, String attributeName, String attributeValue) throws ElementNotFoundException {
        HtmlElement body = page.getBody();

        List<HtmlElement> elementsByAttribute = body.getElementsByAttribute(tagName, attributeName, attributeValue);
        return elementsByAttribute;
    }

    // printing
    static void printAllAnchors(HtmlPage page) {
        List<HtmlAnchor> anchors = page.getAnchors();
        anchors.forEach((anchor) -> {
            System.out.println(anchor);
        });
    }

    static void printAllForms(HtmlPage page) {
        List<HtmlForm> forms = page.getForms();
        System.out.println("forms count:" + forms.size());
        forms.forEach((form) -> {
            System.out.println(form.toString());
        });
    }

    static void printCookies(WebClient webClient) {
        System.out.println("## printCookies");
        Set<Cookie> cookies = webClient.getCookieManager().getCookies();
        cookies.forEach((cookie) -> {
            System.out.println(cookie.getName());
        });
        System.out.println("## printCookies end");
    }
}
