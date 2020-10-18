import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.pdfclown.documents.Page;
import org.pdfclown.documents.contents.ITextString;
import org.pdfclown.documents.contents.TextChar;
import org.pdfclown.documents.contents.colorSpaces.DeviceRGBColor;
import org.pdfclown.documents.interaction.annotations.TextMarkup;
import org.pdfclown.documents.interaction.annotations.TextMarkup.MarkupTypeEnum;
import org.pdfclown.files.File;
import org.pdfclown.files.SerializationModeEnum;
import org.pdfclown.tools.TextExtractor;
import org.pdfclown.util.math.Interval;
import org.pdfclown.util.math.geom.Quad;

public class HighlightPdf {

    public static void createHighlightDocument(String theOriginalFilePath, ArrayList<Highlight> theHighlightList) {
        File myFile = null;
        try{
            myFile=  new File(theOriginalFilePath);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        // loop through the Highlights in theHighlightList
        for (Highlight currHighlight : theHighlightList) {
            Pattern pattern = Pattern.compile(currHighlight.getText().trim(),
             Pattern.CASE_INSENSITIVE);
            highlightString(myFile, pattern);
        }
        try{
            myFile.save(theOriginalFilePath.substring(0, theOriginalFilePath.length() - 4) + "result.pdf",
                    SerializationModeEnum.Incremental);
            myFile.close();
            System.out.println("SAVED FILE!");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void highlightString(File myFile, Pattern thePattern){
        // try{
        System.out.print("TRYING TO SEARCH!");

        TextExtractor textExtractor = new TextExtractor(true, true);

        //The stuff that breaks
        for(final Page page : myFile.getDocument().getPages()) {
            // Extract the page text!
            Map<Rectangle2D,List<ITextString>> textStrings = textExtractor.extract(page);

            // Find the text thePattern matches!
            final Matcher matcher = thePattern.matcher(TextExtractor.toString(textStrings));
            // Highlight the text thePattern matches!
            textExtractor.filter(
                    textStrings,
                    new TextExtractor.IIntervalFilter() {
                        @Override
                        public boolean hasNext() {
                            return matcher.find();
                        }

                        @Override
                        public Interval next() {
                            return new Interval(matcher.start(), matcher.end());
                        }

                        @Override
                        public void process(Interval interval, ITextString match) {
                            // Defining the highlight box of the text thePattern match...
                            List highlightQuads = new ArrayList();
                            {
                                Rectangle2D textBox = null;
                                for(TextChar textChar : match.getTextChars())
                                {
                                    Rectangle2D textCharBox = textChar.getBox();
                                    if(textBox == null)
                                    {textBox = (Rectangle2D)textCharBox.clone();}
                                    else
                                    {
                                        if(textCharBox.getY() > textBox.getMaxY())
                                        {
                                            highlightQuads.add(Quad.get(textBox));
                                            textBox = (Rectangle2D)textCharBox.clone();
                                        }
                                        else
                                        {textBox.add(textCharBox);}
                                    }
                                }
                                highlightQuads.add(Quad.get(textBox));
                            }
                            // Highlight the text thePattern match!
                            new TextMarkup(page, highlightQuads,null,  MarkupTypeEnum.Highlight).withColor(new DeviceRGBColor(0.0, 0.9, .2));
                        }
                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    }
            );
        }
//        } catch (FileNotFoundException e){
//            e.printStackTrace();
//        }
    }
}
