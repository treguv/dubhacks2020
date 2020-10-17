import java.io.File;
import java.io.IOException;

// These are the most important packages to import
// for basic document manipulation.
import com.pdftron.common.PDFNetException;
import com.pdftron.pdf.*;
import com.pdftron.sdf.SDFDoc;
import org.w3c.dom.Text;

import java.io.*;

public class DisplayMain
{
    // Just a simple setup for the application
    public static void main(String[] args) {
        try{
            //Set up link to the pdf
            PDFNet.initialize();
            PDFDoc newDoc = new PDFDoc("./numbered.pdf");
            newDoc.initSecurityHandler();
            //Make page object from first page of pdf
            Page docPage = newDoc.getPage(1);

            //Set up text extraction
            TextExtractor txt = new TextExtractor();
            txt.begin(docPage);

            Highlights highlights;

            //extracts words one by one
            TextExtractor.Word word;
            for (TextExtractor.Line line = txt.getFirstLine(); line.isValid(); line = line.getNextLine())
            {
                for (word = line.getFirstWord(); word.isValid(); word = word.getNextWord())
                {
                    System.out.println(word.getString());
                }
            }


        }catch (PDFNetException e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
    public static void HiglightFinder(){

    }
}