/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eaustria.webcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author bmayr
 */
// Recursive Action for forkJoinFramework from Java7
public class LinkFinderAction extends RecursiveAction {

    private String url;
    private ILinkHandler cr;
    /**
     * Used for statistics
     */
    private static final long t0 = System.nanoTime();

    public LinkFinderAction(String url, ILinkHandler cr) {
        this.url = url;
        this.cr = cr;
    }

    /*
    @Override
    public void compute() {
        // ToDo:
        // 1. if crawler has not visited url yet:
        if (!cr.visited(url)) {
            System.out.println("linkFinderAction -> " + url);
            try {
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("a[href]");

                // 2. Create new list of recursiveActions
                List<RecursiveAction> taskList = new ArrayList<>();
                // 3. Parse url                            
                // 4. extract all links from url                
                // 5. add new Action for each sublink
                for (Element link : links) {
                    taskList.add(new LinkFinderAction(link.attr("abs:href"), cr));
                }

                // nachdem alle Links gefunden wurden URL als besucht markieren                
                cr.addVisited(url);

                // 6. if size of crawler exceeds 500 -> print elapsed time for statistics
                // -> Do not forget to call ìnvokeAll on the actions!
                if (cr.size() == 500) {
                    System.err.println("Time until crawler exceeds size 500 " + (System.nanoTime() - t0) + "ns");
                    System.exit(0);
                }
                invokeAll(taskList);
            } catch (Exception ex) {
                //Logger.getLogger(LinkFinderAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void compute() { // 1500 distinct links
        if (!cr.visited(url)) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("a[href]");

                // 2. Create new list of recursiveActions
                List<RecursiveAction> taskList = new ArrayList<>();
                // 3. Parse url                            
                // 4. extract all links from url                
                // 5. add new Action for each sublink
                for (Element link : links) {
                    taskList.add(new LinkFinderAction(link.attr("abs:href"), cr));
                }

                // nachdem alle Links gefunden wurden URL als besucht markieren                
                cr.addVisited(url);

                // 6. if size of crawler exceeds 500 -> print elapsed time for statistics
                // -> Do not forget to call ìnvokeAll on the actions!
                if (cr.size() == 1500) {
                    System.err.println("Time until crawler exceeds size 1500 " + (System.nanoTime() - t0) + "ns" + " | " + (System.nanoTime() - t0) / 1000000 + "ms");
                    System.exit(0);
                }
                invokeAll(taskList);
            } catch (Exception ex) {
                //Logger.getLogger(LinkFinderAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    */
    @Override
    public void compute() { // 3000 non-distinct links
        //if (!cr.visited(url)) {
            try {
                System.out.println(url);
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("a[href]");

                // 2. Create new list of recursiveActions
                List<RecursiveAction> taskList = new ArrayList<>();
                // 3. Parse url                            
                // 4. extract all links from url                
                // 5. add new Action for each sublink
                for (Element link : links) {
                    taskList.add(new LinkFinderAction(link.attr("abs:href"), cr));
                }

                // nachdem alle Links gefunden wurden URL als besucht markieren                
                cr.addVisited(url);

                // 6. if size of crawler exceeds 500 -> print elapsed time for statistics
                // -> Do not forget to call ìnvokeAll on the actions!
                if (cr.size() == 3000) {
                    System.err.println("Time until crawler exceeds size 3000 " + (System.nanoTime() - t0) + "ns" + " | " + (System.nanoTime() - t0) / 1000000 + "ms");
                    System.exit(0);
                }
                invokeAll(taskList);
            } catch (Exception ex) {
                //Logger.getLogger(LinkFinderAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        //}
    }
}
