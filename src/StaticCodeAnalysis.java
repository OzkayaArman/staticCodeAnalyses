
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.StaticJavaParser;

public class StaticCodeAnalysis {
    static Set<List<String>> cycles;
    static int numCycles;
    static Map<String, HashSet<String>> broadest;
    static double avgBranchingFactor;
    static int cyclomaticComplexity;
    static int maxBreath;
    public static void main(String[] args) {
        StaticJavaParser.getConfiguration().setLanguageLevel(LanguageLevel.JAVA_14);
        String folder = "";
        try{
            folder = args[0];
        } catch (Exception e){
            System.out.println("First argument should be the path to your project folder");
            return;
        }

        try {
            InheritanceAnalyses ia = new InheritanceAnalyses(folder);
            broadest =  ia.findMaximumBreadth();
            avgBranchingFactor = ia.findAverageBranchingFactor();

            
            //Daniel

            // Create an instance of the CyclicDependencies analyzer.
            DependencyCycles cyclicDeps = new DependencyCycles();

            // Parse the project directory to build the dependency graph.
            File file = new File(folder);
            cyclicDeps.parseProject(file);

            // Check for cyclic dependencies and print the result.
            cycles = cyclicDeps.getCycles();
            numCycles = cycles.size();
 

            //Arman
            ControlFlow controlFlow = new ControlFlow();
            controlFlow.parseProject(file);
            cyclomaticComplexity = ControlFlow.calculateCyclometicComplexity();
            printReport();




        } catch (Exception e) {
            // Print any exceptions that occur during parsing or analysis.
            e.printStackTrace();
        }
    }

    static void printReport(){
        // Inheritence analysis
        maxBreath = broadest.entrySet().iterator().next().getValue().size();
        System.out.println("The Maximum Breadth of the inheritance hierarchy in this directory is " + maxBreath);
        System.out.println("The following classes have "+ maxBreath +" subclasses : ");
        for(Entry<String, HashSet<String>> entry : broadest.entrySet()) {    
            System.out.println("\t- "+ entry.getKey() + ", with subclasses:");
            entry.getValue().forEach((child) -> {
                System.out.println("\t\t- "+child);
            });
        }
        System.out.println("Average branching factor for the inheritence hierarchy is : " + avgBranchingFactor +" children per class");

        // Dependency Cycles
        if (cycles.size() > 0) {
            System.out.println("Cyclic dependencies found!");
            for (List<String> cycle : cycles) {
                printCycle(cycle);
            }
        } else {
            System.out.println("No cyclic dependencies detected.");
        }

        // Cyclomatic Complexity
        System.out.println("The cyclomatic complexity is: " + cyclomaticComplexity);
        System.out.println(calcuateScore());

    }

    static private void printCycle(List<String> cycle) {
        String output = String.join(" -> ", cycle);

        output += " -> " + cycle.get(0);
        System.out.println(output);
    }

    static private int calcuateScore(){
        int score;
        int cycleScore = 100;
        int complexityScore = 100;
        for (int i = 1; i <= numCycles; i++){
            cycleScore -= (1/Math.pow(2, i)) * 100;
        }

        
        
        return (cycleScore + complexityScore)/2;
    }

}