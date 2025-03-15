package src;

import java.io.File;
public class StaticCodeAnalysis {
    public static void main(String[] args) {
        try {
            // Initialize the controlFlow object.
            ControlFlow controlFlow = new ControlFlow();

            File controlDir = new File("examples/control");
            File noControl = new File("examples/noControl");

            // Parse the project directory to build the dependency graph.
            controlFlow.parseProject(controlDir);
            System.out.println("Total number of if statements: " + ControlFlow.ifStatementCount.get());
            System.out.println("Total number of for statements: " + ControlFlow.forStatementCount.get());
            System.out.println("Total number of for...each statement: " + ControlFlow.forEachStatementCount.get());
            System.out.println("Total number of while statements: " + ControlFlow.whileStatementCount.get());
            System.out.println("Total number of do...while statements: " + ControlFlow.doStatementCount.get());
            System.out.println("Total number of return statements: " + ControlFlow.returnStatementCount.get());
            System.out.println("Total number of switch case statements: " + ControlFlow.switchCaseCount.get());

            
        } catch (Exception e) {
            // Print any exceptions that occur during parsing or analysis.
            e.printStackTrace();
        }
    }

}