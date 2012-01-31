/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package labs.paradigmatecnologico.com;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;


/**
 *
 * @author Roberto Maestre  rmaestre@paradigmatecnologico.com
 * @author Ruben Abad rabad@paradigmatecnologico.com
 * 
 * @author Paradigma labs 2012
 */
public class CommandMonitor {
    
    private static LinkedList<String> commands = new LinkedList<String>();  
    
    public synchronized void addCommand(String command){
        commands.add(command);
    }
    
    public synchronized void delCommand(String command){
        commands.remove(command);
    }
    

    
    public synchronized void remove(GraphModel graphModel){
        int n = graphModel.getDirectedGraph().getNodeCount();
        int max = (n * 40) / 100;
        int cont = 0;
        System.out.println("Removing "+max+" "+n);
        
        Iterator iter = commands.iterator();
        while(iter.hasNext() && cont < max){
            String command = (String) iter.next();
            String[] chunks = command.split("@");
            if (chunks.length == 2){
                Node n_to = graphModel.getDirectedGraph().getNode(chunks[0]);
                Node n_from = graphModel.getDirectedGraph().getNode(chunks[1]);
                    if (n_to != null && n_from != null) {
                        Edge e = graphModel.getDirectedGraph().getEdge(n_to, n_from);
                        if (e != null) {
                            graphModel.getDirectedGraph().removeEdge(e);
                        } else {
                        }
                    }
             } else {
                //System.out.println("ERROR");
             }
        cont++;
       }
        for (int i=0;i<max && i < commands.size();i++)
            commands.remove(i);
}
    
}
