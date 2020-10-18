/**
 * The Highlight class stores data for a highlight
 */
class Highlight{
    /**
     * Fields
     */
    protected String myText;
    protected int myCount;

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

    /**
     * Increases the count of occurences
     */
    public void increaseCount(){
        myCount++;
    }

    /**
     * Returns the number of occurences
     * @return the number of occurences
     */
    public int getCount() {
        return myCount;
    }

    /**
     * Returns the text from this highlight
      * @return the text from this highlight
     */
    public String getText() {
        return myText;
    }

    /**
     * Returns a String representation of this class
     * @return String representation of this class
     */
    public String toString() { return this.myText + " " + this.myCount;}

}
