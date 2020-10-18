import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

/**
 * EXPECTING: Input of Arraylist of strings that were highlighted and how many times they were highlighted
 */
public class CreatePDF {
    //Set up the writer to append instead of overwirite
    //this pdf object
    private PDDocument myPDF;
    private PDFont myFont = PDType1Font.TIMES_ROMAN;
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
    public void writeText(String theText, int theXPos, int theYPos){
        try{
            PDPageContentStream contentStream = new PDPageContentStream(myPDF, myCurrentPage);//Make new stream
            contentStream.beginText();//Open a text stream
            contentStream.setFont(myFont, myFontSize        );
            //contentStream.moveTextPositionByAmount(100,600);
            contentStream.drawString(theText); //PDPageContentStream.AppendMode.APPEND <-- Stupid depricated method
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
    public void writeArrayListText(@NotNull ArrayList<String> theArrayList){
        for(int i = 0;i <  theArrayList.size(); i++){
            String currentText = theArrayList.get(i);
            currentText = currentText.replace("\n","").replace("\r","");
            writeText(currentText);
        }
    }

}
