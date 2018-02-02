package net.elenx.epomis.webscraping.htmlunit;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;

import java.util.List;
import java.util.Set;

/**
 * @author Piotr Karasiński email: parzno@o2.pl
 */
public class HtmlUnitHelper {


    public static final String CLASS = "class";

    public static HtmlForm getFormById(HtmlPage page, String formId) throws ElementNotFoundException {
        List<HtmlForm> forms = page.getForms();
        for (HtmlForm form : forms) {
            if (form.getId().equals(formId)) {
                return form;
            }
        }
        throw new ElementNotFoundException(HtmlForm.TAG_NAME, "formId", formId);
    }

    public static HtmlButton getSubmitButtonByAttribute(HtmlForm form, String attributeName, String attributeValue) throws ElementNotFoundException {
        List<HtmlButton> submitButtons = form.getElementsByAttribute(HtmlButton.TAG_NAME, attributeName, attributeValue);
        if (submitButtons.isEmpty()) {
            throw new ElementNotFoundException(HtmlButton.TAG_NAME, "attributeName", attributeName);
        }
        return submitButtons.get(0);
    }

    public static List<HtmlElement> getHtmlElementByClass(HtmlPage page, String tagName, String attributeValue) throws ElementNotFoundException {
        HtmlElement body = page.getBody();
        List<HtmlElement> elementsByAttribute = body.getElementsByAttribute(tagName, CLASS, attributeValue);
        return elementsByAttribute;
    }


    public static List<HtmlElement> getHtmlElementByAttribute(HtmlPage page, String tagName, String attributeName, String attributeValue) throws ElementNotFoundException {
        HtmlElement body = page.getBody();

        List<HtmlElement> elementsByAttribute = body.getElementsByAttribute(tagName, attributeName, attributeValue);
        return elementsByAttribute;
    }


    // printing
    public static void printAllAnchors(HtmlPage page) {
        List<HtmlAnchor> anchors = page.getAnchors();
        for (HtmlAnchor anchor : anchors) {
            System.out.println(anchor);
        }
    }

    public static void printAllForms(HtmlPage page) {
        List<HtmlForm> forms = page.getForms();
        System.out.println("forms count:" + forms.size());
        for (HtmlForm form : forms) {
            System.out.println(form.toString());
        }
    }

    public static void printCookies(WebClient webClient) {
        Set<Cookie> cookies = webClient.getCookieManager().getCookies();
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName());
        }
    }
}
