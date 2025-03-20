public class IntegerCustom {
    private int value = 0;
    IntegerCustom(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
    public void setValue(int valueIn){
        value = valueIn;
    }

    public void increment(){
        value = value +1;
    } 
}