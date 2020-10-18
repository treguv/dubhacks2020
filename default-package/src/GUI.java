/*
 * SOURCES USED TO HELP CREATE THIS CLASS
 * TODO
 * (#1)I used this method for File Chooser help
 * https://www.tutorialspoint.com/swingexamples/show_file_chooser_directory_only.htm
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * TODO This comment sucks
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
        final JFrame myFrame = new JFrame();
        final JPanel myPanel = new JPanel();

        // Set up the button
        final JButton myButton = new JButton("Select Directory");
        myButton.setFocusPainted(false);
        myButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // much of this code came from link #1
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(myFrame);

                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    printHighlightsFromDirectory(selectedFile);
                }
            }
        });

        // add the button to the panel and add the panel to frame
        myPanel.add(myButton);
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
    private void printHighlightsFromDirectory(File theDirectory) {
        ArrayList<Highlight> highlightList =
            HighlightParser.getDirectoryHighlights(theDirectory);
        System.out.println("Highlights from the whole directory:");
        for (Highlight h : highlightList) {
            System.out.println(h);
            System.out.println();
            System.out.println();
        }
    }

}
