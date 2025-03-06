public class CyclicA {
    // Field dependency on CyclicB
    private CyclicB b;
    
    public CyclicA() {
        // For demonstration, we can instantiate the dependency
        this.b = new CyclicB();
    }
}
