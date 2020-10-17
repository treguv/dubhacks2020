import java.util.ArrayList;

class Highlight{
    protected int myLocationX;
    protected int myLocationY;
    protected int myLocationWidth;
    protected int myLocationHeight;
    protected int myAmount;
    protected ArrayList<String> myStudents;

    public Highlight(int[] location, int amount){
        myLocationX = location[0];
        myLocationY = location[1];
        myLocationWidth = location[2];
        myLocationHeight = location[3];
        myAmount = amount;
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
