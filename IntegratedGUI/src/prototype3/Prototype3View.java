/*
 * Prototype3View.java
 */

package prototype3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import lombardi.LombardiView;
import JungLayouts.SocialNetworkVisualizer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

/**
 * The application's main frame.
 */
public class Prototype3View extends FrameView {

    public Prototype3View(SingleFrameApplication app) {
        super(app);

        initComponents();
        
        getFrame().setTitle("VisualThoughts Visualization Tool"); 
        
        jSlider1.setMajorTickSpacing(3);
        jSlider2.setMaximum(400);
        jSlider2.setMinimum(5);
        jSlider2.setValue(20);
        jSlider2.setMajorTickSpacing(25);
        jSlider1.setMinorTickSpacing(1);
        jSlider2.setMinorTickSpacing(1);
        jSlider1.setPaintTicks(true);
        jSlider2.setPaintTicks(true);
        jSlider1.setPaintLabels(true);
        jSlider2.setPaintLabels(true);
        jSlider1.setVisible(true);
//        jSlider2.setVisible(false);
        jLabel3.setVisible(true);
        jLabel3.setText("Tweet Date Selection:");
       // jLabel4.setVisible(false);
//        jPanel5.setVisible(false);
//        jList1.setVisible(false);
       // jScrollPane1.setVisible(false);
        
        try{
            popularWords = test.getPopular();
            
            if(popularWords.length < 10){
                myRadio(popularWords);
            } else {
//                jRadioButton1.setText(popularWords[0]);
//                jRadioButton2.setText(popularWords[1]);
//                jRadioButton3.setText(popularWords[2]);
//                jRadioButton4.setText(popularWords[3]);
//                jRadioButton5.setText(popularWords[4]);
//                jRadioButton6.setText(popularWords[5]);
//                jRadioButton7.setText(popularWords[6]);
//                jRadioButton8.setText(popularWords[7]);
//                jRadioButton9.setText(popularWords[8]);
//                jRadioButton10.setText(popularWords[9]);
            }
            
//            jRadioButton1.addActionListener(new mySelection());
//            jRadioButton2.addActionListener(new mySelection());
            
        } catch (SQLException s){
            System.out.println("SQL Error: " + s.toString() + " " + s.getErrorCode() + " " + s.getSQLState());
        } catch (Exception e){
            System.out.println("Error: " + e.toString() + " " + e.getMessage());
        }

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
//                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
//        statusAnimationLabel.setIcon(idleIcon);
//        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
//                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
//                    progressBar.setVisible(true);
//                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
//                    statusAnimationLabel.setIcon(idleIcon);
//                    progressBar.setVisible(false);
//                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
//                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
//                    progressBar.setVisible(true);
//                    progressBar.setIndeterminate(false);
//                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = Prototype3App.getApplication().getMainFrame();
            aboutBox = new Prototype3AboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        Prototype3App.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jXMapKit1 = new org.jdesktop.swingx.JXMapKit();
        scrollPane1 = new java.awt.ScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jSlider1 = new javax.swing.JSlider(0,12,0);
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSlider2 = new javax.swing.JSlider();
        jScroll_network = new javax.swing.JScrollPane();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(1280, 750));

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setPreferredSize(new java.awt.Dimension(1280, 768));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        jXMapKit1.setDefaultProvider(org.jdesktop.swingx.JXMapKit.DefaultProviders.OpenStreetMaps);
        jXMapKit1.setMaximumSize(new java.awt.Dimension(850, 300));
        jXMapKit1.setName("jXMapKit1"); // NOI18N
        jXMapKit1.setPreferredSize(new java.awt.Dimension(850, 300));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jXMapKit1, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jXMapKit1, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
        );

        scrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        scrollPane1.setName("scrollPane1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(prototype3.Prototype3App.class).getContext().getResourceMap(Prototype3View.class);
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel3.border.titleFont"), resourceMap.getColor("jPanel3.border.titleColor"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setForeground(resourceMap.getColor("jLabel1.foreground")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jTextField1.setText(resourceMap.getString("jTextField1.text")); // NOI18N
        jTextField1.setName("jTextField1"); // NOI18N

        jSlider1.setEnabled(false);
        jSlider1.setName("jSlider1"); // NOI18N

        jLabel3.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel3.setForeground(resourceMap.getColor("jLabel3.foreground")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Lombardi", "ISOM Layout", "KK Layout", "FR Layout", "Circle Layout" }));
        jComboBox2.setName("jComboBox_layouts"); // NOI18N

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setForeground(resourceMap.getColor("jLabel2.foreground")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jSlider2.setName("jSlider2"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(127, 127, 127)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton1))))))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jScroll_network.setName("jScroll_network"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScroll_network)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jScroll_network, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1387, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 696, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(148, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(prototype3.Prototype3App.class).getContext().getActionMap(Prototype3View.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:              
    idx = new RAMDirectory();
       
//    jPanel2.removeAll();
    scrollPane1.removeAll();
   // ScrollPane1.setPreferredSize(new Dimension(2000,2000));
    
    myResults = null;
    points = null;
    myTweets = null;    
    myMonths = null;
    myHours = null;
    myDays = null;
        
    lat = new ArrayList<Double>();
    lon = new ArrayList<Double>();
    date = new ArrayList<String>();
    wordIn = new ArrayList<String>();
    wordIn1 = new ArrayList<String>();
    monthPoints = new ArrayList<Integer>();
    
    count1 = 0;
//    
//    if (jComboBox1.getSelectedIndex() == 1){
//                
////        jPanel2.setVisible(false);
//        jPanel1.setVisible(true);
//        scrollPane1.setVisible(true);
//                        
//    } else if (jComboBox1.getSelectedIndex() == 2){
//      
//        jPanel1.setVisible(false);        
//        scrollPane1.setVisible(false);
//    } else {
//      
//        jPanel1.setVisible(true);
//        scrollPane1.setVisible(true);
//    }
//    
    mySelection = getSel();
//    jcombo = jComboBox1.getSelectedIndex();
    myInput = jTextField1.getText();
    SocialNetworkVisualizer.searchKey = myInput;
    if (myInput.equals("") && mySelection == null){
        JOptionPane.showMessageDialog(jLabel1, "No search word was input");
//        jPanel2.setVisible(true);
//        jPanel1.setVisible(false);
//        scrollPane1.setVisible(false);
        
    } else {
        jSlider1.setVisible(true);
        jSlider2.setVisible(true);
        jLabel3.setVisible(true);
        //jLabel4.setVisible(true);
//        jPanel5.setVisible(true);
//        jList1.setVisible(true);
//        jScrollPane1.setVisible(true);
        mySelection = getSel();
        if (myInput.equals("")){
            wordIn = getWords1();
            mySearch = mySelection;
        } else if (mySelection == null){
            mySearch = searchKey(myInput);
            wordIn = getWords(myInput);
        } else {
            wordIn = getWords1();
            wordIn1 = getWords(myInput);
            for (int i=0; i<wordIn1.size(); i++){
                wordIn.add(wordIn1.get(i));
            }
            mySearch = searchKey(myInput) + " OR " + mySelection;
         }

        col = new Integer[wordIn.size()];
        for (int i=0; i<wordIn.size(); i++){
            int ranBlue = randomGenerator.nextInt(255);
            while (ranBlue < 50){
                ranBlue = randomGenerator.nextInt(255);
            }
            col[i] = ranBlue;
        }
        try{
           
            
            
            for (int i=0; i<wordIn.size(); i++){
                test.setPopular(wordIn.get(i));
            }

            if (false){
                
                paintMap(points, myTweets);
                
            } else if (false){
                
                Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_33);
                IndexWriter writer = new IndexWriter(idx, analyzer, IndexWriter.MaxFieldLength.UNLIMITED);

                for(int i=0; i<myTweets.length; i++){
                    writer.addDocument(createDocument(Double.toString(points[i][0]), Double.toString(points[i][1]), myTweets[i][0], myTweets[i][1], myTweets[i][2], myTweets[i][3]));
                }

                writer.optimize();
                writer.close();

                myResults1 = new Integer[12][wordIn.size()];

                for (int i=0; i<wordIn.size(); i++){
                    date = new ArrayList<String>();
                    myMonths = null;
                    myResults = null;
                    monthPoints = new ArrayList<Integer>();

                    Searcher searcher = new IndexSearcher(idx);
                    myResults = search(searcher, wordIn.get(i), myTweets.length);
                    searcher.close();
                    for (int k=0; k<myResults.length; k++){
                        date.add(myResults[k][3]);
                    }
                    myMonths=monthPoints(date);
                    for (int k=0; k<12; k++){
                        int monPon = myMonths[k];
                        myResults1[k][i] = monPon;
                    }
                }

                proGraph myGraph = new proGraph(col, wordIn, myResults1, myTweets, 0 , 0);
                myGraph.init();
                myGraph.setPreferredSize(new Dimension(2000,2000));
             
                scrollPane1.add(myGraph);
                System.out.println("1");
       
                myGraph.setVisible(true);
                
            } else {

               final LombardiView lombardiView = new LombardiView(jScroll_network, true);
                lombardiView.setSearchTweet(myInput);
                
                final SocialNetworkVisualizer JungLayouts = new SocialNetworkVisualizer(jScroll_network, true);
                SocialNetworkVisualizer.searchKey = myInput;
                SocialNetworkVisualizer.userInputTweetCount =  jSlider2.getValue();



                final Thread river = new Thread() {

                    public void run() {
                        try {
                            points = test.findPoints(mySearch);
                            myTweets = test.findTweets(mySearch);

                            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_33);
                            IndexWriter writer = new IndexWriter(idx, analyzer, IndexWriter.MaxFieldLength.UNLIMITED);

                            for (int i = 0; i < myTweets.length; i++) {
                                writer.addDocument(createDocument(Double.toString(points[i][0]), Double.toString(points[i][1]), myTweets[i][0], myTweets[i][1], myTweets[i][2], myTweets[i][3]));


                            }

                            writer.optimize();
                            writer.close();

                            myResults1 = new Integer[12][wordIn.size()];

                            for (int i = 0; i < wordIn.size(); i++) {
                                date = new ArrayList<String>();
                                myMonths = null;
                                myResults = null;
                                monthPoints = new ArrayList<Integer>();

                                Searcher searcher = new IndexSearcher(idx);
                                myResults = search(searcher, wordIn.get(i), myTweets.length);
                                searcher.close();
                                for (int k = 0; k < myResults.length; k++) {
                                    date.add(myResults[k][3]);
                                }
                                myMonths = monthPoints(date);
                                for (int k = 0; k < 12; k++) {
                                    int monPon = myMonths[k];
                                    myResults1[k][i] = monPon;
                                }
                            }

                            proGraph myGraph = new proGraph(col, wordIn, myResults1, myTweets, 0, 0);
                            
                            paintMap(points, myTweets);
                            scrollPane1.add(myGraph);
                            myGraph.init();
      
                            scrollPane1.doLayout();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                
                Thread mainThread = new Thread() {

                    public void run() {
                        enableControls(false);
                        try {
                            river.start();
                            // Layout Lombardi
                            if (jComboBox2.getSelectedIndex() == 0) {
                                lombardiView.start();
                                if (lombardiView.isAlive()) {
                                    lombardiView.join();
                                }
                            } // Layout 1
                            else if (jComboBox2.getSelectedIndex() == 1) {
                                SocialNetworkVisualizer.userInputLayout = 1;
                                JungLayouts.start();
                                if (JungLayouts.isAlive()) {
                                    JungLayouts.join();
                                }
                            } // Layout 2
                            else if (jComboBox2.getSelectedIndex() == 2) {
                                SocialNetworkVisualizer.userInputLayout = 2;
                                JungLayouts.start();
                                if (JungLayouts.isAlive()) {
                                    JungLayouts.join();
                                }
                            }
                            else if (jComboBox2.getSelectedIndex() == 3) {
                                SocialNetworkVisualizer.userInputLayout = 3;
                                JungLayouts.start();
                                if (JungLayouts.isAlive()) {
                                    JungLayouts.join();
                                }
                            }
                            else if (jComboBox2.getSelectedIndex() == 4) {
                                SocialNetworkVisualizer.userInputLayout = 4;
                                JungLayouts.start();
                                if (JungLayouts.isAlive()) {
                                    JungLayouts.join();
                                }
                            }

                            
                            if (river.isAlive()) {
                                river.join();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            enableControls(true);
                        }


                    }
                };
                
               mainThread.start();

            }
            
            

            jSlider1.addChangeListener(new ChangeListener(){
                public void stateChanged (ChangeEvent evt) { 
                    
                    if(jSlider1.getValueIsAdjusting())
                        return;
                    
                    
                    JSlider jSlider = (JSlider)evt.getSource(); 
                    iValue = jSlider.getValue(); 

                    
                   final LombardiView lombardiView = new LombardiView(jScroll_network, false);
                   lombardiView.setSearchTweet(myInput);
                   lombardiView.setSearchMonth(iValue);
                  
                   //Shall be enabled if JSlider1 is Mapped with Jung Layouts, currently they are not
                   /**
                final SocialNetworkVisualizer JungLayouts = new SocialNetworkVisualizer(jScroll_network, true);
                SocialNetworkVisualizer.searchKey = myInput;
                    **/
                   

                    
                    myList.model.clear();
//                    jLabel6.setText(null);
//                    jLabel8.setText(null);
//                    jLabel10.setText(null);
//                    jLabel11.setText(null);
//                    jLabel11.setIcon(null);
//                    jPanel2.removeAll();
                    scrollPane1.removeAll();


                 final  Thread river = new Thread(){
                        public void run() {
                            double[][] thePoints = new double[myTweets.length][2];
                            double lat;
                            double lon;
                            if (jcombo == 1) {
                                if (iValue == 0) {
                                    for (int i = 0; i < points.length; i++) {
                                        lat = points[i][0];
                                        thePoints[i][0] = lat;
                                        lon = points[i][1];
                                        thePoints[i][1] = lon;
                                    }
                                    paintMap(thePoints, myTweets);
                                } else {
                                    monthMap(iValue, myTweets, points);
                                }
                            } else if (jcombo == 2) {
                                if (iValue == 0) {
                                    myResults1 = new Integer[12][wordIn.size()];

                                    for (int i = 0; i < wordIn.size(); i++) {
                                        try {
                                            date = new ArrayList<String>();
                                            myMonths = null;
                                            myResults = null;
                                            monthPoints = new ArrayList<Integer>();

                                            Searcher searcher = new IndexSearcher(idx);
                                            myResults = search(searcher, wordIn.get(i), myTweets.length);
                                            searcher.close();
                                            for (int k = 0; k < myResults.length; k++) {
                                                date.add(myResults[k][3]);
                                            }
                                            myMonths = monthPoints(date);
                                            for (int k = 0; k < 12; k++) {
                                                int monPon = myMonths[k];
                                                myResults1[k][i] = monPon;
                                            }
                                        } catch (ParseException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (IOException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }

                                    proGraph myGraph = new proGraph(col, wordIn, myResults1, myTweets, 0, 0);
                                    myGraph.init();
                                    // jPanel2.add(myGraph);
                                    myGraph.setPreferredSize(new Dimension(2000, 2000));
                                    scrollPane1.add(myGraph);
                                    //scrollPane1.setViewportView(myGraph);

                                } else {
                                    myResults1 = new Integer[31][wordIn.size()];

                                    for (int i = 0; i < wordIn.size(); i++) {
                                        try {
                                            date = new ArrayList<String>();
                                            myDays = null;
                                            myResults = null;
                                            monthPoints = new ArrayList<Integer>();


                                            Searcher searcher = new IndexSearcher(idx);
                                            myResults = search(searcher, wordIn.get(i), myTweets.length);
                                            searcher.close();
                                            for (int k = 0; k < myResults.length; k++) {
                                                date.add(myResults[k][3]);
                                            }
                                            myDays = dayPoints(date, iValue);
                                            for (int k = 0; k < myDays.length; k++) {
                                                int monPon = myDays[k];
                                                myResults1[k][i] = monPon;
                                            }
                                        } catch (ParseException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (CorruptIndexException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (IOException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }


                                    proGraph myGraph1 = new proGraph(col, wordIn, myResults1, myTweets, iValue, 0);
                                    myGraph1.init();
                                    //jPanel2.add(myGraph1);
                                    myGraph1.setPreferredSize(new Dimension(2000, 2000));
                                    scrollPane1.add(myGraph1);
                                    //ScrollPane1.setViewportView(myGraph1);
                                }

                            } else {
                                if (iValue == 0) {
                                    for (int i = 0; i < points.length; i++) {
                                        lat = points[i][0];
                                        thePoints[i][0] = lat;
                                        lon = points[i][1];
                                        thePoints[i][1] = lon;
                                    }
                                    paintMap(thePoints, myTweets);

                                    myResults1 = new Integer[12][wordIn.size()];

                                    for (int i = 0; i < wordIn.size(); i++) {
                                        try {
                                            date = new ArrayList<String>();
                                            myMonths = null;
                                            myResults = null;
                                            monthPoints = new ArrayList<Integer>();

                                            Searcher searcher = new IndexSearcher(idx);
                                            myResults = search(searcher, wordIn.get(i), myTweets.length);
                                            searcher.close();
                                            for (int k = 0; k < myResults.length; k++) {
                                                date.add(myResults[k][3]);
                                            }
                                            myMonths = monthPoints(date);
                                            for (int k = 0; k < 12; k++) {
                                                int monPon = myMonths[k];
                                                myResults1[k][i] = monPon;
                                            }
                                        } catch (ParseException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (CorruptIndexException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (IOException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }

                                    proGraph myGraph = new proGraph(col, wordIn, myResults1, myTweets, 0, 0);
                                    myGraph.init();
                                    //jPanel2.add(myGraph);
                                    myGraph.setPreferredSize(new Dimension(2000, 2000));
                                    scrollPane1.add(myGraph);
                                    //ScrollPane1.setViewportView(myGraph);

                                } else {
                                    monthMap(iValue, myTweets, points);

                                    myResults1 = new Integer[31][wordIn.size()];

                                    for (int i = 0; i < wordIn.size(); i++) {
                                        try {
                                            date = new ArrayList<String>();
                                            myDays = null;
                                            myResults = null;
                                            monthPoints = new ArrayList<Integer>();


                                            Searcher searcher = new IndexSearcher(idx);
                                            myResults = search(searcher, wordIn.get(i), myTweets.length);
                                            searcher.close();
                                            for (int k = 0; k < myResults.length; k++) {
                                                date.add(myResults[k][3]);
                                            }
                                            myDays = dayPoints(date, iValue);
                                            for (int k = 0; k < myDays.length; k++) {
                                                int monPon = myDays[k];
                                                myResults1[k][i] = monPon;
                                            }
                                        } catch (ParseException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (CorruptIndexException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (IOException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }


                                    proGraph myGraph1 = new proGraph(col, wordIn, myResults1, myTweets, iValue, 0);
                                    myGraph1.init();
                                    // jPanel2.add(myGraph1);
                                    myGraph1.setPreferredSize(new Dimension(2000, 2000));
                                    scrollPane1.add(myGraph1);
                                    //ScrollPane1.setViewportView(myGraph1);
                                }
                            }
                        }
                    };
                    
                 Thread mainThread = new Thread() {

                    public void run() {
                        enableControls(false);
                        try {
                            river.start();
                            // Layout Lombardi
                            if (jComboBox2.getSelectedIndex() == 0) {
                                lombardiView.start();
                                if (lombardiView.isAlive()) {
                                    lombardiView.join();
                                }
                            } // Layout 1
                            /*
                            else if (jComboBox2.getSelectedIndex() == 1) {
                                SocialNetworkVisualizer.userInputLayout = 1;
                                JungLayouts.start();
                                if (JungLayouts.isAlive()) {
                                    JungLayouts.join();
                                }
                            } // Layout 2
                            else if (jComboBox2.getSelectedIndex() == 2) {
                                SocialNetworkVisualizer.userInputLayout = 2;
                                JungLayouts.start();
                                if (JungLayouts.isAlive()) {
                                    JungLayouts.join();
                                }
                            }
                            else if (jComboBox2.getSelectedIndex() == 3) {
                                SocialNetworkVisualizer.userInputLayout = 3;
                                JungLayouts.start();
                                if (JungLayouts.isAlive()) {
                                    JungLayouts.join();
                                }
                            } // Layout 2
                            else if (jComboBox2.getSelectedIndex() == 4) {
                                SocialNetworkVisualizer.userInputLayout = 4;
                                JungLayouts.start();
                                if (JungLayouts.isAlive()) {
                                    JungLayouts.join();
                                }
                            }
                             */
                            
                            if (river.isAlive()) {
                                river.join();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            enableControls(true);
                        }


                    }
                };
                
               mainThread.start();

            }

                
            });
            
            
            jSlider2.addChangeListener(new ChangeListener(){
                public void stateChanged (ChangeEvent evt1) { 
                     
                 
                if(jSlider2.getValueIsAdjusting())
                        return;
                
                JSlider jSlider = (JSlider)evt1.getSource(); 
                    iValue1 = jSlider.getValue(); 
                    //jSlider2.setValue(iValue);
                    
                final SocialNetworkVisualizer JungLayouts = new SocialNetworkVisualizer(jScroll_network, true);
                SocialNetworkVisualizer.searchKey = myInput;
                SocialNetworkVisualizer.userInputTweetCount = iValue1; 
                
                Thread mainThread = new Thread() {
                    public void run() {
                        enableControls(false);
                    
                    if (jComboBox2.getSelectedIndex() == 0) {

                            } // Layout 1
                            
                            else if (jComboBox2.getSelectedIndex() == 1) {
                                SocialNetworkVisualizer.userInputLayout = 1;
                                JungLayouts.start();
                                if (JungLayouts.isAlive()) {
                                        try {
                                            JungLayouts.join();
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                }
                            } // Layout 2
                            else if (jComboBox2.getSelectedIndex() == 2) {
                                SocialNetworkVisualizer.userInputLayout = 2;
                                JungLayouts.start();
                                if (JungLayouts.isAlive()) {
                                        try {
                                            JungLayouts.join();
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                }
                            }
                            else if (jComboBox2.getSelectedIndex() == 3) {
                                SocialNetworkVisualizer.userInputLayout = 3;
                                JungLayouts.start();
                                if (JungLayouts.isAlive()) {
                                        try {
                                            JungLayouts.join();
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                }
                            } // Layout 2
                            else if (jComboBox2.getSelectedIndex() == 4) {
                                SocialNetworkVisualizer.userInputLayout = 4;
                                JungLayouts.start();
                                if (JungLayouts.isAlive()) {
                                        try {
                                            JungLayouts.join();
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                }
                            }
                             
                };
                    
                    
                
                };
                        }
            });
            


//            jSlider2.addChangeListener(new ChangeListener(){
//                public void stateChanged (ChangeEvent evt) {
//                    if (iValue != 0 ){
//                    JSlider jSlider1 = (JSlider)evt.getSource(); 
//                    iValue1 = jSlider1.getValue(); 
//
//                    myList.model.clear();
//                    jLabel6.setText(null);
//                    jLabel8.setText(null);
//                    jLabel10.setText(null);
//                    jLabel11.setText(null);
//                    jLabel11.setIcon(null);                    
////                    jPanel2.removeAll();
//                    scrollPane1.removeAll();
//
//                    if (jcombo == 1){
//                        if (iValue1 == 0){
//                            monthMap(iValue,myTweets,points);
//                        } else {
//                            dayMap(iValue, iValue1, myTweets,points);
//                        }
//                    } else if (jcombo == 2){
//                        if(iValue1 == 0){
//                            myResults1 = new Integer[31][wordIn.size()];
//
//                            for (int i=0; i<wordIn.size(); i++){
//                                try {
//                                    date = new ArrayList<String>();
//                                    myDays = null;
//                                    myResults = null;
//                                    monthPoints = new ArrayList<Integer>();
//
//
//                                    Searcher searcher = new IndexSearcher(idx);
//                                    myResults = search(searcher, wordIn.get(i), myTweets.length);
//                                    searcher.close();
//                                    for (int k=0; k<myResults.length; k++){
//                                        date.add(myResults[k][3]);
//                                    }
//                                    myDays = dayPoints(date, iValue);
//                                    for (int k=0; k<myDays.length; k++){
//                                        int monPon = myDays[k];
//                                        myResults1[k][i] = monPon;
//                                    }
//                                } catch (ParseException ex) {
//                                    Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
//                                } catch (CorruptIndexException ex) {
//                                    Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
//                                } catch (IOException ex) {
//                                    Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }
//
//
//                            proGraph myGraph1 = new proGraph(col, wordIn, myResults1, myTweets, iValue, 0);
//                            myGraph1.init();
//                            //jPanel2.add(myGraph1);
//                            myGraph1.setPreferredSize(new Dimension(2000,2000));
//                            scrollPane1.add(myGraph1);
////                            ScrollPane1.setViewportView(myGraph1);
//                        }else {
//                            myResults1 = new Integer[24][wordIn.size()];
//
//                            for (int i=0; i<wordIn.size(); i++){
//                                try {
//                                    date = new ArrayList<String>();
//                                    myHours = null;
//                                    myResults = null;
//                                    monthPoints = new ArrayList<Integer>();
//
//
//                                    Searcher searcher = new IndexSearcher(idx);
//                                    myResults = search(searcher, wordIn.get(i), myTweets.length);
//                                    searcher.close();
//                                    for (int k=0; k<myResults.length; k++){
//                                        date.add(myResults[k][3]);
//                                    }
//                                    myHours = hourPoints(date, iValue, iValue1);
//                                    for (int k=0; k<myHours.length; k++){
//                                        int monPon = myHours[k];
//                                        myResults1[k][i] = monPon;
//                                    }
//                                } catch (ParseException ex) {
//                                    Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
//                                } catch (CorruptIndexException ex) {
//                                    Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
//                                } catch (IOException ex) {
//                                    Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }
//
//                            proGraph myGraph1 = new proGraph(col, wordIn, myResults1, myTweets, iValue, iValue1);
//                            myGraph1.init();
//                           // jPanel2.add(myGraph1);
//                            myGraph1.setPreferredSize(new Dimension(2000,2000));
//                           scrollPane1.add(myGraph1);
////                            ScrollPane1.setViewportView(myGraph1);
//                        }
//                    } else {
//                        if (iValue1 == 0){
//                            monthMap(iValue,myTweets,points);
//
//                            myResults1 = new Integer[31][wordIn.size()];
//
//                            for (int i=0; i<wordIn.size(); i++){
//                                try {
//                                    date = new ArrayList<String>();
//                                    myDays = null;
//                                    myResults = null;
//                                    monthPoints = new ArrayList<Integer>();
//
//
//                                    Searcher searcher = new IndexSearcher(idx);
//                                    myResults = search(searcher, wordIn.get(i), myTweets.length);
//                                    searcher.close();
//                                    for (int k=0; k<myResults.length; k++){
//                                        date.add(myResults[k][3]);
//                                    }
//                                    myDays = dayPoints(date, iValue);
//                                    for (int k=0; k<myDays.length; k++){
//                                        int monPon = myDays[k];
//                                        myResults1[k][i] = monPon;
//                                    }
//                                } catch (ParseException ex) {
//                                    Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
//                                } catch (CorruptIndexException ex) {
//                                    Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
//                                } catch (IOException ex) {
//                                    Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }
//
//
//                            proGraph myGraph1 = new proGraph(col, wordIn, myResults1, myTweets, iValue, 0);
//                            myGraph1.init();
//                            //jPanel2.add(myGraph1);
//                            myGraph1.setPreferredSize(new Dimension(2000,2000));
//                            scrollPane1.add(myGraph1);
////                            ScrollPane1.setViewportView(myGraph1);
//                        } else {
//                            myResults1 = new Integer[24][wordIn.size()];
//
//                            for (int i=0; i<wordIn.size(); i++){
//                                try {
//                                    date = new ArrayList<String>();
//                                    myHours = null;
//                                    myResults = null;
//                                    monthPoints = new ArrayList<Integer>();
//
//
//                                    Searcher searcher = new IndexSearcher(idx);
//                                    myResults = search(searcher, wordIn.get(i), myTweets.length);
//                                    searcher.close();
//                                    for (int k=0; k<myResults.length; k++){
//                                        date.add(myResults[k][3]);
//                                    }
//                                    myHours = hourPoints(date, iValue, iValue1);
//                                    for (int k=0; k<myHours.length; k++){
//                                        int monPon = myHours[k];
//                                        myResults1[k][i] = monPon;
//                                    }
//                                } catch (ParseException ex) {
//                                    Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
//                                } catch (CorruptIndexException ex) {
//                                    Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
//                                } catch (IOException ex) {
//                                    Logger.getLogger(Prototype3View.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }
//
//                            proGraph myGraph1 = new proGraph(col, wordIn, myResults1, myTweets, iValue, iValue1);
//                            myGraph1.init();
//                           // jPanel2.add(myGraph1);
//                            myGraph1.setPreferredSize(new Dimension(2000,2000));
//                            scrollPane1.add(myGraph1);
////                            ScrollPane1.setViewportView(myGraph1);
//
//                            dayMap(iValue, iValue1, myTweets,points);
//                        }
//                    }
//                    }
//                }
//            });
            
            myList newList = new myList(myTweets, points, jcombo);          

            myInit();
            
        } catch (SQLException s){
            System.out.println("SQL Error: " + s.toString() + " " + s.getErrorCode() + " " + s.getSQLState());
            s.printStackTrace();
        } catch (Exception e){
            System.out.println("Error: " + e.toString() + " " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}//GEN-LAST:event_jButton1ActionPerformed

    private void myInit(){
        jSlider1.setValue(0);
        //jSlider2.setValue(20);
        myList.model.clear();
//        jLabel6.setText(null);
//        jLabel8.setText(null);
//        jLabel10.setText(null);
//        jLabel11.setText(null);
//        jLabel11.setIcon(null);
//        jComboBox1.setSelectedIndex(0);
    }
    
    private void myRadio(String[] mPopular){
        if (mPopular.length<1){
//            jRadioButton1.setVisible(false);
//            jRadioButton2.setVisible(false);
//            jRadioButton3.setVisible(false);
//            jRadioButton4.setVisible(false);
//            jRadioButton5.setVisible(false);
//            jRadioButton6.setVisible(false);
//            jRadioButton7.setVisible(false);
//            jRadioButton8.setVisible(false);
//            jRadioButton9.setVisible(false);
//            jRadioButton10.setVisible(false);
        } else if (mPopular.length == 1){
//            jRadioButton1.setVisible(true);
//            jRadioButton1.setText(mPopular[0]);
//            jRadioButton2.setVisible(false);
//            jRadioButton3.setVisible(false);
//            jRadioButton4.setVisible(false);
//            jRadioButton5.setVisible(false);
//            jRadioButton6.setVisible(false);
//            jRadioButton7.setVisible(false);
//            jRadioButton8.setVisible(false);
//            jRadioButton9.setVisible(false);
//            jRadioButton10.setVisible(false);
        } else if (mPopular.length == 2){
//            jRadioButton1.setVisible(true);
//            jRadioButton1.setText(mPopular[0]);
//            jRadioButton2.setVisible(true);
//            jRadioButton2.setText(mPopular[1]);
//            jRadioButton3.setVisible(false);
//            jRadioButton4.setVisible(false);
//            jRadioButton5.setVisible(false);
//            jRadioButton6.setVisible(false);
//            jRadioButton7.setVisible(false);
//            jRadioButton8.setVisible(false);
//            jRadioButton9.setVisible(false);
//            jRadioButton10.setVisible(false);
        } else if (mPopular.length == 3){
//            jRadioButton1.setVisible(true);
//            jRadioButton1.setText(mPopular[0]);
//            jRadioButton2.setVisible(true);
//            jRadioButton2.setText(mPopular[1]);
//            jRadioButton3.setVisible(true);
//            jRadioButton3.setText(mPopular[2]);
//            jRadioButton4.setVisible(false);
//            jRadioButton5.setVisible(false);
//            jRadioButton6.setVisible(false);
//            jRadioButton7.setVisible(false);
//            jRadioButton8.setVisible(false);
//            jRadioButton9.setVisible(false);
//            jRadioButton10.setVisible(false);
        } else if (mPopular.length == 4){
//            jRadioButton1.setVisible(true);
//            jRadioButton1.setText(mPopular[0]);
//            jRadioButton2.setVisible(true);
//            jRadioButton2.setText(mPopular[1]);
//            jRadioButton3.setVisible(true);
//            jRadioButton3.setText(mPopular[2]);
//            jRadioButton4.setVisible(true);
//            jRadioButton4.setText(mPopular[3]);
//            jRadioButton5.setVisible(false);
//            jRadioButton6.setVisible(false);
//            jRadioButton7.setVisible(false);
//            jRadioButton8.setVisible(false);
//            jRadioButton9.setVisible(false);
//            jRadioButton10.setVisible(false);
        }  else if (mPopular.length == 5){
//            jRadioButton1.setVisible(true);
//            jRadioButton1.setText(mPopular[0]);
//            jRadioButton2.setVisible(true);
//            jRadioButton2.setText(mPopular[1]);
//            jRadioButton3.setVisible(true);
//            jRadioButton3.setText(mPopular[2]);
//            jRadioButton4.setVisible(true);
//            jRadioButton4.setText(mPopular[3]);
//            jRadioButton5.setVisible(true);
//            jRadioButton5.setText(mPopular[4]);
//            jRadioButton6.setVisible(false);
//            jRadioButton7.setVisible(false);
//            jRadioButton8.setVisible(false);
//            jRadioButton9.setVisible(false);
//            jRadioButton10.setVisible(false);
        }   else if (mPopular.length == 6){
//            jRadioButton1.setVisible(true);
//            jRadioButton1.setText(mPopular[0]);
//            jRadioButton2.setVisible(true);
//            jRadioButton2.setText(mPopular[1]);
//            jRadioButton3.setVisible(true);
//            jRadioButton3.setText(mPopular[2]);
//            jRadioButton4.setVisible(true);
//            jRadioButton4.setText(mPopular[3]);
//            jRadioButton5.setVisible(true);
//            jRadioButton5.setText(mPopular[4]);
//            jRadioButton6.setVisible(true);
//            jRadioButton6.setText(mPopular[5]);
//            jRadioButton7.setVisible(false);
//            jRadioButton8.setVisible(false);
//            jRadioButton9.setVisible(false);
//            jRadioButton10.setVisible(false);
        }   else if (mPopular.length == 7){
//            jRadioButton1.setVisible(true);
//            jRadioButton1.setText(mPopular[0]);
//            jRadioButton2.setVisible(true);
//            jRadioButton2.setText(mPopular[1]);
//            jRadioButton3.setVisible(true);
//            jRadioButton3.setText(mPopular[2]);
//            jRadioButton4.setVisible(true);
//            jRadioButton4.setText(mPopular[3]);
//            jRadioButton5.setVisible(true);
//            jRadioButton5.setText(mPopular[4]);
//            jRadioButton6.setVisible(true);
//            jRadioButton6.setText(mPopular[5]);
//            jRadioButton7.setVisible(true);
//            jRadioButton7.setText(mPopular[6]);
//            jRadioButton8.setVisible(false);
//            jRadioButton9.setVisible(false);
//            jRadioButton10.setVisible(false);
        }   else if (mPopular.length == 8){
//            jRadioButton1.setVisible(true);
//            jRadioButton1.setText(mPopular[0]);
//            jRadioButton2.setVisible(true);
//            jRadioButton2.setText(mPopular[1]);
//            jRadioButton3.setVisible(true);
//            jRadioButton3.setText(mPopular[2]);
//            jRadioButton4.setVisible(true);
//            jRadioButton4.setText(mPopular[3]);
//            jRadioButton5.setVisible(true);
//            jRadioButton5.setText(mPopular[4]);
//            jRadioButton6.setVisible(true);
//            jRadioButton6.setText(mPopular[5]);
//            jRadioButton7.setVisible(true);
//            jRadioButton7.setText(mPopular[6]);
//            jRadioButton8.setVisible(true);
//            jRadioButton8.setText(mPopular[7]);
//            jRadioButton9.setVisible(false);
//            jRadioButton10.setVisible(false);
        }   else if (mPopular.length == 9){
//            jRadioButton1.setVisible(true);
//            jRadioButton1.setText(mPopular[0]);
//            jRadioButton2.setVisible(true);
//            jRadioButton2.setText(mPopular[1]);
//            jRadioButton3.setVisible(true);
//            jRadioButton3.setText(mPopular[2]);
//            jRadioButton4.setVisible(true);
//            jRadioButton4.setText(mPopular[3]);
//            jRadioButton5.setVisible(true);
//            jRadioButton5.setText(mPopular[4]);
//            jRadioButton6.setVisible(true);
//            jRadioButton6.setText(mPopular[5]);
//            jRadioButton7.setVisible(true);
//            jRadioButton7.setText(mPopular[6]);
//            jRadioButton8.setVisible(true);
//            jRadioButton8.setText(mPopular[7]);
//            jRadioButton9.setVisible(true);
//            jRadioButton9.setText(mPopular[8]);
//            jRadioButton10.setVisible(false);
        }   else if (mPopular.length == 10){
//            jRadioButton1.setVisible(true);
//            jRadioButton1.setText(mPopular[0]);
//            jRadioButton2.setVisible(true);
//            jRadioButton2.setText(mPopular[1]);
//            jRadioButton3.setVisible(true);
//            jRadioButton3.setText(mPopular[2]);
//            jRadioButton4.setVisible(true);
//            jRadioButton4.setText(mPopular[3]);
//            jRadioButton5.setVisible(true);
//            jRadioButton5.setText(mPopular[4]);
//            jRadioButton6.setVisible(true);
//            jRadioButton6.setText(mPopular[5]);
//            jRadioButton7.setVisible(true);
//            jRadioButton7.setText(mPopular[6]);
//            jRadioButton8.setVisible(true);
//            jRadioButton8.setText(mPopular[7]);
//            jRadioButton9.setVisible(true);
//            jRadioButton9.setText(mPopular[8]);
//            jRadioButton10.setVisible(true);
//            jRadioButton10.setText(mPopular[9]);
        }
    }

    private String getSel(){
        String mySel = null;
        mySel1 = new ArrayList<String>();
        
//        if(jRadioButton1.isSelected()){
//            mySel1.add(jRadioButton1.getActionCommand());
//        }
//        if(jRadioButton2.isSelected()){
//            mySel1.add(jRadioButton2.getActionCommand());
//        }
//        if(jRadioButton3.isSelected()){
//            mySel1.add(jRadioButton3.getActionCommand());
//        }
//        if(jRadioButton4.isSelected()){
//            mySel1.add(jRadioButton4.getActionCommand());
//        }
//        if(jRadioButton5.isSelected()){
//            mySel1.add(jRadioButton5.getActionCommand());
//        }
//        if(jRadioButton6.isSelected()){
//            mySel1.add(jRadioButton6.getActionCommand());
//        }
//        if(jRadioButton7.isSelected()){
//            mySel1.add(jRadioButton7.getActionCommand());
//        }
//        if(jRadioButton8.isSelected()){
//            mySel1.add(jRadioButton8.getActionCommand());
//        }
//        if(jRadioButton9.isSelected()){
//            mySel1.add(jRadioButton9.getActionCommand());
//        }
//        if(jRadioButton10.isSelected()){
//            mySel1.add(jRadioButton10.getActionCommand());
//        }
        
        if(!mySel1.isEmpty()){
            mySel = "tweettext LIKE '%" + mySel1.get(0) + "%'"; 
            for (int i=1; i<mySel1.size(); i++){
                mySel = mySel + " OR tweettext LIKE '%" + mySel1.get(i) + "%'";
            }
        }
        return mySel;
    }
    
    private String searchKey(String myInput1){
        ArrayList<Integer> myWords = new ArrayList<Integer>();
        String mySearch1;
        
        for (int i=0; i<myInput1.length(); i++){
            if(myInput1.charAt(i) == ' '){
                myWords.add(i);
            }
        }
        
        if(myWords.isEmpty()){
            mySearch1 = "tweettext LIKE '%" + myInput1 + "%'";
        } else {
            mySearch1 = "tweettext LIKE '%" + myInput1.substring(0, myWords.get(0)) + "%'";
            for (int i=0; i< myWords.size(); i++){
                if(i+1 == myWords.size()){
                    mySearch1 = mySearch1 + " OR tweettext LIKE '%" + myInput1.substring(myWords.get(i)+1, myInput1.length()) + "%'";
                } else {
                    mySearch1 = mySearch1 + " OR tweettext LIKE '%" + myInput1.substring(myWords.get(i)+1, myWords.get(i+1)) + "%'";
                }
            }
        }
        
        return mySearch1;
    }
    
    private ArrayList<String> getWords(String mInput){
        ArrayList<String> myWords = new ArrayList<String>();
        ArrayList<Integer> mySpace = new ArrayList<Integer>();
        
        for (int i=0; i<mInput.length(); i++){
            if(mInput.charAt(i) == ' '){
                mySpace.add(i);
            }
        }
        if(mySpace.isEmpty()){
            myWords.add(mInput);
        } else {
            myWords.add(mInput.substring(0, mySpace.get(0)));
            for (int i=0; i<mySpace.size(); i++){
                if(i+1 == mySpace.size()){
                    myWords.add(mInput.substring(mySpace.get(i)+1, mInput.length()));
                } else {
                    myWords.add(mInput.substring(mySpace.get(i)+1, mySpace.get(i+1)));
                }
            }
        }
        return myWords;
    }
    
    private ArrayList<String> getWords1(){
        ArrayList<String> myWords = new ArrayList<String>();
//        
//        if(jRadioButton1.isSelected()){
//            myWords.add(jRadioButton1.getActionCommand());
//        }
//        if(jRadioButton2.isSelected()){
//            myWords.add(jRadioButton2.getActionCommand());
//        }
//        if(jRadioButton3.isSelected()){
//            myWords.add(jRadioButton3.getActionCommand());
//        }
//        if(jRadioButton4.isSelected()){
//            myWords.add(jRadioButton4.getActionCommand());
//        }
//        if(jRadioButton5.isSelected()){
//            myWords.add(jRadioButton5.getActionCommand());
//        }
//        if(jRadioButton6.isSelected()){
//            myWords.add(jRadioButton6.getActionCommand());
//        }
//        if(jRadioButton7.isSelected()){
//            myWords.add(jRadioButton7.getActionCommand());
//        }
//        if(jRadioButton8.isSelected()){
//            myWords.add(jRadioButton8.getActionCommand());
//        }
//        if(jRadioButton9.isSelected()){
//            myWords.add(jRadioButton9.getActionCommand());
//        }
//        if(jRadioButton10.isSelected()){
//            myWords.add(jRadioButton10.getActionCommand());
//        }
//        
        return myWords;
    }
    
    private static Document createDocument(String lat1, String lon1, String user, String date, String picture, String content) {
      Document doc = new Document();
      
      doc.add(new Field("lat", lat1, Field.Store.YES, Field.Index.NO));
      doc.add(new Field("lon", lon1, Field.Store.YES, Field.Index.NO));
      doc.add(new Field("user", user, Field.Store.YES, Field.Index.NO));
      doc.add(new Field("date", date, Field.Store.YES, Field.Index.NO));
      doc.add(new Field("picture", picture, Field.Store.YES, Field.Index.NO));
      doc.add(new Field("content", content, Field.Store.YES, Field.Index.ANALYZED));
      
      return doc;
   }
    
    private String[][] search(Searcher searcher, String queryString, int tweetLength) throws ParseException, IOException {
        ArrayList<String> myLat = new ArrayList<String>();
        ArrayList<String> myLon = new ArrayList<String>();
        ArrayList<String> myTweet = new ArrayList<String>();
        ArrayList<String> myUser = new ArrayList<String>();
        ArrayList<String> myPicture = new ArrayList<String>();
        ArrayList<String> myDate = new ArrayList<String>();
        ArrayList<Double> lat2 = new ArrayList<Double>();
        ArrayList<Double> lon2 = new ArrayList<Double>();
        
        String[][] myResult;
        
        QueryParser parser = new QueryParser(Version.LUCENE_33, "content", new StandardAnalyzer(Version.LUCENE_33));
        Query query = parser.parse(queryString);
        
        TopScoreDocCollector collector = TopScoreDocCollector.create(5 * tweetLength, false);
        searcher.search(query, collector);

        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        int hitCount = collector.getTotalHits();
        
        myResult = new String[hitCount][6];
        
        if (hitCount == 0) {
            System.out.println("No matches were found for \"" + queryString + "\"");
        } else {
            for (int i = 0; i<hitCount; i++) {
                ScoreDoc scoreDoc = hits[i];
                int docId = scoreDoc.doc;
                Document doc = searcher.doc(docId);
                myLat.add(doc.get("lat"));
                lat2.add(Double.parseDouble(doc.get("lat")));
                lon2.add(Double.parseDouble(doc.get("lon")));
                myLon.add(doc.get("lon"));
                myUser.add(doc.get("user"));
                myDate.add(doc.get("date"));
                myPicture.add(doc.get("picture"));
                myTweet.add(doc.get("content"));
            }
            
            for (int i=0; i<myTweet.size(); i++){
                myResult[i][0] = myLat.get(i);
                myResult[i][1] = myLon.get(i);
                myResult[i][2] = myUser.get(i);
                myResult[i][3] = myDate.get(i);
                myResult[i][4] = myPicture.get(i);
                myResult[i][5] = myTweet.get(i);
            }
            
        }
        
      
        return myResult;
   }
    
    private void paintMap(final double[][] mpoints, final String[][] myDetails1){
        
        Set<Waypoint> waypoints = new HashSet<Waypoint>();
        WaypointPainter painter = new WaypointPainter();
        
                    
        for ( int i = 0; i < mpoints.length ; i++){
            waypoints.add(new Waypoint(mpoints[i][1], mpoints[i][0]));
            painter.setWaypoints(waypoints);
        }
       
        count1 = 0;
        painter.setRenderer(new WaypointRenderer() {
            public boolean paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint wp) { 
                String[] words = null;
                    
                if(count1 == myDetails1.length){
                    count1 = 0;
                }
                    
                words = myDetails1[count1][3].split(" "); 
                int myRan = randomGenerator.nextInt(words.length);
                while (myRan < 0){
                    myRan = randomGenerator.nextInt(words.length);
                }
                disText = words[myRan];
                
                int j=0;
                for (int i=0; i<wordIn.size(); i++){
                    if (myDetails1[count1][3].toLowerCase().contains(wordIn.get(i))){
                        col1 = col[i];
                        j++;
                    }
                }
                count1++;
                    
                g.setPaint(new Color(0,0,col1));
                Polygon triangle = new Polygon();
                triangle.addPoint(0,0);
                triangle.addPoint(11,11);
                triangle.addPoint(-11,11);
                g.fill(triangle);
                int width = (int) g.getFontMetrics().getStringBounds(disText, g).getWidth();
                g.fillRoundRect(-width/2 -5, 10, width+10, 20, 10, 10);

                g.setPaint(Color.BLACK);
                g.drawString(disText, -width/2-1, 26-1); //shadow
                g.drawString(disText, -width/2-1, 26-1); //shadow
                g.setPaint(Color.WHITE);
                g.drawString(disText, -width/2, 26); //text
                return false;
            }
        });
        
        jXMapKit1.getMainMap().setOverlayPainter(painter);


        
//        jXMapKit1.setAddressLocation(new GeoPosition(51.5, -0.5));
    }
    
    
    private void monthMap(int value, String[][] myDetails, double[][] mPoints){
        double[][] thePoints1 = new double[mPoints.length][2];
        double lat1;
        double lon1;
        
        if (value == 1){
            
            for (int i=0; i<myDetails.length; i++ ){
                if ((myDetails[i][1].substring(5, 7)).equals("01")){
                    lat1 = mPoints[i][0];
                    lat1 = mPoints[i][4];
                    thePoints1[i][0] = lat1;
                    lon1 = mPoints[i][1];
                    lon1 = mPoints[i][5];
                    thePoints1[i][1] = lon1;
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value == 2){
            
            for (int i=0; i<myDetails.length; i++ ){
                if ((myDetails[i][1].substring(5, 7)).equals("02")){
                    lat1 = mPoints[i][0];
                    thePoints1[i][0] = lat1;
                    lon1 = mPoints[i][1];
                    thePoints1[i][1] = lon1;
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value == 3){
            
            for (int i=0; i<myDetails.length; i++ ){
                if ((myDetails[i][1].substring(5, 7)).equals("03")){
                    lat1 = mPoints[i][0];
                    thePoints1[i][0] = lat1;
                    lon1 = mPoints[i][1];
                    thePoints1[i][1] = lon1;
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value == 4){
            
            for (int i=0; i<myDetails.length; i++ ){
                if ((myDetails[i][1].substring(5, 7)).equals("04")){
                    lat1 = mPoints[i][0];
                    thePoints1[i][0] = lat1;
                    lon1 = mPoints[i][1];
                    thePoints1[i][1] = lon1;
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value == 5){
            
            for (int i=0; i<myDetails.length; i++ ){
                if ((myDetails[i][1].substring(5, 7)).equals("05")){
                    lat1 = mPoints[i][0];
                    thePoints1[i][0] = lat1;
                    lon1 = mPoints[i][1];
                    thePoints1[i][1] = lon1;
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value == 6){
            
            for (int i=0; i<myDetails.length; i++ ){
                if ((myDetails[i][1].substring(5, 7)).equals("06")){
                    lat1 = mPoints[i][0];
                    thePoints1[i][0] = lat1;
                    lon1 = mPoints[i][1];
                    thePoints1[i][1] = lon1;
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value == 7){
            
            for (int i=0; i<myDetails.length; i++ ){
                if ((myDetails[i][1].substring(5, 7)).equals("07")){
                    lat1 = mPoints[i][0];
                    thePoints1[i][0] = lat1;
                    lon1 = mPoints[i][1];
                    thePoints1[i][1] = lon1;
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value == 8){
            
            for (int i=0; i<myDetails.length; i++ ){
                if ((myDetails[i][1].substring(5, 7)).equals("08")){
                    lat1 = mPoints[i][0];
                    thePoints1[i][0] = lat1;
                    lon1 = mPoints[i][1];
                    thePoints1[i][1] = lon1;
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value == 9){
            
            for (int i=0; i<myDetails.length; i++ ){
                if ((myDetails[i][1].substring(5, 7)).equals("09")){
                    lat1 = mPoints[i][0];
                    thePoints1[i][0] = lat1;
                    lon1 = mPoints[i][1];
                    thePoints1[i][1] = lon1;
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value == 10){
            
            for (int i=0; i<myDetails.length; i++ ){
                if ((myDetails[i][1].substring(5, 7)).equals("10")){
                    lat1 = mPoints[i][0];
                    thePoints1[i][0] = lat1;
                    lon1 = mPoints[i][1];
                    thePoints1[i][1] = lon1;
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value == 11){
            
            for (int i=0; i<myDetails.length; i++ ){
                if ((myDetails[i][1].substring(5, 7)).equals("11")){
                    lat1 = mPoints[i][0];
                    thePoints1[i][0] = lat1;
                    lon1 = mPoints[i][1];
                    thePoints1[i][1] = lon1;
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value == 12){
            
            for (int i=0; i<myDetails.length; i++ ){
                if ((myDetails[i][1].substring(5, 7)).equals("12")){
                    lat1 = mPoints[i][0];
                    thePoints1[i][0] = lat1;
                    lon1 = mPoints[i][1];
                    thePoints1[i][1] = lon1;
                }
            }
            paintMap(thePoints1, myDetails);
            
        } 
    }
    
    private void dayMap(int value, int value1, String[][] myDetails, double[][] mPoints){
        double[][] thePoints1 = new double[mPoints.length][2];
        double lat1;
        double lon1;
        
        if (value1 == 1){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("01")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 2){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("02")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 3){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("03")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        }  else if (value1 == 4){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("04")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 5){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("05")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 6){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("06")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 7){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("07")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 8){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("08")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 9){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("09")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;            
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 10){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("10")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 11){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("11")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 12){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("12")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 13){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("13")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 14){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("14")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 15){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("15")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 16){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("16")){
                         lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 17){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("17")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 18){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("18")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 19){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("19")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 20){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("20")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 21){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("21")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 22){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("22")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 23){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("23")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 24){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("24")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 25){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("25")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 26){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("26")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 27){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("27")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 28){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("28")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 29){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("29")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 30){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("30")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        } else if (value1 == 31){
            
            for (int i=0; i<myDetails.length; i++ ){
                if (((Integer.toString(value).equals(myDetails[i][1].substring(6,7))) || ((Integer.toString(value).equals(myDetails[i][1].substring(5, 7))) && myDetails[i][1].substring(5, 6).equals("0")))){
                    if ((myDetails[i][1].substring(8, 10)).equals("31")){
                        lat1 = mPoints[i][0];
                        thePoints1[i][0] = lat1;
                        lon1 = mPoints[i][1];
                        thePoints1[i][1] = lon1;
                    }
                }
            }
            paintMap(thePoints1, myDetails);
            
        }
    }
    
    private int[] monthPoints(ArrayList<String> date){
        int[] myMonths1 = new int[12];
        
        for(int i=0; i<date.size(); i++){
            if(date.get(i).substring(5,7).equals("01")){
                myMonths1[0]++;
            } else if(date.get(i).substring(5,7).equals("02")){
                myMonths1[1]++;
            } else if(date.get(i).substring(5,7).equals("03")){
                myMonths1[2]++;
            } else if(date.get(i).substring(5,7).equals("04")){
                myMonths1[3]++;
            } else if(date.get(i).substring(5,7).equals("05")){
                myMonths1[4]++;
            } else if(date.get(i).substring(5,7).equals("06")){
                myMonths1[5]++;
            } else if(date.get(i).substring(5,7).equals("07")){
                myMonths1[6]++;
            } else if(date.get(i).substring(5,7).equals("08")){
                myMonths1[7]++;
            } else if(date.get(i).substring(5,7).equals("09")){
                myMonths1[8]++;
            } else if(date.get(i).substring(5,7).equals("10")){
                myMonths1[9]++;
            } else if(date.get(i).substring(5,7).equals("11")){
                myMonths1[10]++;
            } else if(date.get(i).substring(5,7).equals("12")){
                myMonths1[11]++;
            }
        }
        
        return myMonths1;
    }
    
    private int[] dayPoints(ArrayList<String> date, int value){
        int[] myDays1 = new int[31];
        
        for(int i=0; i<date.size(); i++){
            if(date.get(i).substring(8, 10).equals("01") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[0]++;
            } else if(date.get(i).substring(8, 10).equals("02") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[1]++;
            } else if(date.get(i).substring(8, 10).equals("03") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[2]++;
            } else if(date.get(i).substring(8, 10).equals("04") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[3]++;
            } else if(date.get(i).substring(8, 10).equals("05") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[4]++;
            } else if(date.get(i).substring(8, 10).equals("06") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[5]++;
            } else if(date.get(i).substring(8, 10).equals("07") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[6]++;
            } else if(date.get(i).substring(8, 10).equals("08") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[7]++;
            } else if(date.get(i).substring(8, 10).equals("09") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[8]++;
            } else if(date.get(i).substring(8, 10).equals("10") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[9]++;
            } else if(date.get(i).substring(8, 10).equals("11") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[10]++;
            } else if(date.get(i).substring(8, 10).equals("12") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[11]++;
            } else if(date.get(i).substring(8, 10).equals("13")&& (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[12]++;
            } else if(date.get(i).substring(8, 10).equals("14") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[13]++;
            } else if(date.get(i).substring(8, 10).equals("15") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[14]++;
            } else if(date.get(i).substring(8, 10).equals("16") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[15]++;
            } else if(date.get(i).substring(8, 10).equals("17") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[16]++;
            } else if(date.get(i).substring(8, 10).equals("18") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[17]++;
            } else if(date.get(i).substring(8, 10).equals("19") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[18]++;
            } else if(date.get(i).substring(8, 10).equals("20") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[19]++;
            } else if(date.get(i).substring(8, 10).equals("21") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[20]++;
            } else if(date.get(i).substring(8, 10).equals("22") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[21]++;
            } else if(date.get(i).substring(8, 10).equals("23") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[22]++;
            } else if(date.get(i).substring(8, 10).equals("24") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[23]++;
            } else if(date.get(i).substring(8, 10).equals("25") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[24]++;
            } else if(date.get(i).substring(8, 10).equals("26") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[25]++;
            } else if(date.get(i).substring(8, 10).equals("27") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[26]++;
            } else if(date.get(i).substring(8, 10).equals("28") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[27]++;
            } else if(date.get(i).substring(8, 10).equals("29") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[28]++;
            } else if(date.get(i).substring(8, 10).equals("30") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[29]++;
            } else if(date.get(i).substring(8, 10).equals("31") && (Integer.toString(value).equals(date.get(i).substring(6,7)) || Integer.toString(value).equals(date.get(i).substring(5, 7)))){
                myDays1[30]++;
            } 
        }
        
        return myDays1;
    }
    
    private int[] hourPoints(ArrayList<String> date, int value, int value1){
        int[] myHours1 = new int[24];
        
        for(int i=0; i<date.size(); i++){
            if ((date.get(i).substring(5, 7).equals(Integer.toString(value))) || (date.get(i).substring(6, 7).equals(Integer.toString(value)) && date.get(i).substring(5, 6).equals("0"))){
                if((date.get(i).substring(8, 10).equals(Integer.toString(value1))) || (date.get(i).substring(9, 10).equals(Integer.toString(value1)) && date.get(i).substring(8, 9).equals("0"))){
                    if(date.get(i).substring(11,13).equals("00")){
                        myHours1[0]++;
                    } else if(date.get(i).substring(11,13).equals("01")){
                        myHours1[1]++;
                    } else if(date.get(i).substring(11,13).equals("02")){
                        myHours1[2]++;
                    } else if(date.get(i).substring(11,13).equals("03")){
                        myHours1[3]++;
                    } else if(date.get(i).substring(11,13).equals("04")){
                        myHours1[4]++;
                    } else if(date.get(i).substring(11,13).equals("05")){
                        myHours1[5]++;
                    } else if(date.get(i).substring(11,13).equals("06")){
                        myHours1[6]++;
                    } else if(date.get(i).substring(11,13).equals("07")){
                        myHours1[7]++;
                    } else if(date.get(i).substring(11,13).equals("08")){
                        myHours1[8]++;
                    } else if(date.get(i).substring(11,13).equals("09")){
                        myHours1[9]++;
                    } else if(date.get(i).substring(11,13).equals("10")){
                        myHours1[10]++;
                    } else if(date.get(i).substring(11,13).equals("11")){
                        myHours1[11]++;
                    } else if(date.get(i).substring(11,13).equals("12")){
                        myHours1[12]++;
                    } else if(date.get(i).substring(11,13).equals("13")){
                        myHours1[13]++;
                    } else if(date.get(i).substring(11,13).equals("14")){
                        myHours1[14]++;
                    } else if(date.get(i).substring(11,13).equals("15")){
                        myHours1[15]++;
                    } else if(date.get(i).substring(11,13).equals("16")){
                        myHours1[16]++;
                    } else if(date.get(i).substring(11,13).equals("17")){
                        myHours1[17]++;
                    } else if(date.get(i).substring(11,13).equals("18")){
                        myHours1[18]++;
                    } else if(date.get(i).substring(11,13).equals("19")){
                        myHours1[19]++;
                    } else if(date.get(i).substring(11,13).equals("20")){
                        myHours1[20]++;
                    } else if(date.get(i).substring(11,13).equals("21")){
                        myHours1[21]++;
                    } else if(date.get(i).substring(11,13).equals("22")){
                        myHours1[22]++;
                    } else if(date.get(i).substring(11,13).equals("23")){
                        myHours1[23]++;
                    }
                }
            }
        }
        
        return myHours1;
    }
    
    public void enableControls(boolean isEnabled) {

        jSlider1.setEnabled(isEnabled);
        jTextField1.setEnabled(isEnabled);
        jButton1.setEnabled(isEnabled);
        jComboBox2.setEnabled(isEnabled);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScroll_network;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JTextField jTextField1;
    public static org.jdesktop.swingx.JXMapKit jXMapKit1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private java.awt.ScrollPane scrollPane1;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    
    MySQLAccess test = new MySQLAccess();
    
    RAMDirectory idx;
    
    String[][] myResults;
    Integer[][] myResults1;
    String[][] myDetails1;
    String[][] myTweets;
    public static String[] popularWords;
    double [][] points;
    int[] myMonths = new int[12];
    int[] myHours = new int[24];
    int[] myDays = new int[31];
    Integer[] col;
    
    String myInput;
    String mySearch;
    String popular;
    String mypopular;
    public static String mySelection;
    public static String disText;
    int iValue;
    int iValue1;
    int count = 0;
    public static int jcombo = 0;
    public static int my2 = 0;
    public static int my1 = 0;
    public static int col1;
    public static int count1;
            
    ArrayList<String> mySel1 = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> wordIn = new ArrayList<String>();
    ArrayList<String> wordIn1 = new ArrayList<String>();
    ArrayList<Double> lat = new ArrayList<Double>();
    ArrayList<Double> lon = new ArrayList<Double>();
    ArrayList<Integer> monthPoints;
    ArrayList<Integer> dayPoints;
    ArrayList<Integer> hourPoints;
    
    Random randomGenerator = new Random();
}




class mySelection implements ActionListener{
    public void actionPerformed(ActionEvent e) {
    }
}