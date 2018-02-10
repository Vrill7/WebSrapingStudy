package net.elenx.epomis.webscraping.htmlunit;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.List;

class Pracuj {

    public static void main(String[] args) {
        Pracuj praca = new Pracuj();
    }

    public Pracuj() {
        testHtmlUnitStrict(WebClientConf.getConfiguredWebClient());
    }

    private void testHtmlUnitStrict(WebClient webClient) {
        try {
            String BASE_URL = "https://www.pracuj.pl/";
            String searchPhraseInput = "kw";
            String searchPlaceInput = "wp";

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

            HtmlButton submitButtonById = HtmlUnitHelper.getSubmitButtonById(serachForm, "searchBtn");
            if (submitButtonById != null) {
                HtmlPage feedPage = submitButtonById.click();
                System.out.println("### - waiting");
                webClient.waitForBackgroundJavaScript(HtmlUnitHelper.TIMEOUT_MILLIS);

//                System.out.println("#7 - feedPage as text");
//                System.out.println(feedPage.getBody().asText());
//                System.out.println("#7 - feedPage as xml");
//                System.out.println(feedPage.getBody().asXml());

                DomNode querySelector = feedPage.querySelector("ul.o-list");
//            DomNode querySelector = feedPage.querySelector("ul#mainOfferList");
                System.out.println("#8 - ul.o-list ul as xml");
                System.out.println(querySelector.asXml());
            }
            webClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
