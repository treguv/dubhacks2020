/*
 * SOURCES USED TO HELP CREATE THIS CLASS
 * https://pdfbox.apache.org/
 * https://stackoverflow.com/questions/32608083/not-able-to-read-the-exact-text-highlighted-across-the-lines
 */

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The HighlightParser class is a utility class used to get highlighted text
 * from pdf files in a directory
 */
public class HighlightParser {

    /**
     * Returns a List of Highlight classes containing highlights from the pdf
     *  files found in the given directory file
     *
     * @param theDirectory the directory that stores only pdf files
     * @return a ArrayList of Highlights that contains the highlights from
     * the files in theDirectory
     */
    public static ArrayList<Highlight> getDirectoryHighlights(File theDirectory) {
        // array of file-paths to be checked
        String[] filePaths  = getFilePaths(theDirectory);

        // stored highlights for the whole directory
        ArrayList<Highlight> directoryHighlights = new ArrayList<>();

        // aux storage for highlights from a single file
        ArrayList<Highlight> fileHighlights;

        // loop through each file in the directory
        for (String currFilePath : filePaths) {
            try {
                // get the highlights from the current file-path
                fileHighlights = getFileHighlights(currFilePath);

                // check and see if there are existing highlights in
                // directoryHighlights that are in the current fileHighlights
                // If there is, increment the count in the
                // directoryHighlight and remove the fileHighlight
                for (Highlight directoryHighlight : directoryHighlights) {
                    for (Highlight fileHighlight : fileHighlights) {
                        // compare the highlights
                        if (directoryHighlight.compare(fileHighlight)) {
                            // the highlights are the same, so increment the
                            // directory highlight and remove the fileHighlight
                            directoryHighlight.increaseCount();
                            fileHighlights.remove(fileHighlight);
                            break;
                        }
                    }
                }

                // the fileHighlights remaining are all unique and not inside
                // the directoryHighlights list, so add them.
                for (Highlight fileHighlight : fileHighlights) {
                    directoryHighlights.add(fileHighlight);
                }
            } catch (Exception e) {
                System.out.println("error trying to add highlight from " + currFilePath);
            }
        }
//        System.out.println("PRINTING HIGHLIGHTS LIST");
        for (Highlight h : directoryHighlights) {
//            System.out.println(h);
        }
        return directoryHighlights;
    }

    /**
     * Returns an Array of String file-paths from the given directory
     * @param theDirectory the directory the file-paths are from
     * @return an Array of String file-paths
     */
    private static String[] getFilePaths(File theDirectory) {
        // the list of files
        File[] fileList = theDirectory.listFiles();
        // the list of file-paths
        String[] filePaths = new String[fileList.length];
        int index = 0;

        // add the file-paths to the array
        for (File currFile : fileList) {
            filePaths[index] = currFile.getPath();
            index++;
        }
        return filePaths;
    }

    /**
     * Returns an ArrayList of Highlights from the given file-path
     * TODO LINK GOES HERE
     * @param theFilePath the file-path for the file we want the highlights from
     * @throws IOException if the File is not found
     */
    private static ArrayList<Highlight> getFileHighlights(String theFilePath) throws IOException {
        // stores the highlights from this file
        ArrayList<Highlight> fileHighlights = new ArrayList<>();

        PDDocument document = PDDocument.load(new File(theFilePath));
        // add all of the pages in the pdf document to a list
        List<PDPage> pageList = new ArrayList<>();
        for (PDPage currPage : document.getDocumentCatalog().getPages()) {
            pageList.add(currPage);
        }

        int pageNumber = 0;
        // get a page
        PDPage currPage = pageList.get(pageNumber);

        // get the annotations from the page
        List<PDAnnotation> annotations = currPage.getAnnotations();

        // loop through each annotation
        for (PDAnnotation annotation : annotations) {
            // ensure that the annotation is a highlight
            if (annotation.getSubtype().equals("Highlight")) {
                // extract the highlighted text
                PDFTextStripperByArea stripperByArea = new PDFTextStripperByArea();

                COSArray quadsArray =
                        (COSArray) annotation.getCOSObject().getDictionaryObject(COSName.getPDFName("QuadPoints"));

                String highlightedString = null;

                for (int j = 1, k = 0; j <= (quadsArray.size() / 8); j++) {
                    COSFloat ULX = (COSFloat) quadsArray.get(0 + k);
                    COSFloat ULY = (COSFloat) quadsArray.get(1 + k);
                    COSFloat URX = (COSFloat) quadsArray.get(2 + k);
                    COSFloat URY = (COSFloat) quadsArray.get(3 + k);
                    COSFloat LLX = (COSFloat) quadsArray.get(4 + k);
                    COSFloat LLY = (COSFloat) quadsArray.get(5 + k);
                    COSFloat LRX = (COSFloat) quadsArray.get(6 + k);
                    COSFloat LRY = (COSFloat) quadsArray.get(7 + k);

                    k += 8;

                    float ulx = ULX.floatValue() - 1;                           // upper left x.
                    float uly = ULY.floatValue();                               // upper left y.
                    float width = URX.floatValue() - LLX.floatValue();          // calculated by upperRightX - lowerLeftX.
                    float height = URY.floatValue() - LLY.floatValue();         // calculated by upperRightY - lowerLeftY.

                    PDRectangle pageSize = currPage.getMediaBox();
                    uly = pageSize.getHeight() - uly;

                    Rectangle2D.Float rectangle_2 = new Rectangle2D.Float(ulx, uly, width, height);
                    stripperByArea.addRegion("highlightedRegion", rectangle_2);
                    stripperByArea.extractRegions(currPage);
                    String highlightedText = stripperByArea.getTextForRegion("highlightedRegion");

                    if (j > 1) {
                        highlightedString =
                                highlightedString.concat(highlightedText);
                    } else {
                        highlightedString = highlightedText;
                    }

                    // make a highlight from each line of the highlightedString
                    String[] lines = highlightedString.split("\\r?\\n");
                    for (String s : lines) {
                        Highlight newHighlight = new Highlight(s);
                        fileHighlights.add(newHighlight);
                    }
//                    System.out.println(Arrays.toString(lines));
                }
            }
        }
//        System.out.println("Highlights from " + theFilePath + ":");
//        System.out.println(fileHighlights.toString());
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println();
        document.close();

        return fileHighlights;
    }

}

// (TO BE DELETED, KEPT FOR REFERENCE)
//
//    /**
//     * Gets the highlighed text in the file and returns it in an arraylist
//     * @param filePath Path of the file to get highlights from
//     * @param pageNumber the page number to check
//     * @return An array list that contains the highlights
//     * @throws IOException
//     */
//    public static ArrayList<Highlight> getHighlightedText(String filePath,
//     int pageNumber) throws IOException {
//        ArrayList<String> highlightedTexts = new ArrayList<>();
//        // this is the in-memory representation of the PDF document.
//        // this will load a document from a file.
//
////        PDDocument document = PDDocument.load(new File(filePath));
//        // this represents all pages in a PDF document.
////        List<PDPage> allPages = new ArrayList<>();
////        document.getDocumentCatalog().getPages().forEach(allPages::add);
//        // this represents a single page in a PDF document.
////        PDPage page = allPages.get(pageNumber);
////        // get  annotation dictionaries
////        List<PDAnnotation> annotations = page.getAnnotations();
//
//        for (int i = 0; i < annotations.size(); i++) {
//            // check subType
//            if (annotations.get(i).getSubtype().equals("Highlight")) {
//                // extract highlighted text
//                PDFTextStripperByArea stripperByArea = new PDFTextStripperByArea();
//
//                COSArray quadsArray = (COSArray) annotations.get(i).getCOSObject().getDictionaryObject(COSName.getPDFName("QuadPoints"));
//                String str = null;
//
//                for (int j = 1, k = 0; j <= (quadsArray.size() / 8); j++) {
//
//                    COSFloat ULX = (COSFloat) quadsArray.get(0 + k);
//                    COSFloat ULY = (COSFloat) quadsArray.get(1 + k);
//                    COSFloat URX = (COSFloat) quadsArray.get(2 + k);
//                    COSFloat URY = (COSFloat) quadsArray.get(3 + k);
//                    COSFloat LLX = (COSFloat) quadsArray.get(4 + k);
//                    COSFloat LLY = (COSFloat) quadsArray.get(5 + k);
//                    COSFloat LRX = (COSFloat) quadsArray.get(6 + k);
//                    COSFloat LRY = (COSFloat) quadsArray.get(7 + k);
//
//                    k += 8;
//
//                    float ulx = ULX.floatValue() - 1;                           // upper left x.
//                    float uly = ULY.floatValue();                               // upper left y.
//                    float width = URX.floatValue() - LLX.floatValue();          // calculated by upperRightX - lowerLeftX.
//                    float height = URY.floatValue() - LLY.floatValue();         // calculated by upperRightY - lowerLeftY.
//
//                    PDRectangle pageSize = page.getMediaBox();
//                    uly = pageSize.getHeight() - uly;
//
//                    Rectangle2D.Float rectangle_2 = new Rectangle2D.Float(ulx, uly, width, height);
//                    stripperByArea.addRegion("highlightedRegion", rectangle_2);
//                    stripperByArea.extractRegions(page);
//                    String highlightedText = stripperByArea.getTextForRegion("highlightedRegion");
//
//                    if (j > 1) {
//                        str = str.concat(highlightedText);
//                    } else {
//                        str = highlightedText;
//                    }
//                }
//                highlightedTexts.add(str);
//            }
//        }
//        document.close();
//        return highlightedTexts;
//    }
//
//
//    public ArrayList<String> getMyHighlights(){
//        return myHighlights;
//    }
//    /**
//     *  Return string representation of the object
//     * @return string representation of the object
//     */
//    public String toString(){
//        return myHighlights.toString();
//    }
