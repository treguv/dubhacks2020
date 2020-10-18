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
            highlightString(myFile, currHighlight,
             getMaxHighlightOccurence(theHighlightList));
        }
        try{
            String newFileName = theOriginalFilePath.substring(0,
             theOriginalFilePath.length() - 4) + "result.pdf";
            myFile.save(newFileName,
                    SerializationModeEnum.Incremental);
            myFile.close();
            Desktop.getDesktop().open(new java.io.File(newFileName));
//            System.out.println("SAVED FILE!");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void highlightString(File myFile,
     Highlight theHighlight, int maxOccurrences){
        Pattern thePattern = Pattern.compile(theHighlight.getText().trim(),
                Pattern.CASE_INSENSITIVE);
        // try{
//        System.out.print("TRYING TO SEARCH!");

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
                            new TextMarkup(page, highlightQuads,null,
                              MarkupTypeEnum.Highlight).withColor(getColor(theHighlight, maxOccurrences));
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

    /**
    * todo bad comment
     * Returns the maximum number of occurrences
     * @param theHighlightList
     * @return
     */
    private static int getMaxHighlightOccurence(ArrayList<Highlight> theHighlightList) {
         int max = Integer.MIN_VALUE;
         for (Highlight currHighlight : theHighlightList) {
             if (currHighlight.getCount() > max) {
                 max = currHighlight.getCount();
             }
         }
//         System.out.println(max);
         return max;
    }

    private static DeviceRGBColor getColor(Highlight theHighlight,
     int theMaxOccurences) {
        double ratio = (double) (theHighlight.getCount() / (double) theMaxOccurences);
        DeviceRGBColor color;
        if (ratio < .25) {
            color = DeviceRGBColor.get(new Color(115, 166, 231));
        } else if(ratio < .5) {
            color = DeviceRGBColor.get(new Color(115, 166, 231));
        } else if (ratio < .75) {
            color = DeviceRGBColor.get(new Color(115, 166, 231));
        } else if (ratio < 1) {
            color = DeviceRGBColor.get(new Color(115, 166, 231));
        } else {
            color = DeviceRGBColor.get(new Color(115, 166, 231));
        }

        return color;
//        return new DeviceRGBColor(ratio * .5, .75, ratio);
//        return new DeviceRGBColor(0.0, 0.9, .2);
    }
}
