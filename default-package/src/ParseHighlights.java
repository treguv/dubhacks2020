//Import all dependencies
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
 * @ author _________
 */

/**
 * Returns an array list containing all the highlighted text in a given document
 */
public class ParseHighlights {
    // class fields
    private ArrayList<String > myHighlights;
    /**
     * 1 param constructor
     *
     * @param theFilePath the path to the file to be parsed for hightlights
     */
    public ParseHighlights(String theFilePath) {
        ArrayList<String> getHighlights = new ArrayList<>();
        try {
            myHighlights = getHighlightedText(theFilePath, 0);
        } catch(IOException e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    /**
     * Gets the highlighed text in the file and returns it in an arraylist
     * @param filePath Path of the file to get highlights from
     * @param pageNumber the page number to check
     * @return An array list that contains the highlights
     * @throws IOException
     */
    public static ArrayList<String> getHighlightedText(String filePath, int pageNumber) throws IOException {
        ArrayList<String> highlightedTexts = new ArrayList<>();
        // this is the in-memory representation of the PDF document.
        // this will load a document from a file.

        PDDocument document = PDDocument.load(new File(filePath));
        // this represents all pages in a PDF document.
        List<PDPage> allPages = new ArrayList<>();
        document.getDocumentCatalog().getPages().forEach(allPages::add);
        // this represents a single page in a PDF document.
        PDPage page = allPages.get(pageNumber);
        // get  annotation dictionaries
        List<PDAnnotation> annotations = page.getAnnotations();

        for (int i = 0; i < annotations.size(); i++) {
            // check subType
            if (annotations.get(i).getSubtype().equals("Highlight")) {
                // extract highlighted text
                PDFTextStripperByArea stripperByArea = new PDFTextStripperByArea();

                COSArray quadsArray = (COSArray) annotations.get(i).getCOSObject().getDictionaryObject(COSName.getPDFName("QuadPoints"));
                String str = null;

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

                    PDRectangle pageSize = page.getMediaBox();
                    uly = pageSize.getHeight() - uly;

                    Rectangle2D.Float rectangle_2 = new Rectangle2D.Float(ulx, uly, width, height);
                    stripperByArea.addRegion("highlightedRegion", rectangle_2);
                    stripperByArea.extractRegions(page);
                    String highlightedText = stripperByArea.getTextForRegion("highlightedRegion");

                    if (j > 1) {
                        str = str.concat(highlightedText);
                    } else {
                        str = highlightedText;
                    }
                }
                highlightedTexts.add(str);
            }
        }
        document.close();
        return highlightedTexts;
    }
    public ArrayList<String> getMyHighlights(){
        return myHighlights;
    }
    /**
     *  Return string representation of the object
     * @return string representation of the object
     */
    public String toString(){
        return myHighlights.toString();
    }
}