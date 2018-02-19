package net.elenx.epomis.webscraping.htmlunit;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static net.elenx.epomis.webscraping.LinkedinStatics.*;

class LinkedIn {

    private static final Logger LOG = Logger.getLogger(LinkedIn.class.getName());
    public static void main(String[] args) {
        // Valid values for this property are "trace", "debug", "info", "warn", "error", or "fatal"
        System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "info");
        System.getProperties().put("org.apache.commons.logging.com.gargoylesoftware.htmlunit.html.HTMLParserListener", "fatal");
        LinkedIn linkedIn = new LinkedIn();
    }

    public LinkedIn() {
        testHtmlUnitStrict(WebClientConf.getConfiguredWebClient());
    }

    private void testHtmlUnitStrict(WebClient webClient) {
        try {
            LOG.info("#1 - base url");
            final HtmlPage basePage = webClient.getPage(BASE_URL);
            LOG.info(basePage.getBaseURI());
            HtmlUnitHelper.printCookies(webClient);

            LOG.info("#2 - alerty ze strony");
            showAlerts(basePage);

            LOG.info("#3 - get forms");
            HtmlForm loginForm = basePage.getForms().get(0);
            LOG.info(loginForm.toString());

            LOG.info("#4 - get form input");
            HtmlInput session_key = loginForm.getInputByName(INPUT_LOGIN_NAME);
            HtmlInput session_password = loginForm.getInputByName(INPUT_PASSWORD_NAME);
            session_key.setValueAttribute("login@login.pl");
            session_password.setValueAttribute("password");

            LOG.info("#5 - send login form");
            HtmlPage feedPage = addSubmitButtonAndSend(webClient, basePage, loginForm);

            LOG.info("#7 - feedPage as text");
            LOG.info(feedPage.getBody().asText());
//            LOG.info("#7 - coursesPage as xml");
//            LOG.info(coursesPage.getBody().asXml());

            LOG.info("#8 - feedPage uri check");
            LOG.info(feedPage.getBaseURI());
            
            final HtmlPage jobsPage = HtmlUnitHelper.getAjaxPage(webClient, JOBS_URL);
            LOG.info(jobsPage.getBaseURI());

            LOG.info("#9 - get forms jobsPage");
            HtmlForm formsjobsPage = jobsPage.getForms().get(0);
            LOG.info(formsjobsPage.asXml());

//            List<Object> byXPath = formsjobsPage.getByXPath("div[@id='nav-typeahead-placeholder']");
            List<Object> byXPath = formsjobsPage.getByXPath("div/div/*");
            for (Object object : byXPath) {
                LOG.info("object \n" + object.toString());
            }

//            Iterable<DomElement> elementsByTagName = formsjobsPage.getChildElements();
//            for (DomElement domElement : elementsByTagName) {
//                LOG.info("childss...\n" + domElement.asXml());
//            }
            webClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    private HtmlPage addSubmitButtonAndSend(WebClient webClient, HtmlPage htmlPage, HtmlForm form) throws IOException {
        // create a submit button - it doesn't work with 'input'
        HtmlElement button = (HtmlElement) htmlPage.createElement("button");
        button.setAttribute("type", "submit");
        // append the button to the form
        form.appendChild(button);
        // submit the form
        HtmlPage newPage = button.click();

        LOG.info("### - waiting");
        webClient.waitForBackgroundJavaScript(HtmlUnitHelper.TIMEOUT_MILLIS);

        return newPage;
    }

    private void showAlerts(final HtmlPage basePage) {
        // w przegladarce da się zalogować nie potwierdzając tego info
        // w sumie można to chyba pominąć
        DomElement allertQueue = basePage.getElementById("global-alert-queue");
        LOG.info("##  allertQueue ");
        LOG.info(allertQueue.getTextContent());
        LOG.info("## allertQueue end");
    }
}
