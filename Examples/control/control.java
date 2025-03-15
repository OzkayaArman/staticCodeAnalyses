package Examples.control;

public class control {

    public static String processNumber(int number) {
        // If statement
        if (number < 0) {
            return "Negative number"; // Return statement
        }

        // Switch-case statement
        switch (number % 3) {
            case 0:
                System.out.println("Divisible by 3");
                break;
            case 1:
                System.out.println("Remainder 1 when divided by 3");
                break;
            case 2:
                System.out.println("Remainder 2 when divided by 3");
                break;
            default:
                System.out.println("Unexpected case");
        }

        // For loop
        int sum = 0;
        for (int i = 0; i < number; i++) {
            sum += i;
        }
        System.out.println("Sum of numbers from 0 to " + (number - 1) + ": " + sum);

        // While loop
        int count = 0;
        while (count < 3) {
            System.out.println("While loop iteration: " + count);
            count++;
        }

        // For-each loops
        int[] values = { 1, 2, 3, 4, 5 };
        for (int val : values) {
            System.out.println("Value: " + val);
        }

        int[] values2 = { 1, 2, 3, 4, 5 };
        for (int val : values2) {
            System.out.println("Value: " + val);
        }
        

        return "Processing complete"; // Return statement
    }

    public static void main(String[] args) {
        System.out.println(processNumber(5));
    }
}
