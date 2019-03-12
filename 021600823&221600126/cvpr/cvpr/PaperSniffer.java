package cvpr;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * PaperSniffer - Collect paper information from given website.
 *
 * @author Liu Zhongyu
 */
public class PaperSniffer {
    private List<String> paperUrls = null;
    private List<PaperContent> paperContents = null;
    private static final Object LOCK = new Object();    // multithread lock

    /**
     * Return a list of paper URLs.
     */
    public List<String> getPaperUrls() {
        if(paperUrls != null)
            return paperUrls;

        paperUrls = new LinkedList<>();
        try {
            Document document = Jsoup.connect(SnifferConfig.START_URL).get();
            Elements linkElements = document.select(SnifferConfig.PAPER_URL_QUERY);

            for(Element link : linkElements)
                paperUrls.add(link.attr("href"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paperUrls;
    }

    /**
     * Return a list of PaperContent.
     * Each PaperContent object contains the titles and abstract of a paper.
     */
    public List<PaperContent> getPaperContents() {
        if(paperContents != null)
            return paperContents;

        paperUrls = getPaperUrls();
        paperContents = new LinkedList<>();

        for(String paperUrl : paperUrls) {
            // visit every paper's web page to grab its title and abstract
            // create a thread for each paperUrl
            Runnable thread = new Runnable() {
                @Override
                public void run() {
                    try {
                        Document document = Jsoup.connect(SnifferConfig.URL_BASE + paperUrl).get();
                        Element titleNode = document.select(SnifferConfig.PAPER_TITLE_QUERY).first();
                        Element abstractNode = document.select(SnifferConfig.PAPER_ABSTRACT_QUERY).first();
                        PaperContent paper = new PaperContent(titleNode.text(), abstractNode.text());
                        synchronized (LOCK) {
                            paperContents.add(paper);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.run();
        }

        return paperContents;
    }
}
