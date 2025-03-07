package Examples;
public class control {
    public static void analyzeNumber(int num) {
        if (num < 0) {  // +1
            System.out.println("Negative number");
        } else if (num == 0) {  // +1
            System.out.println("Zero");
        } else {  
            System.out.println("Positive number");
        }

        for (int i = 1; i <= num; i++) {  // +1
            if (i % 2 == 0) {  // +1
                System.out.println(i + " is even");
            } else {  // +1
                System.out.println(i + " is odd");
            }
        }
    }

    public static void main(String[] args) {
        analyzeNumber(3);
    }
}
