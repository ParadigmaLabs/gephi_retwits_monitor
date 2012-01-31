/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package labs.paradigmatecnologico.com;

import java.util.*;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
import org.gephi.filters.plugin.graph.InDegreeRangeBuilder.InDegreeRangeFilter;
import org.gephi.graph.api.Edge;
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
public class FilterEdgesOld implements Runnable  {

    private static GraphModel graphModel;
    private static CommandMonitor command_monitor;
    
    public FilterEdgesOld(GraphModel graphModel, CommandMonitor command_monitor){
        this.graphModel = graphModel;
        this.command_monitor = command_monitor;
    }
        
    @Override
    public void run() {
        try {
            while (true){
                Thread.sleep(10000);
                
                //Remove edges
                command_monitor.remove(graphModel);

                // Remove nodes with 0 indegree and 0 outdegree
                Node[] nodes = graphModel.getDirectedGraph().getNodes().toArray();
                for(int i=0; i<nodes.length; i++) {
                    int indegree = graphModel.getDirectedGraph().getInDegree(nodes[i]);
                    int outdegree = graphModel.getDirectedGraph().getOutDegree(nodes[i]);
                    if (indegree==0 && outdegree==0 || indegree==1){
                        graphModel.getDirectedGraph().removeNode(nodes[i]);
                    }
                }
      
            }
        } catch (InterruptedException ex) {
           Thread.currentThread().interrupt();
        }catch (Exception ex) {
           ex.printStackTrace();
        }

    }
    
}
