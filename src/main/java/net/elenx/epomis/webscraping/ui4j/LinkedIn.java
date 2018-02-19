package net.elenx.epomis.webscraping.ui4j;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.dom.Document;
import io.webfolder.ui4j.api.dom.Element;

import java.util.Optional;

import static io.webfolder.ui4j.api.browser.BrowserFactory.getWebKit;
import java.util.logging.Logger;
import static net.elenx.epomis.webscraping.LinkedinStatics.*;

public class LinkedIn {

    private static final Logger LOG = Logger.getLogger(LinkedIn.class.getName());

    private static final int TIMEOUT_MILLIS = 5000;
    private static final int TIMEOUT_SEC = 5;

    public static void main(String[] args) {
        LinkedIn linkedIn = new LinkedIn();
    }

    public LinkedIn() {
        try (Page page = getWebKit().navigate(BASE_URL)) {
            page.show(true);
            Document document = page.getDocument();

            Optional<Element> login = document.query("#login-email");
            Optional<Element> password = document.query("#login-password");
            Optional<Element> form = document.query("form");
            login.get().setValue("login");
            password.get().setValue("password");
            wait1Second();
            form.get().getForm().get().submit();
            wait1Second();
            Optional<Element> jobsLink = document.query("[href=/jobs/]a");
            jobsLink.ifPresent(element -> element.click());

//            Page jobsPage = getWebKit().navigate("https://www.linkedin.com/jobs/");
//            wait1Second();
//            Optional<Element> query = jobsPage.getDocument().query("[role=combobox]input");
//            jobsPage.show();
        }
    }

    private void printAnchor(Page page) {
        page
                .getDocument()
                .queryAll("a")
                .forEach(e -> {
                    LOG.info(e.getText().get());
                });
    }

    private static void wait1Second() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
