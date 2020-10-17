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

public class pdfBoxHighlight {

    public static void main(String[]args) throws IOException {
        ParseHighlights highlights = new ParseHighlights("C:\\Users\\vladi\\Downloads\\New folder\\test-0.pdf");
        System.out.println(highlights.toString());
    }


}
