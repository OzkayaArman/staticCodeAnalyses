package control;

import java.util.ArrayList;
import java.util.List;

public class control {

    public static void processNumber(int number) {
        int testNumber = 10;
        int noOfAcceptors = 3;
        int maxProcessSteps = 50; 
        List<Integer> acceptors = new ArrayList<>();


        if(testNumber < noOfAcceptors){
            System.out.println("If Statement");
        }
        if(testNumber < noOfAcceptors){
            System.out.println("If Statement");
        }
        if(testNumber < noOfAcceptors){
            System.out.println("If Statement");
        }

        //For loop
        for (int i = 0; i < maxProcessSteps; i++) {
            acceptors.add(1);
        }

        for(int x : acceptors){
            System.out.println(x);
        }
        
        int i = 0;
        
        //Do While loop
        do{
            acceptors.add(1);
            i++;
        }while(i < maxProcessSteps);

        // While loop
        while(i > 0){
            i--;
        }

        
    }

    public boolean nextMethod() {
        String msg = "";
        if (msg != null) {
            switch (msg) {
                case "": 
                    System.out.println("Case 1");
                    break;          
                case "prepare":
                    System.out.println("Case 2");
                    break;
                default: 
                    System.out.println("Case default");
                    return true;
            }
        } 
        return false;
    }
    public static void main(String[] args) {
       processNumber(5);
    }
}
