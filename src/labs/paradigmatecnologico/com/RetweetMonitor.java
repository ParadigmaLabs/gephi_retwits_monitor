/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package labs.paradigmatecnologico.com;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Random;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.data.attributes.type.DynamicDouble;
import org.gephi.data.attributes.type.DynamicInteger;
import org.gephi.data.attributes.type.Interval;
import org.gephi.datalab.api.AttributeColumnsMergeStrategiesController;
import org.gephi.dynamic.api.DynamicController;
import org.gephi.dynamic.api.DynamicGraph;
import org.gephi.dynamic.api.DynamicModel;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.InDegreeRangeBuilder;
import org.gephi.graph.api.*;
import org.gephi.io.generator.spi.Generator;
import org.gephi.io.importer.api.*;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.ServiceProvider;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//labs.paradigmatecnologico.com//TitterWorkbench//EN",
autostore = false)
@TopComponent.Description(preferredID = "TitterWorkbenchTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "labs.paradigmatecnologico.com.TitterWorkbenchTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_TitterWorkbenchAction",
preferredID = "TitterWorkbenchTopComponent")
@Messages({
    "CTL_TitterWorkbenchAction=TitterWorkbench",
    "CTL_TitterWorkbenchTopComponent=TitterWorkbench Window",
    "HINT_TitterWorkbenchTopComponent=This is a TitterWorkbench window"
})


/**
 *
 * @author Roberto Maestre  rmaestre@paradigmatecnologico.com
 * @author Ruben Abad rabad@paradigmatecnologico.com
 * 
 * @author Paradigma labs 2012
 */
public final class RetweetMonitor extends TopComponent {

    
    private static final ProjectController pc;
    private static final Workspace workspace;
    private static  GraphModel graphModel;
    private static final DynamicGraph dynamicGraph;
    private static final Graph graph;
    
    private static final Container container;
    private static final ImportController importController;
    private static final AttributeModel attributeModel;
    private static final AttributeColumn dateColumn;
    
    private static final AttributeColumnsMergeStrategiesController dataLabController;
    private static final DynamicModel dynamicModel;
    
    private static CommandMonitor command_monitor = new CommandMonitor();

    private static Thread thread_filter_below = null;
    private static Thread thread_filter_old = null;
    
    private int cont_tweets_processed = 0;
    
    private boolean button = true;
    TwitterStream twitterStream = null;
    
    static {                
        // Vars for acces to graph and visualization
        pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        workspace = pc.getCurrentWorkspace();
        graphModel = Lookup.getDefault().lookup(GraphController.class).getModel(); 
        graph = graphModel.getGraph();
        
        container = Lookup.getDefault().lookup(ContainerFactory.class).newContainer();
        
        importController = Lookup.getDefault().lookup(ImportController.class);
        importController.process(container, new DefaultProcessor(), workspace);
    
        attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
        dateColumn = attributeModel.getNodeTable().addColumn("date", AttributeType.INT);
        
        dataLabController = Lookup.getDefault().lookup(AttributeColumnsMergeStrategiesController.class);
        dataLabController.mergeNumericColumnsToTimeInterval(attributeModel.getNodeTable(), dateColumn, null, 1990, 2010);
 
        //Use the DynamicModel dynamic graph factory
        dynamicModel = Lookup.getDefault().lookup(DynamicController.class).getModel();
        dynamicGraph = dynamicModel.createDynamicGraph(graph);
    }
      
      
    public RetweetMonitor() {
        initComponents();
        setName(Bundle.CTL_TitterWorkbenchTopComponent());
        setToolTipText(Bundle.HINT_TitterWorkbenchTopComponent());
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
                
        // Some params of UI init
        threshold.setText("1");
        threshold.setVisible(false);
        threshold_label.setVisible(false);
        seconds.setVisible(false);
        seconds_label.setVisible(false);
        
        remove_threshold.setEnabled(false);
        remove_threshold.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        button_capture = new javax.swing.JButton();
        tweets_number = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        username_label = new javax.swing.JLabel();
        password_label = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        keyword = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        remove_threshold = new javax.swing.JCheckBox();
        threshold = new javax.swing.JTextField();
        threshold_label = new javax.swing.JLabel();
        seconds_label = new javax.swing.JLabel();
        seconds = new javax.swing.JTextField();
        remove_old = new javax.swing.JCheckBox();
        clean = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(button_capture, org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.button_capture.text")); // NOI18N
        button_capture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_captureMouseClicked(evt);
            }
        });
        button_capture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_captureActionPerformed(evt);
            }
        });

        tweets_number.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(tweets_number, org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.tweets_number.text")); // NOI18N

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.jLabel1.text")); // NOI18N

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(username_label, org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.username_label.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(password_label, org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.password_label.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.jLabel5.text")); // NOI18N

        username.setText(org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.username.text")); // NOI18N
        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });

        keyword.setFont(new java.awt.Font("Lucida Grande", 2, 13)); // NOI18N
        keyword.setText(org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.keyword.text")); // NOI18N

        password.setText(org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.password.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(remove_threshold, org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.remove_threshold.text")); // NOI18N
        remove_threshold.setActionCommand(org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.remove_threshold.actionCommand")); // NOI18N
        remove_threshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remove_thresholdActionPerformed(evt);
            }
        });

        threshold.setText(org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.threshold.text")); // NOI18N

        threshold_label.setFont(new java.awt.Font("Lucida Grande", 2, 13)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(threshold_label, org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.threshold_label.text")); // NOI18N

        seconds_label.setFont(new java.awt.Font("Lucida Grande", 2, 13)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(seconds_label, org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.seconds_label.text")); // NOI18N

        seconds.setText(org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.seconds.text")); // NOI18N

        remove_old.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(remove_old, org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.remove_old.text")); // NOI18N
        remove_old.setActionCommand(org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.remove_old.actionCommand")); // NOI18N
        remove_old.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remove_oldActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(clean, org.openide.util.NbBundle.getMessage(RetweetMonitor.class, "RetweetMonitor.clean.text")); // NOI18N
        clean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(password_label)
                                    .addComponent(username_label)
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(keyword, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(button_capture, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(8, 8, 8)
                                .addComponent(tweets_number))
                            .addComponent(remove_old)
                            .addComponent(remove_threshold))
                        .addContainerGap(40, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(threshold_label)
                            .addComponent(seconds_label))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(seconds, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(threshold, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(clean, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(username_label)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(password_label)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(keyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(button_capture)
                .addGap(1, 1, 1)
                .addComponent(clean)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tweets_number))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(remove_old)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(remove_threshold)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(threshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(threshold_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(seconds_label)
                    .addComponent(seconds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void button_captureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_captureActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button_captureActionPerformed

    
    
    
    
    private void button_captureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_captureMouseClicked
        // TODO add your handling code here:
        //Set some UI vars
        username_label.setForeground(new Color(0, 0, 0));
        password_label.setForeground(new Color(0, 0, 0));
        threshold_label.setForeground(new Color(0, 0, 0));
        seconds_label.setForeground(new Color(0, 0, 0));
       
        // Check if UI is valid
        if (username.getText().equals("") || 
                new String(password.getPassword()).equals("") ||
                (threshold.getText().equals("") && remove_threshold.isSelected()) ||
                (seconds.getText().equals("") && remove_threshold.isSelected()) ){
            
            if (threshold.getText().equals("") && remove_threshold.isSelected())
                threshold_label.setForeground(new Color(255, 0, 0));
            if (seconds.getText().equals("") && remove_threshold.isSelected())
                seconds_label.setForeground(new Color(255, 0, 0));
                
            if (username.getText().equals(""))
                username_label.setForeground(new Color(255, 0, 0));
            if (new String(password.getPassword()).equals(""))
                password_label.setForeground(new Color(255, 0, 0));
        } else {
        
        // If puss button
        if (this.button){     
            
            // Disable UI tools
            clean.setEnabled(false);
            threshold.setEnabled(false);
            threshold.setEditable(false);
            seconds.setEnabled(false);
            seconds.setEditable(false);
            remove_old.setEnabled(false);
            remove_threshold.setEnabled(false);
            this.button = false;
            this.button_capture.setText("Pause capture");
            
            // BEGIN DISABLED IN THIS VERSION
            // If remove nodes below threshold, create thread
            if (remove_threshold.isSelected()) {
                remove_threshold.setEnabled(false);
                FilterNodesBelowThreshold filter_nodes = new FilterNodesBelowThreshold(graphModel, 
                                                                    Integer.parseInt(threshold.getText()),
                                                                    Integer.parseInt(seconds.getText()));
                thread_filter_below = new Thread(filter_nodes);
                thread_filter_below.start();
            }
            // END DISABLED IN THIS VERSION
            
            // If remove old filter is enabled, create thread
            if (remove_old.isSelected()) {
                remove_threshold.setEnabled(false);
                FilterEdgesOld thread_filter_old = new FilterEdgesOld(graphModel, command_monitor);
                thread_filter_below = new Thread(thread_filter_old);
                thread_filter_below.start();
            }
            
          
            // ---------------------------------------------------------------
            // BEGIN TWITTER4J streaming
            
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true);
            cb.setUseSSL(true);
            cb.setUser(username.getText());
            cb.setPassword(password.getText());
            twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
            
           StatusListener listener = new StatusListener(){
            public void onStatus(Status status) {
                if (status.isRetweet()){
                    
                    String from = status.getUser().getScreenName();
                    String to = status.getRetweetedStatus().getUser().getScreenName();
                    
                    Node n_from = graphModel.getGraph().getNode(from);
                    if (graphModel.getGraph().getNode(from) == null){
                        n_from = graphModel.factory().newNode(from);
                        n_from.getNodeData().setLabel(from);
                        /*
                        Random random = new Random();
                        Integer randomDataValue = new Integer(random.nextInt(21) + 1990);
                        n_from.getAttributes().setValue(dateColumn.getIndex(), randomDataValue);
                        */
                        graphModel.getDirectedGraph().addNode(n_from);
                    }

                    Node n_to = graphModel.getGraph().getNode(to);
                    if (graphModel.getGraph().getNode(to) == null){
                        n_to = graphModel.factory().newNode(to);
                        n_to.getNodeData().setLabel(to);
                        
                        Random random = new Random();
                               
                 Integer randomDataValue = new Integer(random.nextInt(21) + 1990);
                        n_to.getAttributes().setValue(dateColumn.getIndex(), randomDataValue);
                        
                        graphModel.getDirectedGraph().writeLock();
                        graphModel.getDirectedGraph().addNode(n_to);
                        graphModel.getDirectedGraph().writeUnlock();
                        
                    }
                    
                    if (remove_old.isSelected())
                        command_monitor.addCommand(String.format("%s@%s", n_from, n_to));
                    
                    graphModel.getDirectedGraph().writeLock();
                    Edge e0 = graphModel.factory().newEdge(n_from, n_to);
                    try {
                        graphModel.getDirectedGraph().addEdge(e0);
                    } catch (Exception e) {
                        
                    }
                    graphModel.getDirectedGraph().writeUnlock();
            
                    tweets_number.setText(Integer.toString(cont_tweets_processed));
                    cont_tweets_processed ++;    
                }
            }
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
            public void onScrubGeo(long l, long l1) {}
            public void onException(Exception ex) {
                ex.printStackTrace();
            }   
            };
            
            
            twitterStream.addListener(listener);
            
            // Perform filter if filter param is provided
            if (!keyword.getText().equals("")) {
                String[] params = new String[1];
                params[0] = keyword.getText();
                FilterQuery fq = new FilterQuery();
                fq.track(params);
                twitterStream.filter(fq);
            // Listen all stream if filter param is not provided
            } else {
                twitterStream.sample();
            }

            
            // ---------------------------------------------------------------
            // END TWITTER4J streaming
            


    
        } else {
            // Kill streaming
            twitterStream.shutdown();
            // Kill thread filters
            if (thread_filter_below != null){
                thread_filter_below.interrupt();
                thread_filter_below = null;
            }
            if (thread_filter_old != null){
                thread_filter_old.interrupt();
                thread_filter_old = null;
            }
            
            
            // Re-enabled UI components
            threshold.setEnabled(true);
            threshold.setEditable(true);
            seconds.setEnabled(true);
            seconds.setEditable(true);
            clean.setEnabled(true);
            remove_old.setEnabled(true);

            remove_threshold.setEnabled(false);
            remove_threshold.setVisible(false);
            this.button = true;
            this.button_capture.setText("Continue capture");
            
            /*
            DynamicInteger numberofNodes = new DynamicInteger();
            for (int i = 1990; i < 2009; i++) {
                int low = i;
                int high = i + 1;
                Graph subGraph = dynamicGraph.getSnapshotGraph(low, high);
                int count = subGraph.getNodeCount();
                numberofNodes = new DynamicInteger(numberofNodes, new Interval<Integer>(low, high, count)); 
            }
            //Get all intervals and print values
            System.out.println("Number of nodes:");
            for (Interval<Integer> interval : numberofNodes.getIntervals(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)) {
                int low = (int) interval.getLow();
                int high = (int) interval.getHigh();
                System.out.println(low + "-" + high + "  ->  " + interval.getValue());
            }
            */


        }
    
        }
        
        
        
    
    }//GEN-LAST:event_button_captureMouseClicked

    private void usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameActionPerformed

    private void remove_thresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remove_thresholdActionPerformed
        // TODO add your handling code here:
        if (remove_threshold.isSelected()) {
            if (threshold.getText().equals("") || seconds.getText().equals("")){
                if (threshold.getText().equals(""))
                    threshold_label.setForeground(new Color(255, 0, 0));
                if (seconds.getText().equals(""))
                    seconds_label.setForeground(new Color(255, 0, 0));
            }
            
            threshold.setVisible(true);
            threshold_label.setVisible(true);
            seconds.setVisible(true);
            seconds_label.setVisible(true);
        } else {
                threshold.setVisible(false);
                threshold_label.setVisible(false);
                seconds.setVisible(false);
                seconds_label.setVisible(false);
        }
    }//GEN-LAST:event_remove_thresholdActionPerformed

    private void remove_oldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remove_oldActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_remove_oldActionPerformed

    private void cleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanActionPerformed
        // TODO add your handling code here:
        // Kill streaming
        twitterStream.shutdown();
        // Kill thread filters
        if (thread_filter_below != null){
            thread_filter_below.interrupt();
            thread_filter_below = null;
        }
        if (thread_filter_old != null){
            thread_filter_old.interrupt();
            thread_filter_old = null;
        }
        Node[] nodes = graphModel.getDirectedGraph().getNodes().toArray();
        for(int i=0; i<nodes.length; i++) {
                graphModel.getDirectedGraph().removeNode(nodes[i]);
        }
        Edge[] edges = graphModel.getDirectedGraph().getEdges().toArray();
        for(int i=0; i<edges.length; i++) {
                graphModel.getDirectedGraph().removeEdge(edges[i]);
        }
        cont_tweets_processed = 0;
        this.button_capture.setText("Start capture");
        
                
    }//GEN-LAST:event_cleanActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JButton button_capture;
    private javax.swing.JButton clean;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField keyword;
    private javax.swing.JPasswordField password;
    private javax.swing.JLabel password_label;
    private javax.swing.JCheckBox remove_old;
    private javax.swing.JCheckBox remove_threshold;
    private javax.swing.JTextField seconds;
    private javax.swing.JLabel seconds_label;
    private javax.swing.JTextField threshold;
    private javax.swing.JLabel threshold_label;
    private javax.swing.JLabel tweets_number;
    private javax.swing.JTextField username;
    private javax.swing.JLabel username_label;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
