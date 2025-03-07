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
            System.out.println("Total number of if statements: " + controlFlow.ifStatementCount.get());
            
        } catch (Exception e) {
            // Print any exceptions that occur during parsing or analysis.
            e.printStackTrace();
        }
    }

}