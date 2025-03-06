package staticCodeAnalyses.src;

import java.io.File;

public class StaticCodeAnalysis {
    public static void main(String[] args) {
        try {
            // Create an instance of the CyclicDependencies analyzer.
            CyclicDependencies cyclicDeps = new CyclicDependencies();
            
            
            File cycleDir = new File("examples/Cyclic");
            File noCycleDIr = new File("examples/NonCyclic");
            // Parse the project directory to build the dependency graph.
            cyclicDeps.parseProject(cycleDir);
            
            // Check for cyclic dependencies and print the result.
            if (cyclicDeps.hasCycle()) {
                System.out.println("Cyclic dependencies found!");
            } else {
                System.out.println("No cyclic dependencies detected.");
            }
            cyclicDeps = new CyclicDependencies();

            cyclicDeps.parseProject(noCycleDIr);
            if (cyclicDeps.hasCycle()) {
                System.out.println("Cyclic dependencies found!");
            } else {
                System.out.println("No cyclic dependencies detected.");
            }
        } catch (Exception e) {
            // Print any exceptions that occur during parsing or analysis.
            e.printStackTrace();
        }
    }
}
