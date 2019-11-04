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
import java.util.logging.Level;
import java.util.logging.Logger;
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
            //visit1500DistinctLinks(url);
            //visit3000NonDistinctLinks(url);
        } catch (Exception ex) {
            Logger.getLogger(LinkFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getSimpleLinks(String url) {
        try {
            // 1. if url not already visited, visit url with linkHandler
            if (!linkHandler.visited(url)) {
                System.out.println("linkFinderUrl -> " + url); // just for testing

                // ToDo: Implement
                Document doc = Jsoup.connect(url).timeout(1000).get();
                Elements links = doc.select("a[href]");

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
            if (linkHandler.size() == 500) {
                System.err.println(System.nanoTime() - t0 + "ns");
                System.exit(0);
            }
        } catch (Exception ex) {

        }
    }

    private void visit1500DistinctLinks(String url) {
        try {
            if (!linkHandler.visited(url)) {
                System.out.println(url);
                Document doc = Jsoup.connect(url).timeout(1000).get();
                Elements links = doc.select("a[href]");

                for (Element link : links) {
                    linkHandler.queueLink(link.attr("abs:href"));
                }
            }

            linkHandler.addVisited(url);

            if (linkHandler.size() == 1500) {
                System.err.println("Time needed to visit 1500 distinct links: " + (System.nanoTime() - t0) + "ns" + " | " + (System.nanoTime() - t0) / 1000000 + "ms");
                System.exit(0);
            }
        } catch (Exception ex) {
        }
    }

    private void visit3000NonDistinctLinks(String url) {
        try {
            // 1. if url not already visited, visit url with linkHandler
            //if (!linkHandler.visited(url)) {
            //System.out.println("linkFinderUrl -> " + url); // just for testing

            // ToDo: Implement
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                // 2. get url and Parse Website        
                // 3. extract all URLs and add url to list of urls which should be visited
                //System.out.println("foundLink -> " + link.attr("abs:href"));

                linkHandler.queueLink(link.attr("abs:href"));
            }
            //}

            // nachdem alle Links gefunden wurden URL als besucht markieren
            linkHandler.addVisited(url);
            //    only if link is not empty and url has not been visited before
            // 4. If size of link handler equals 500 -> print time elapsed for statistics
            if (linkHandler.size() == 3000) {
                System.err.println("Time need to visit 3000 non-distinct links: " + (System.nanoTime() - t0) + "ns");
                System.exit(0);
            }
            System.out.println(linkHandler.size());
        } catch (Exception ex) {

        }
    }
}
