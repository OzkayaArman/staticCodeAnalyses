
import java.io.File;
import java.util.List;
import java.util.Set;

public class StaticCodeAnalysis {
    public static void main(String[] args) {
        try {
            File cycleDir = new File("examples/Cyclic");
            File noCycleDIr = new File("examples/NonCyclic");

            //Nandi 
            InheritanceAnalyses ia = new InheritanceAnalyses("Examples/Inheritance/FourSub");
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