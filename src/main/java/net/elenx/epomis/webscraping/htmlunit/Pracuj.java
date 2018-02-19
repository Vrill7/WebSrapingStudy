package net.elenx.epomis.webscraping.htmlunit;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

class Pracuj {

    private static final Logger LOG = Logger.getLogger(Pracuj.class.getName());

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

            LOG.info("#1 - base url");
            final HtmlPage basePage = webClient.getPage(BASE_URL);
            LOG.info(basePage.getBaseURI());
            HtmlUnitHelper.printCookies(webClient);

            LOG.info("#3 - get forms");
            HtmlForm serachForm = basePage.getForms().get(0);
            LOG.info(serachForm.toString());

            LOG.info("#4 - get form input");
            HtmlInput phrase = serachForm.getInputByName(searchPhraseInput);
            HtmlInput place = serachForm.getInputByName(searchPlaceInput);
            phrase.setValueAttribute("java");
            place.setValueAttribute("łódź");

            LOG.info("#5 - send form");

            HtmlButton submitButtonById = HtmlUnitHelper.getSubmitButtonById(serachForm, "searchBtn");
            if (submitButtonById != null) {
                HtmlPage feedPage = submitButtonById.click();
                LOG.info("### - waiting");
                webClient.waitForBackgroundJavaScript(HtmlUnitHelper.TIMEOUT_MILLIS);

//                LOG.info("#7 - feedPage as text");
//                LOG.info(feedPage.getBody().asText());
//                LOG.info("#7 - feedPage as xml");
//                LOG.info(feedPage.getBody().asXml());

                DomNode querySelector = feedPage.querySelector("ul.o-list");
//            DomNode querySelector = feedPage.querySelector("ul#mainOfferList");
                LOG.info("#8 - ul.o-list ul as xml");
                LOG.info(querySelector.asXml());
            }
            webClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
