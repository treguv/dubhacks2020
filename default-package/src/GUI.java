/*
 * SOURCES USED TO HELP CREATE THIS CLASS
 * (#1)I used this method for File Chooser help
 * https://www.tutorialspoint.com/swingexamples/show_file_chooser_directory_only.htm
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * The GUI class displays a Frame on the screen and takes user input. Once
 * the user selects a directory, a pdf is made with the common highlights
 * from the directory
 */
public class GUI {
    // runs the program
    public static void main(String[] args) {
        new GUI();
    }

    /**
     * Constructor
     */
    public GUI() {
        final JFrame myFrame = new JFrame("PDF Highlight Combiner DLX");
        final JPanel myPanel = new JPanel();
        final String[] originalFilePath = new String[1];

        JLabel instructionsLabel = new JLabel("Select the original pdf " +
                "document");

        final JButton selectDirectory = new JButton("Select Directory");
        selectDirectory.setFocusPainted(false);
        selectDirectory.setVisible(false);
        selectDirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // much of this code came from link #1
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(myFrame);

                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        printHighlightsFromDirectory(selectedFile,
                         originalFilePath[0]);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    selectDirectory.setVisible(false);
                    instructionsLabel.setText("Check for a result pdf file " +
                     "with the results of the program!");
                }
            }
        });

        final JButton selectOriginalFile = new JButton("Select Original File");
        selectOriginalFile.setFocusPainted(false);
        selectOriginalFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // much of this code came from link #1
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int option = fileChooser.showOpenDialog(myFrame);

                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    originalFilePath[0] = selectedFile.getPath();
                }
                selectDirectory.setVisible(true);
                selectOriginalFile.setVisible(false);
                instructionsLabel.setText("Select the directory of identical pdf" +
                        " files with highlights");
            }
        });

        // add the button to the panel and add the panel to frame
        myPanel.add(instructionsLabel);
        myPanel.add(selectDirectory);
        myPanel.add(selectOriginalFile);
        myFrame.getContentPane().add(myPanel, BorderLayout.CENTER);

        // set frame settings
        myFrame.setSize(500, 500);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setVisible(true);
    }

    /**
     * Prints the highlights from the files in the given directory on the
     * console
     * @param theDirectory the directory the highlights are from
     */
    private void printHighlightsFromDirectory(File theDirectory,
     String theOriginalFilePath) throws FileNotFoundException {
        ArrayList<Highlight> highlightList = HighlightParser.getDirectoryHighlights(theDirectory);

         HighlightPdf.createHighlightDocument(theOriginalFilePath,
                 highlightList);

//        for testing purposes:
//        Highlight test = new Highlight(" eget");
//        Highlight test2 = new Highlight("nulla");
//        ArrayList<Highlight> theArray = new ArrayList<Highlight>();
//        theArray.add(test);
//        theArray.add(test2);

//        System.out.println("Highlights from the whole directory:");
//        for (Highlight h : highlightList) {
//            System.out.println(h);
//            System.out.println();
//            System.out.println();
//        }

    }

}
