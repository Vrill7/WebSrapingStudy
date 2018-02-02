package net.elenx.epomis.webscraping.htmlunit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.List;

import static net.elenx.epomis.webscraping.UdemyStatics.*;

public class Udemy {

    private static final int TIMEOUT_MILLIS = 5000;
    private static final int TIMEOUT_SEC = 5;

    public static void main(String[] args) {
        Udemy udemy = new Udemy();
        udemy.findCoursesHtmlUnitStrict("java");
    }

    public Udemy() {   }

    public void findCoursesHtmlUnitStrict(String text) {
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        try {
            final HtmlPage basePage = webClient.getPage(BASE_URL);
            System.out.println(basePage.getBaseURI());
            HtmlForm searchForm = HtmlUnitHelper.getFormById(basePage, SEARCH_FORM_ID);
            final HtmlTextInput textField = searchForm.getInputByName("q");
            textField.setValueAttribute(text);

            HtmlButton submitButton = HtmlUnitHelper.getSubmitButtonByAttribute(searchForm, DATA_PURPOSE, "do-search");

//            HtmlPage coursesPage = (HtmlPage) textField.type(KeyboardEvent.DOM_VK_RETURN);
            HtmlPage coursesPage = submitButton.click();
            System.out.println(coursesPage.getBaseURI());
            webClient.waitForBackgroundJavaScript(TIMEOUT_MILLIS);

            List<HtmlAnchor> anchors = coursesPage.getAnchors();
            for (HtmlAnchor anchor : anchors) {
                String attribute = anchor.getAttribute(DATA_PURPOSE);
//                System.out.println(attribute);
                if (attribute.equals("search-course-card-title")) {
                    System.out.println(anchor);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
