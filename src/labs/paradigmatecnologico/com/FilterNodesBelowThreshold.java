/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package labs.paradigmatecnologico.com;

import java.util.Iterator;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
import org.gephi.filters.plugin.graph.InDegreeRangeBuilder.InDegreeRangeFilter;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Roberto Maestre  rmaestre@paradigmatecnologico.com
 * @author Ruben Abad rabad@paradigmatecnologico.com
 * 
 * @author Paradigma labs 2012
 */
public class FilterNodesBelowThreshold implements Runnable  {

    private static GraphModel graphModel;
    private static int threshold;
    private static int seconds;
    
    public FilterNodesBelowThreshold(GraphModel graphModel, int threshold, int seconds){
        this.graphModel = graphModel;
        this.threshold = threshold;
        this.seconds = seconds * 1000;
    }
    
    @Override
    public void run() {
        try {
            while (true){
                Thread.sleep(seconds);
                System.out.println("Removing nodes below threshold");
                
                graphModel.getDirectedGraph().writeLock();
                Node[] nodes = graphModel.getDirectedGraph().getNodes().toArray();
                for(int i=0; i<nodes.length; i++) {
                    int indegree = graphModel.getDirectedGraph().getInDegree(nodes[i]);
                    if (indegree <= this.threshold){
                        graphModel.getDirectedGraph().removeNode(nodes[i]);
                    }
                }
                graphModel.getDirectedGraph().writeUnlock();
            }
        } catch (InterruptedException ex) {
           Thread.currentThread().interrupt();
        }
    }
    
}
