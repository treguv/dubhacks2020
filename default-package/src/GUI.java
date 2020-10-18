/*
 * (#1)I used this method for File Chooser help
 * https://www.tutorialspoint.com/swingexamples/show_file_chooser_directory_only.htm
 *
 *
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class GUI {

    /**
     * Constructor
     */
    public GUI() {
        final JFrame myFrame = new JFrame();
        final JPanel myPanel = new JPanel();

        // button
        final JButton myButton = new JButton("Select Directory");
        myButton.setFocusPainted(false);
        // button action when pressed
        myButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // much of this code came from link #1
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(myFrame);

                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
//                    System.out.println("Directory selected: " + selectedFile.getName());
                    iterateOverDirectory(selectedFile);
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
     * Iterates over the files in the given directory
     * @param theDirectory the directory that stores the files to be iterated
     * over
     */
    private static void iterateOverDirectory(File theDirectory) {
        File[] fileList = theDirectory.listFiles();
        System.out.println("Printing all of the file names in " + theDirectory.getName() + ":");
        for (File currFile : fileList) {
            System.out.println(currFile.getName());
        }
    }

    // runs the program
    public static void main(String[] args) {
        new GUI();
    }
}
