package net.elenx.epomis.webscraping.htmlunit;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.List;

class Praca {

    public static void main(String[] args) {
        Praca praca = new Praca();
    }

    public Praca() {
        testHtmlUnitStrict(WebClientConf.getConfiguredWebClient());
    }

    private void testHtmlUnitStrict(WebClient webClient) {
        try {
            String BASE_URL = "https://www.praca.pl/";
            String searchPhraseInput = "data[Search][phrase]";
            String searchPlaceInput = "data[Search][place]";

            System.out.println("#1 - base url");
            final HtmlPage basePage = webClient.getPage(BASE_URL);
            System.out.println(basePage.getBaseURI());
            HtmlUnitHelper.printCookies(webClient);

            System.out.println("#3 - get forms");
            HtmlForm serachForm = basePage.getForms().get(0);
            System.out.println(serachForm.toString());

            System.out.println("#4 - get form input");
            HtmlInput phrase = serachForm.getInputByName(searchPhraseInput);
            HtmlInput place = serachForm.getInputByName(searchPlaceInput);
            phrase.setValueAttribute("java");
            place.setValueAttribute("łódź");

            System.out.println("#5 - send form");
            
            HtmlPage feedPage = serachForm.click();
            System.out.println("### - waiting");
            webClient.waitForBackgroundJavaScript(HtmlUnitHelper.TIMEOUT_MILLIS);

            System.out.println("#7 - feedPage as text");
//            System.out.println(feedPage.getBody().asText());
//            System.out.println("#7 - coursesPage as xml");
//            System.out.println(feedPage.getBody().asXml());

            DomNode querySelector = feedPage.querySelector("ul.listing-announcement_new");
            System.out.println("#7 - coursesPage ul as xml");
            System.out.println(querySelector.asXml());
            webClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
