/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eaustria.webcrawler;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

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

    @Override
    public void compute() {
        // ToDo:
        // 1. if crawler has not visited url yet:
        if (!cr.visited(url)) {
            try {
                // 2. Create new list of recursiveActions
                List<RecursiveAction> taskList = new ArrayList<>();
                // 3. Parse url
                Parser parser = new Parser(url);
                // 4. extract all links from url                
                NodeList linksNodes = parser.extractAllNodesThatMatch((Node node) -> {
                    return (((LinkTag)node).isHTTPLikeLink());
                });
                // 5. add new Action for each sublink
                for (Node currentLinkNode : linksNodes.toNodeArray()) {
                    taskList.add(new LinkFinderAction(currentLinkNode.getText(), cr));
                }
                // 6. if size of crawler exceeds 500 -> print elapsed time for statistics
                // -> Do not forget to call ìnvokeAll on the actions!
                invokeAll(taskList);
            } catch (ParserException ex) {
                System.err.println("Parser exception!");
            } catch (ClassCastException ex) {
                System.err.println("Class Cast exception");
            }            
        }        
    }
}



