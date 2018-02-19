package net.elenx.epomis.webscraping.jsoup;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.elenx.epomis.webscraping.UdemyStatics.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class Udemy {

    Logger LOG = Logger.getLogger(Udemy.class.getName());

    public static void main(String[] args) {
        Udemy udemyJSoup = new Udemy();
        udemyJSoup.listJavaCourses();
    }

    public Udemy() {
    }

    public void listJavaCourses() {
        try {
            Document javaCoursesPage = Jsoup.connect(SEARCH_JAVA_URL).get();
            LOG.info(javaCoursesPage.title());
            LOG.info(javaCoursesPage.html());

            Elements courseCards = javaCoursesPage.select("div[data-purpose=search-course-cards]");
            LOG.log(Level.INFO, "search-course-cards find:{0}", courseCards.size());

            for (Element courseCard : courseCards) {
                Elements courseLink = courseCard.select("a[data-purpose=search-course-card-title]");
                LOG.log(Level.INFO, "courseLink:{0}", courseLink.get(0).absUrl("href"));
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

}
