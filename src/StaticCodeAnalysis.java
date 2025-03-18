package src;

import java.io.File;
import java.util.List;
import java.util.Set;

public class StaticCodeAnalysis {
    public static void main(String[] args) {
        try {
            // Initialize the controlFlow object.
            ControlFlow controlFlow = new ControlFlow();

            File controlDir = new File("examples/control");
            File noControl = new File("examples/noControl");
            File cycleDir = new File("examples/Cyclic");
            File noCycleDIr = new File("examples/NonCyclic");

            //Arman
            // Parse the project directory to build the dependency graph.
            controlFlow.parseProject(controlDir);
            System.out.println("Total number of if statements: " + ControlFlow.ifStatementCount);
            System.out.println("Total number of for statements: " + ControlFlow.forStatementCount.get());
            System.out.println("Total number of for...each statement: " + ControlFlow.forEachStatementCount.get());
            System.out.println("Total number of while statements: " + ControlFlow.whileStatementCount.get());
            System.out.println("Total number of do...while statements: " + ControlFlow.doStatementCount.get());
            System.out.println("Total number of switch case statements: " + ControlFlow.switchCaseCount.get());

            System.out.println("Your t")

            //Nandi 
            InheritanceAnalyses ia = new InheritanceAnalyses("examples/example");
            ia.findMaximumBreadth();
            ia.findAverageBranchingFactor();
            
            //Daniel

            // Create an instance of the CyclicDependencies analyzer.
            DependencyCycles cyclicDeps = new DependencyCycles();

            // Parse the project directory to build the dependency graph.
            cyclicDeps.parseProject(cycleDir);

            // Check for cyclic dependencies and print the result.
            Set<List<String>> cycles = cyclicDeps.getCycles();
            if (cycles.size() > 0) {
                System.out.println("Cyclic dependencies found!");
                for (List<String> cycle : cycles) {
                    printCycle(cycle);
                }
            } else {
                System.out.println("No cyclic dependencies detected.");
            }
            cyclicDeps = new DependencyCycles();

            cyclicDeps.parseProject(noCycleDIr);
            cycles = cyclicDeps.getCycles();
            if (cycles.size() > 0) {
                System.out.println("Cyclic dependencies found!");
                for (List<String> cycle : cycles) {
                    printCycle(cycle);
                }
            } else {
                System.out.println("No cyclic dependencies detected.");
            }

        } catch (Exception e) {
            // Print any exceptions that occur during parsing or analysis.
            e.printStackTrace();
        }
    }

    static private void printCycle(List<String> cycle) {
        String output = String.join(" -> ", cycle);

        output += " -> " + cycle.get(0);
        System.out.println(output);
    }

}