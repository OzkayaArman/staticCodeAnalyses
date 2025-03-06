public class CyclicC {
    // Field dependency on CyclicA creates a cycle
    private CyclicA a;
    
    public CyclicC() {
        this.a = new CyclicA();
    }
}