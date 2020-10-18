import java.io.IOException;

class Highlight{
    /**
     * Fields
     */
    protected String myText;
    protected int myCount;

    /**
     * Constructors
     */
    public Highlight(String theText, int theCount){
        myText = theText;
        myCount = theCount;
    }

    public Highlight(String theText){
        myText = theText;
        myCount = 1;
    }

    /**
     * Compares two Highlight objects.
     * @param theOtherHighlight the Highlight we want to compare to.
     */
    public Boolean compare(Highlight theOtherHighlight){
        return myText.equals(theOtherHighlight.getText());
    }

    public void increaseCount(){
        myCount++;
    }

    public int getCount() {
        return myCount;
    }

    public String getText() {
        return myText;
    }

    public String toString() { return this.myText + " " + this.myCount;}
}
