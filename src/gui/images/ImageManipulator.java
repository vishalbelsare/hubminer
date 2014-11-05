/**
* Hub Miner: a hubness-aware machine learning experimentation library.
* Copyright (C) 2014  Nenad Tomasev. Email: nenad.tomasev at gmail.com
* 
* This program is free software: you can redistribute it and/or modify it under
* the terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
* 
* This program is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
* FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License along with
* this program. If not, see <http://www.gnu.org/licenses/>.
*/
package gui.images;

import data.representation.images.sift.LFeatRepresentation;
import distances.primary.CombinedMetric;
import draw.basic.RotatedEllipse;
import images.mining.clustering.SingleImageSIFTClusterer;
import images.mining.display.SIFTDraw;
import images.mining.segmentation.SRMSegmentation;
import ioformat.FileUtil;
import ioformat.images.ConvertJPGToPGM;
import ioformat.images.SiftUtil;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import learning.unsupervised.Cluster;
import learning.unsupervised.evaluation.quality.OptimalConfigurationFinder;
import statistics.Variance2D;

/**
 * This class implements a simple GUI for SIFT feature extraction and
 * visualization as well as segmentation - on a single image.
 * 
 * @author Nenad Tomasev <nenad.tomasev at gmail.com>
 */
public class ImageManipulator extends javax.swing.JFrame {

    private File currentDirectory = new File(".");
    private File currentInFile = null;
    private File currentOutFile = null;
    // Temporary directory for generating PGM files for SIFT feature extraction
    // via SiftWin.
    private File temporaryDir = new File("." + File.separator +
            "temporary_directory");
    private File temporaryPGM = new File(temporaryDir, "temporaryPGM.pgm");
    private File originalImageFile = null;

    // BufferedImage objects for different display options.
    private BufferedImage currentImage = null;
    private BufferedImage currentModifiedImage = null;
    private BufferedImage siftImage = null;
    private BufferedImage clusteredImage = null;
    private BufferedImage segmentedImage = null;
    private BufferedImage ellipsesImage = null;

    // SIFT feature representation.
    private LFeatRepresentation currentSIFT = null;
    // SIFT feature clusters.
    private Cluster[] clusters = null;
    // Ellipses corresponding to the SIFT feature clusters.
    private RotatedEllipse[] ellipses = null;

    private int currProgressPercentage = 0;

    private Timer timer;


    /**
     * Initialization.
     */
    private void initialization() {
        try {
            FileUtil.createDirectory(temporaryDir);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        this.setTitle("Image manipulator v1.0");
        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setOrientation(JProgressBar.HORIZONTAL);
        progressBar.setForeground(Color.ORANGE);
    }

    /** Creates new form ImageManipulator */
    public ImageManipulator() {
        initComponents();
        initialization();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        originalImagePanel = new gui.images.ImagePanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        modifiedImagePanel = new gui.images.ImagePanel();
        siftButton = new java.awt.Button();
        clustersButton = new java.awt.Button();
        ellipsesButton = new java.awt.Button();
        segmentButton = new java.awt.Button();
        label1 = new java.awt.Label();
        statusLabel = new java.awt.Label();
        progressBar = new javax.swing.JProgressBar();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        fileMenu.setMnemonic(KeyEvent.VK_F);
        loadImageItem = new javax.swing.JMenuItem();
        loadImageItem.setMnemonic(KeyEvent.VK_L);
        saveImageItem = new javax.swing.JMenuItem();
        saveImageItem.setMnemonic(KeyEvent.VK_S);
        featuresMenu = new javax.swing.JMenu();
        featuresMenu.setMnemonic(KeyEvent.VK_E);
        siftCalculationItem = new javax.swing.JMenuItem();
        siftCalculationItem.setMnemonic(KeyEvent.VK_F);
        clusterFeaturesMenuItem = new javax.swing.JMenuItem();
        clusterFeaturesMenuItem.setMnemonic(KeyEvent.VK_C);
        segmentMenu = new javax.swing.JMenu();
        segmentMenu.setMnemonic(KeyEvent.VK_S);
        srmSegmentItem = new javax.swing.JMenuItem();
        srmSegmentItem.setMnemonic(KeyEvent.VK_S);
        aboutMenu = new javax.swing.JMenu();
        aboutMenu.setMnemonic(KeyEvent.VK_A);
        infoItem = new javax.swing.JMenuItem();
        infoItem.setMnemonic(KeyEvent.VK_I);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        org.jdesktop.layout.GroupLayout originalImagePanelLayout = new org.jdesktop.layout.GroupLayout(originalImagePanel);
        originalImagePanel.setLayout(originalImagePanelLayout);
        originalImagePanelLayout.setHorizontalGroup(
            originalImagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 550, Short.MAX_VALUE)
        );
        originalImagePanelLayout.setVerticalGroup(
            originalImagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 388, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(originalImagePanel);

        org.jdesktop.layout.GroupLayout modifiedImagePanelLayout = new org.jdesktop.layout.GroupLayout(modifiedImagePanel);
        modifiedImagePanel.setLayout(modifiedImagePanelLayout);
        modifiedImagePanelLayout.setHorizontalGroup(
            modifiedImagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 568, Short.MAX_VALUE)
        );
        modifiedImagePanelLayout.setVerticalGroup(
            modifiedImagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 388, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(modifiedImagePanel);

        siftButton.setLabel("SIFT features");
        siftButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siftButtonActionPerformed(evt);
            }
        });

        clustersButton.setLabel("Clusters/SIFT");
        clustersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clustersButtonActionPerformed(evt);
            }
        });

        ellipsesButton.setLabel("Clusters/Ellipses");
        ellipsesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ellipsesButtonActionPerformed(evt);
            }
        });

        segmentButton.setLabel("Segmented Image");
        segmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                segmentButtonActionPerformed(evt);
            }
        });

        label1.setText("Execution Status:");

        statusLabel.setText("Idle");

        fileMenu.setText("<html><u>F</u>ile");

        loadImageItem.setText("<html><u>L</u>oad image");
        loadImageItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadImageItemActionPerformed(evt);
            }
        });
        fileMenu.add(loadImageItem);

        saveImageItem.setText("<html><u>S</u>ave selected image");
        saveImageItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveImageItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveImageItem);

        menuBar.add(fileMenu);

        featuresMenu.setText("<html>F<u>e</u>atures");

        siftCalculationItem.setText("<html><u>F</u>ind SIFT features");
        siftCalculationItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siftCalculationItemActionPerformed(evt);
            }
        });
        featuresMenu.add(siftCalculationItem);

        clusterFeaturesMenuItem.setText("<html><u>C</u>luster features");
        clusterFeaturesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clusterFeaturesMenuItemActionPerformed(evt);
            }
        });
        featuresMenu.add(clusterFeaturesMenuItem);

        menuBar.add(featuresMenu);

        segmentMenu.setText("<html><u>S</u>egment");

        srmSegmentItem.setText("<html><u>S</u>RM method");
        srmSegmentItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                srmSegmentItemActionPerformed(evt);
            }
        });
        segmentMenu.add(srmSegmentItem);

        menuBar.add(segmentMenu);

        aboutMenu.setText("<html><u>A</u>bout");

        infoItem.setText("<html><u>I</u>nfo");
        infoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoItemActionPerformed(evt);
            }
        });
        aboutMenu.add(infoItem);

        menuBar.add(aboutMenu);

        setJMenuBar(menuBar);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 554, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(label1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(statusLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 446, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 554, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(siftButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(38, 38, 38)
                        .add(clustersButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 116, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(33, 33, 33)
                        .add(ellipsesButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 42, Short.MAX_VALUE)
                        .add(segmentButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 119, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 392, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(clustersButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                            .add(ellipsesButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                            .add(siftButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, segmentButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE))
                        .add(68, 68, 68))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, statusLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, label1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    /**
     * Re-initialize the internal structures and counters.
     */
    public void reinitialize() {
        currentModifiedImage = null;
        siftImage = null;
        clusteredImage = null;
        segmentedImage = null;
        ellipsesImage = null;
        currentSIFT = null;
        clusters = null;
        ellipses = null;
        disableButton(segmentButton);
        disableButton(ellipsesButton);
        disableButton(clustersButton);
        disableButton(siftButton);
        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setOrientation(JProgressBar.HORIZONTAL);
        progressBar.setForeground(Color.ORANGE);
    }


    /**
     * Disables the button while calculating.
     * @param button Button object to disable.
     */
    public static void disableButton(java.awt.Button button) {
        button.setEnabled(false);
    }


    /**
     * Enables the button after finishing calculations.
     * @param button Button object to enable.
     */
    public static void enableButton(java.awt.Button button) {
        button.setEnabled(true);
    }


    /**
     * Loads the specified image.
     * @param evt ActionEvent object.
     */
    private void loadImageItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadImageItemActionPerformed
        JFileChooser jfc = new JFileChooser(currentDirectory);
        int rVal = jfc.showOpenDialog(ImageManipulator.this);
	if (rVal == JFileChooser.APPROVE_OPTION) {
            currentInFile = jfc.getSelectedFile();
            originalImageFile = currentInFile;
            currentDirectory = currentInFile.getParentFile();
            try {
                statusLabel.setText("Reading image");
                currentImage = ImageIO.read(currentInFile);
                originalImagePanel.setImage(currentImage);
                reinitialize();
                statusLabel.setText("Image loaded");
            } catch (Exception e) {
                statusLabel.setText(e.getMessage());
            }
        }
    }//GEN-LAST:event_loadImageItemActionPerformed


    /**
     * Perform SRM image segmentation.
     * @param evt ActionEvent object.
     */
    private void srmSegmentItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_srmSegmentItemActionPerformed
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "You must first load an image",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new ProgressUpdater(), 1500, 1500);
        currProgressPercentage = 1;
        // Perform the SRM image segmentation.
        SRMSegmentation seg = new SRMSegmentation(currentImage);
        statusLabel.setText("Segmenting the image...");
        try {
            seg.segment();
            currProgressPercentage = 98;
        } catch (Exception e) {
            statusLabel.setText("Error occured: " + e.getMessage());
            return;
        }
        // Generate the segmented image for display.
        Image img = seg.getSegmentedImage();
        segmentedImage = new BufferedImage(img.getWidth(null),
                img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        segmentedImage.getGraphics().drawImage(img, 0 , 0, null);
        currentModifiedImage = segmentedImage;
        modifiedImagePanel.setImage(segmentedImage);
        currProgressPercentage = 100;
        enableButton(segmentButton);
        // Start some clean up.
        System.gc();
        timer = null;
    }//GEN-LAST:event_srmSegmentItemActionPerformed


    /**
     * Saves the image to a file.
     * @param evt ActionEvent object.
     */
    private void saveImageItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveImageItemActionPerformed
        JFileChooser jfc = new JFileChooser(currentDirectory);
        int rVal = jfc.showSaveDialog(ImageManipulator.this);
	if (rVal == JFileChooser.APPROVE_OPTION) {
            currentOutFile = jfc.getSelectedFile();
            currentDirectory = currentOutFile.getParentFile();
            try {
                statusLabel.setText("Saving image to: " +
                        currentOutFile.getPath());
                ImageIO.write(currentModifiedImage, "JPG", currentOutFile);
                statusLabel.setText("Image succesfully saved to: " +
                        currentOutFile.getPath());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_saveImageItemActionPerformed


    /**
     * Display the segmented image.
     * @param evt ActionEvent object.
     */
    private void segmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_segmentButtonActionPerformed
        currentModifiedImage = segmentedImage;
        modifiedImagePanel.setImage(segmentedImage);
    }//GEN-LAST:event_segmentButtonActionPerformed


    /**
     * Display the image where the SIFT feature clusters are visualized as
     * ellipses that correspond to the principle components of variance.
     * @param evt ActionEvent object.
     */
    private void ellipsesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ellipsesButtonActionPerformed
        currentModifiedImage = ellipsesImage;
        modifiedImagePanel.setImage(ellipsesImage);
    }//GEN-LAST:event_ellipsesButtonActionPerformed


    /**
     * Displays the image with the SIFT feature clusters on top.
     * @param evt ActionEvent object.
     */
    private void clustersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clustersButtonActionPerformed
        currentModifiedImage = clusteredImage;
        modifiedImagePanel.setImage(clusteredImage);
    }//GEN-LAST:event_clustersButtonActionPerformed


    /**
     * Displays the image with the SIFT features shown on top.
     * @param evt ActionEvent object.
     */
    private void siftButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siftButtonActionPerformed
        currentModifiedImage = siftImage;
        modifiedImagePanel.setImage(siftImage);
    }//GEN-LAST:event_siftButtonActionPerformed


    /**
     * Calculates the SIFT features for the image via SiftWin.
     * @param evt ActionEvent object.
     */
    private void siftCalculationItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siftCalculationItemActionPerformed
        JFileChooser jfc = new JFileChooser(currentDirectory);
        int rVal = jfc.showSaveDialog(ImageManipulator.this);
	if (rVal == JFileChooser.APPROVE_OPTION) {
            currentOutFile = jfc.getSelectedFile();
            currentDirectory = currentOutFile.getParentFile();
            if (!(currentOutFile.getPath().endsWith(".key"))) {
                currentOutFile = new File(currentDirectory,
                        currentOutFile.getName() + ".key");
            }
            try {
                statusLabel.setText("Calculating sift features");
                if (currentImage == null) {
                    JOptionPane.showMessageDialog(this,
                            "You must first load an image", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                timer = new Timer(true);
                timer.scheduleAtFixedRate(new ProgressUpdater(), 1500, 1500);
                currProgressPercentage = 3;
                statusLabel.setText("Converting to PGM for"
                        + "SIFT calculations...");
                // Generate a temporary PGM file for SiftWin to work on.
                ConvertJPGToPGM.convertFile(originalImageFile, temporaryPGM);
                currProgressPercentage = 45;
                statusLabel.setText("Converted to PGM...");
                statusLabel.setText("Finding features...");
                // Extract the SIFT features.
                SiftUtil.siftFile(temporaryPGM, currentOutFile, "");
                currProgressPercentage = 90;
                temporaryPGM.delete();
                // Load the extracted features from a file.
                currentSIFT = SiftUtil.importFeaturesFromSift(currentOutFile);
                currProgressPercentage = 95;
                // Visualize the loaded features on the image.
                siftImage = SIFTDraw.drawSIFTImage(currentSIFT, currentImage);
                currentModifiedImage = siftImage;
                modifiedImagePanel.setImage(siftImage);
                currProgressPercentage = 100;
                // Enable the buttons once again.
                enableButton(siftButton);
                statusLabel.setText("Features calculations completed");
                statusLabel.setText("features succesfully saved to: " +
                        currentOutFile.getPath());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            timer = null;
        }
    }//GEN-LAST:event_siftCalculationItemActionPerformed


    /**
     * Invoke SIFT feature clutering.
     * @param evt ActionEvent object.
     */
    private void clusterFeaturesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clusterFeaturesMenuItemActionPerformed
        if (currentSIFT == null) {
            JOptionPane.showMessageDialog(this,
                    "You must first calculate the features",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new ProgressUpdater(), 1500, 1500);
        currProgressPercentage = 1;
        MinMaxClustersDialog.showDialog(this);
    }//GEN-LAST:event_clusterFeaturesMenuItemActionPerformed


    /**
     * This method prints out the information about this GUI component.
     * @param evt ActionEvent object.
     */
    private void infoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoItemActionPerformed
        JOptionPane.showMessageDialog(this, "<html>Perform some actions on a "
                + "single image. Detect SIFT features, cluster them to "
                + "represent objects or perform segmentation. <br> Made by "
                + "Nenad Tomasev (nenad.tomasev@gmail.com), April 2010" ,
                "Info" ,
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_infoItemActionPerformed

    /**
    * @param args The command line parameters.
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ImageManipulator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu aboutMenu;
    private javax.swing.JMenuItem clusterFeaturesMenuItem;
    private java.awt.Button clustersButton;
    private java.awt.Button ellipsesButton;
    private javax.swing.JMenu featuresMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem infoItem;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private java.awt.Label label1;
    private javax.swing.JMenuItem loadImageItem;
    private javax.swing.JMenuBar menuBar;
    private gui.images.ImagePanel modifiedImagePanel;
    private gui.images.ImagePanel originalImagePanel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem saveImageItem;
    private java.awt.Button segmentButton;
    private javax.swing.JMenu segmentMenu;
    private java.awt.Button siftButton;
    private javax.swing.JMenuItem siftCalculationItem;
    private javax.swing.JMenuItem srmSegmentItem;
    private java.awt.Label statusLabel;
    // End of variables declaration//GEN-END:variables


    /**
     * Initiates a clustering sequence by trying all target cluster numbers
     * between the specified minimal and maximal cluster numbers.
     * @param minClusters Integer that is the minimal number of clusters to be
     * tried.
     * @param maxClusters Integer that is the maximal number of clusters to be
     * tried.
     */
    public void setForClustering (int minClusters, int maxClusters) {
        SingleImageSIFTClusterer clusterer = new SingleImageSIFTClusterer();
        clusterer.setRepresentation(currentSIFT);
        clusterer.setCombinedMetric(CombinedMetric.FLOAT_EUCLIDEAN);
        clusterer.setBounds(minClusters, maxClusters);
        // The cluster configuration will be selected via Dunn index.
        clusterer.setConfigurationSelector(new OptimalConfigurationFinder(
                OptimalConfigurationFinder.DUNN_INDEX));
        try {
            statusLabel.setText("Clustering...");
            clusters = clusterer.clusterImage(currentImage, true);
            // Depending on how many configurations there were, the following
            // steps comprise a different percentage of the total progess.
            switch (maxClusters - minClusters) {
                case 0: currProgressPercentage = 20; break;
                case 1: currProgressPercentage = 29; break;
                case 2: currProgressPercentage = 36; break;
                case 3: currProgressPercentage = 42; break;
                case 4: currProgressPercentage = 47; break;
                case 5: currProgressPercentage = 52; break;
                case 6: currProgressPercentage = 57; break;
                case 7: currProgressPercentage = 62; break;
                case 8: currProgressPercentage = 66; break;
                default: currProgressPercentage = 70; break;

            }
            // Calculate the principal components for all the clusters.
            Variance2D var = new Variance2D();
            statusLabel.setText("Searching for variance ellipsoids...");
            ellipses = var.findVarianceEllipseForSIFTCLusterConfiguration(
                    clusters);
            currProgressPercentage = 90;
            statusLabel.setText("Ellipsoids calculated");
            statusLabel.setText("Drawing clustered features...");
            // Generate the image with the SIFT feature clusters on top.
            clusteredImage = SIFTDraw.drawClusteredSIFTImage(
                    clusters, currentImage);
            currentModifiedImage = clusteredImage;
            modifiedImagePanel.setImage(clusteredImage);
            enableButton(clustersButton);
            currProgressPercentage = 95;
            statusLabel.setText("Clustered features drawn...");
            statusLabel.setText("Drawing ellipses...");
            // Generate the image with the SIFT feature clusters represented as
            // ellipses on top.
            ellipsesImage = SIFTDraw.drawClusterEllipsesOnImage(
                    ellipses, currentImage, true);
            currentModifiedImage = ellipsesImage;
            modifiedImagePanel.setImage(ellipsesImage);
            enableButton(ellipsesButton);
            statusLabel.setText("Ellipses drawn...");
            currProgressPercentage = 100;
            statusLabel.setText("feature clustering completed");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            System.err.println(e.getMessage());
        }
        timer = null;
    }

    // Timer for updating the progress bar.
    private class ProgressUpdater extends TimerTask {
        @Override
        public void run() {            
            progressBar.setValue(currProgressPercentage);
        }
    }
}
