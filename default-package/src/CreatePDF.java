import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.IOException;
import java.util.ArrayList;

public class CreatePDF {
    //this pdf object
    private PDDocument myPDF;
    private PDFont myFont = PDType1Font.HELVETICA_BOLD;
    private PDPage myCurrentPage;
    private float myFontSize = 12;

    /**
     * Parameterless constructor
     */
    public CreatePDF(){
        myPDF = makeBlankDocument();
    }

    /**
     * Makes a blank PDF document
     * @return new document
     */
    private PDDocument makeBlankDocument(){
        PDDocument document= new PDDocument();
        return document;
    }

    /**
     * Adds blank page onto the current document
     * When you make a new page thecurrent page for writeText() is set to the new page
     */
    public void addNewPage(){
        PDPage newPage = new PDPage();
        myCurrentPage = newPage;
        myPDF.addPage(newPage);
    }
    public void writeText(String theText){
        try{
            PDPageContentStream contentStream = new PDPageContentStream(myPDF, myCurrentPage);//Make new stream
            contentStream.beginText();//Open a text stream
            contentStream.setFont(myFont, myFontSize        );
            //contentStream.moveTextPositionByAmount(100,600);
            contentStream.drawString(theText);
            contentStream.endText();
            contentStream.close();

        } catch(IOException e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
    /**
     *  Saves the document with given name.
     *  (Assumes that if you save the document you are done so it closes the document)
     * @param theDocumentName the name of the file
     */
    public void saveDocument(String theDocumentName){
        try{
            myPDF.save(theDocumentName);
            myPDF.close();
        } catch(IOException e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    /**
     * Takes in an array list and writes its contents onto the page
     * @param theArrayList the array list to write
     */
    public void writeArrayListText(ArrayList<String> theArrayList){

    }

}
