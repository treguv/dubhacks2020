import java.io.File;
import java.io.IOException;

// These are the most important packages to import
// for basic document manipulation. 
import com.pdftron.common.PDFNetException;
import com.pdftron.pdf.*;
import com.pdftron.sdf.SDFDoc;

import java.io.*;

public class PDFReadWrite
{
    // Just a simple setup for the application
    public static void main(String[] args)
    {
        // PDFNet must always be initialized before any PDFTron
        // classes and methods can be used
        PDFNet.initialize();
        System.out.println("Hello World!");

        // Most PDFTron operations are required to be wrapped in
        // a try-catch block for PDFNetException, or in a method/class that
        // throws PDFNetException
        try {
            // Creates a new PDFDoc object
            PDFDoc doc = new PDFDoc();

            // Creating a new page and adding it
            // to document's sequence of pages
            Page page1 = doc.pageCreate();
            doc.pagePushBack(page1);

            // Files can be saved with various options
            // Linearized files are the most effective
            // for opening and viewing quickly on various platforms
            doc.save(("linearized_ouput.pdf"), SDFDoc.SaveMode.LINEARIZED, null);

            doc.close();
        } catch (PDFNetException e) {
            System.out.println(e);
            e.getStackTrace();
        }
        PDFNet.terminate();
    }
}