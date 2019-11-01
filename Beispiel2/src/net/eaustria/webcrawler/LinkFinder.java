/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eaustria.webcrawler;

/**
 *
 * @author bmayr
 */
import java.net.URL;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import java.util.ArrayList;
import java.util.List;
import static java.util.concurrent.ForkJoinTask.invokeAll;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.htmlparser.Node;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkFinder implements Runnable {

    private String url;
    private ILinkHandler linkHandler;
    /**
     * Used for statistics
     */
    private static final long t0 = System.nanoTime();

    public LinkFinder(String url, ILinkHandler handler) {
        //ToDo: Implement Constructor
        this.url = url;
        this.linkHandler = handler;
    }

    @Override
    public void run() {
        try {
            getSimpleLinks(url);
        } catch (Exception ex) {
            Logger.getLogger(LinkFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getSimpleLinks(String url) throws Exception {        
        // ToDo: Implement
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");

        // 1. if url not already visited, visit url with linkHandler
        if (!linkHandler.visited(url)) {
            System.out.println("linkFinderUrl -> " + url);
            for (Element link : links) {
                // 2. get url and Parse Website        
                // 3. extract all URLs and add url to list of urls which should be visited
                //System.out.println("foundLink -> " + link.attr("abs:href"));
                linkHandler.queueLink(link.attr("abs:href"));
            }
        }
        
        // nachdem alle Links gefunden wurden URL als besucht markieren
        linkHandler.addVisited(url);
        //    only if link is not empty and url has not been visited before
        // 4. If size of link handler equals 500 -> print time elapsed for statistics
        if (linkHandler.size() == 100) {
            System.err.println(System.nanoTime() - t0 + "ns");
        }
    }
}
