package Cyclic;

public class CyclicB {
    // Field dependency on CyclicC
    private CyclicC c;
    
    public CyclicB() {
        this.c = new CyclicC();
    }
}