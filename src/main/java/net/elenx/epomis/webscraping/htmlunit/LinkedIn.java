package net.elenx.epomis.webscraping.htmlunit;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.List;

import static net.elenx.epomis.webscraping.LinkedinStatics.*;

class LinkedIn {

    public static void main(String[] args) {
        LinkedIn linkedIn = new LinkedIn();
    }

    public LinkedIn() {
        testHtmlUnitStrict(WebClientConf.getConfiguredWebClient());
    }

    private void testHtmlUnitStrict(WebClient webClient) {
        try {
            System.out.println("#1 - base url");
            final HtmlPage basePage = webClient.getPage(BASE_URL);
            System.out.println(basePage.getBaseURI());
            HtmlUnitHelper.printCookies(webClient);

            System.out.println("#2 - alerty ze strony");
            showAlerts(basePage);

            System.out.println("#3 - get forms");
            HtmlForm loginForm = basePage.getForms().get(0);
            System.out.println(loginForm.toString());

            System.out.println("#4 - get form input");
            HtmlInput session_key = loginForm.getInputByName(INPUT_LOGIN_NAME);
            HtmlInput session_password = loginForm.getInputByName(INPUT_PASSWORD_NAME);
            session_key.setValueAttribute("login@login.pl");
            session_password.setValueAttribute("password");

            System.out.println("#5 - send form");
            HtmlPage feedPage = addSubmitButtonAndSend(webClient, basePage, loginForm);

            System.out.println("#7 - feedPage as text");
            System.out.println(feedPage.getBody().asText());
//            System.out.println("#7 - coursesPage as xml");
//            System.out.println(coursesPage.getBody().asXml());

            System.out.println("#8 - feedPage uri check");
            System.out.println(feedPage.getBaseURI());

            final HtmlPage jobsPage = HtmlUnitHelper.getAjaxPage(webClient, JOBS_URL);
            System.out.println(jobsPage.getBaseURI());

            System.out.println("#3 - get forms jobsPage");
            HtmlForm formsjobsPage = jobsPage.getForms().get(0);
            System.out.println(formsjobsPage.asXml());

//            List<Object> byXPath = formsjobsPage.getByXPath("div[@id='nav-typeahead-placeholder']");
            List<Object> byXPath = formsjobsPage.getByXPath("div/div/*");
            for (Object object : byXPath) {
                System.out.println("object \n" + object.toString());
            }

//            Iterable<DomElement> elementsByTagName = formsjobsPage.getChildElements();
//            for (DomElement domElement : elementsByTagName) {
//                System.out.println("childss...\n" + domElement.asXml());
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

        System.out.println("### - waiting");
        webClient.waitForBackgroundJavaScript(HtmlUnitHelper.TIMEOUT_MILLIS);

        return newPage;
    }

    private void showAlerts(final HtmlPage basePage) {
        // w przegladarce da się zalogować nie potwierdzając tego info
        // w sumie można to chyba pominąć
        DomElement allertQueue = basePage.getElementById("global-alert-queue");
        System.out.println("##  allertQueue ");
        System.out.println(allertQueue.getTextContent());
        System.out.println("## allertQueue end");
    }
}
