import java.util.ArrayList;

class Highlight{
    protected int myLocationX;
    protected int myLocationY;
    protected int myLocationWidth;
    protected int myLocationHeight;
    protected int myAmount;
    protected ArrayList<String> myStudents;

    public Highlight(int locationX, int locationY, int locationWidth, int locationHeight, int amount){
        myLocationX = locationX;
        myLocationY = locationY;
        myLocationWidth = locationWidth;
        myLocationHeight = locationHeight;
        myAmount = amount;
        myStudents = new ArrayList<>();
    }

    public Highlight(int locationX, int locationY, int locationWidth, int locationHeight){
        myLocationX = locationX;
        myLocationY = locationY;
        myLocationWidth = locationWidth;
        myLocationHeight = locationHeight;
        myAmount = 1;
        myStudents = new ArrayList<>();
    }

    public void addStudent(String name){
        myStudents.add(name);
    }

    public Boolean compare(Highlight otherHighlight){
        Boolean isEqual = false;
        //TO DO: compare two highlight objects and see if they overlap.
        return isEqual;
    }

    public void increaseAmount(){
        myAmount++;
    }

    public void decreaseAmount(){
        myAmount--;
    }

    public int getAmount() {
        return myAmount;
    }

    public ArrayList<String> getStudents() {
        return myStudents;
    }

    public int[] getMyLocation() {
        int[] location = new int[]{myLocationX, myLocationY, myLocationWidth, myLocationHeight};
        return location;
    }

}
