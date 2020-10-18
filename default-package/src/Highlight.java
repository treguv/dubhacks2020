
class Highlight{
    protected String myText;
    protected int myPage;
    protected int myCount;

    public Highlight(String theText, int thePage, int theCount){
        myText = theText;
        myPage = thePage;
        myCount = theCount;
    }

    public Highlight(String theText, int thePage){
        myText = theText;
        myPage = thePage;
        myCount = 1;
    }

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
}
