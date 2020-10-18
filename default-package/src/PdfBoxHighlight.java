import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;

import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PdfBoxHighlight extends PDFTextStripper{

    public PdfBoxHighlight()  throws IOException {
        super();
    }

    public static void main(String[] args) throws IOException {
//        ArrayList<String> testGetHighlights = new ArrayList<>();
//        testGetHighlights = getHighlightedText("./numbered.pdf", 0);
//        System.out.println(testGetHighlights.toString());

        PDDocument document = null;
        String fileName = "./test-noHighlight.pdf";
        try {
            document = PDDocument.load( new File(fileName) );
            PDFTextStripper stripper = new PdfBoxHighlight();
            stripper.setSortByPosition( true );

            stripper.setStartPage( 0 );
            stripper.setEndPage( document.getNumberOfPages() );

            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(document, dummy);

            File file1 = new File("./test-noHighlight-New.pdf");
            document.save(file1);
        }
        finally {
            if( document != null ) {
                document.close();
            }
        }
    }

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

    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        boolean isFound = true; // make this false when checking for words
        float posXInit  = 0,
                posXEnd   = 0,
                posYInit  = 0,
                posYEnd   = 0,
                width     = 0,
                height    = 0,
                fontHeight = 0;
//        String[] criteria = {"Donec ante", " dolor aliquam", " Donec et urna et enim"};
//
//        for (int i = 0; i < criteria.length; i++) {
//            if (string.contains(criteria[i])) {
//                isFound = true;
//            }
//        }
        if (isFound) {
            posXInit = textPositions.get(0).getXDirAdj();
            posXEnd  = textPositions.get(textPositions.size() - 1).getXDirAdj() + textPositions.get(textPositions.size() - 1).getWidth();
            posYInit = textPositions.get(0).getPageHeight() - textPositions.get(0).getYDirAdj();
            posYEnd  = textPositions.get(0).getPageHeight() - textPositions.get(textPositions.size() - 1).getYDirAdj();
            width    = textPositions.get(0).getWidthDirAdj();
            height   = textPositions.get(0).getHeightDir();

            System.out.println(string + "X-Init = " + posXInit + "; Y-Init = " + posYInit + "; X-End = " + posXEnd + "; Y-End = " + posYEnd + "; Font-Height = " + fontHeight);

            /* numeration is index-based. Starts from 0 */

            float quadPoints[] = {posXInit, posYEnd + height + 2, posXEnd, posYEnd + height + 2, posXInit, posYInit - 2, posXEnd, posYEnd - 2};

            List<PDAnnotation> annotations = document.getPage(this.getCurrentPageNo() - 1).getAnnotations();
            PDAnnotationTextMarkup highlight = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);

            PDRectangle position = new PDRectangle();
            position.setLowerLeftX(posXInit);
            position.setLowerLeftY(posYEnd);
            position.setUpperRightX(posXEnd);
            position.setUpperRightY(posYEnd + height);

            highlight.setRectangle(position);

            // quadPoints is array of x,y coordinates in Z-like order (top-left, top-right, bottom-left,bottom-right)
            // of the area to be highlighted

            highlight.setQuadPoints(quadPoints);

            PDColor yellow = new PDColor(new float[]{1, 1, 1 / 255F}, PDDeviceRGB.INSTANCE);
            highlight.setColor(yellow);
            annotations.add(highlight);
        }
    }


    public static void combineHighlight(ArrayList<Highlight>... arrays) {
    //TO DO:
    }
}
