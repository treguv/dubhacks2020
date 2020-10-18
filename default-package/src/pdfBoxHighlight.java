import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.desktop.ScreenSleepEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class pdfBoxHighlight {

    public static void main(String[]args) throws IOException {
        //Parse demo pdf for just higlighted sections
        ParseHighlights parse = new ParseHighlights("C:\\Users\\vladi\\Downloads\\New folder\\test-0.pdf");
        //pass the demo stringinto the pdf writer
        //Will make pdfs
        CreatePDF testPdf = new CreatePDF();
        //write array list into pdf
        testPdf.addNewPage();
        testPdf.writeArrayListText(parse.getMyHighlights());
        testPdf.saveDocument("Hello World!.pdf");
    }


}
