package net.elenx.epomis.webscraping.htmlunit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.elenx.epomis.webscraping.UdemyStatics.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class Udemy {

    private static final Logger LOG = Logger.getLogger(Udemy.class.getName());

    private static final int TIMEOUT_SEC = 5;
    private static final int TIMEOUT_MILLIS = 5000;

    public static void main(String[] args) {
        Udemy udemyHtmlUnit = new Udemy();
        udemyHtmlUnit.listJavaCourses();
//        udemyHtmlUnit.listJavaCoursesWithJsoup();
    }

    public Udemy() {
    }

    public void listJavaCourses() {
        try (WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52)) {
            final HtmlPage javaCoursesPage = webClient.getPage(SEARCH_JAVA_URL);
            webClient.waitForBackgroundJavaScript(TIMEOUT_MILLIS);
            LOG.info(javaCoursesPage.getTitleText());
            LOG.info(javaCoursesPage.asXml());

            final DomNodeList<DomNode> courseCards = javaCoursesPage.querySelectorAll("div[data-purpose=search-course-cards]");
            LOG.log(Level.INFO, "search-course-cards find:{0}", courseCards.size());

            for (DomNode courseCard : courseCards) {
                DomNode courseLink = courseCard.querySelector("a[data-purpose=search-course-card-title]");
                LOG.log(Level.INFO, "courseLink: {0}", courseLink.getAttributes().getNamedItem("href"));
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void listJavaCoursesWithJsoup() {

        try (WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52)) {
            final HtmlPage javaCoursesPageIn = webClient.getPage(SEARCH_JAVA_URL);
            webClient.waitForBackgroundJavaScript(TIMEOUT_MILLIS);
            LOG.info(javaCoursesPageIn.getTitleText());
            LOG.info(javaCoursesPageIn.asXml());

            Document javaCoursesPage = Jsoup.parse(javaCoursesPageIn.asXml());
            Elements courseCards = javaCoursesPage.select("div[data-purpose=search-course-cards]");
            LOG.log(Level.INFO, "search-course-cards find:{0}", courseCards.size());

            for (Element courseCard : courseCards) {
                Elements courseLink = courseCard.select("a[data-purpose=search-course-card-title]");
                LOG.log(Level.INFO, "courseLink:{0}", courseLink.get(0).html());
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void findCoursesHtmlUnitStrict(String text) {
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        try {
            final HtmlPage basePage = webClient.getPage(BASE_URL);
            LOG.info(basePage.getBaseURI());
            HtmlForm searchForm = HtmlUnitHelper.getFormById(basePage, SEARCH_FORM_ID);
            final HtmlTextInput textField = searchForm.getInputByName("q");
            textField.setValueAttribute(text);

            HtmlButton submitButton = HtmlUnitHelper.getSubmitButtonByAttribute(searchForm, DATA_PURPOSE, "do-search");

//            HtmlPage coursesPage = (HtmlPage) textField.type(KeyboardEvent.DOM_VK_RETURN);
            HtmlPage coursesPage = submitButton.click();
            LOG.info(coursesPage.getBaseURI());
            webClient.waitForBackgroundJavaScript(TIMEOUT_MILLIS);

            List<HtmlAnchor> anchors = coursesPage.getAnchors();
            for (HtmlAnchor anchor : anchors) {
                String attribute = anchor.getAttribute(DATA_PURPOSE);
                if (attribute.equals("search-course-card-title")) {
                    LOG.info(anchor.asText());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
