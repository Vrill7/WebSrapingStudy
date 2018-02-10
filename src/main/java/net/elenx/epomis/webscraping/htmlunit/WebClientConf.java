package net.elenx.epomis.webscraping.htmlunit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 *
 * @author Piotr Karasiński <piotrkarasinski@dolsat.pl>
 */
class WebClientConf {

    static WebClient getConfiguredWebClient() {
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setTimeout(2000);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setScreenWidth(1920);
        webClient.getOptions().setScreenHeight(1080);
        webClient.getOptions().setGeolocationEnabled(true);

        // overcome problems in JavaScript
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        webClient.getOptions().setCssEnabled(false);
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        return webClient;
    }
}
