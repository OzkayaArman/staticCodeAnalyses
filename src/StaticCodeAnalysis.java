
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class StaticCodeAnalysis {
    static Set<List<String>> cycles;
    static int numCycles;
    static Map<String, SubClassCollection> broadest;
    static double avgBranchingFactor;
    static int cyclomaticComplexity;
    static int maxBreath;
    static InheritanceAnalyses ia ;
    public static void main(String[] args) {
        String folder = "";
        try{
            folder = args[0];
        } catch (Exception e){
            System.out.println("First argument should be the path to your project folder");
            return;
        }

        try {
            ia = new InheritanceAnalyses(folder);
            broadest = ia.findMaximumBreadth();
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
        maxBreath = ia.getMaximumBreadth();
        System.out.println("The Maximum Breadth of the inheritance hierarchy in this directory is " + maxBreath);
        System.out.println("The following classes have "+ maxBreath +" subclasses : ");
        for(Entry<String, SubClassCollection> entry : broadest.entrySet()) {    
            System.out.println("\t- "+ entry.getKey() + ", with subclasses:");
            entry.getValue().getSubClasses().forEach((child) -> {
                System.out.println("\t\t- "+child);
            });
        };
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
        System.out.println("Overall quality score: " + calcuateScore());

    }

    static private void printCycle(List<String> cycle) {
        String output = String.join(" -> ", cycle);

        output += " -> " + cycle.get(0);
        System.out.println(output);
    }

    static private int calcuateScore(){
        int score;
        int cycleScore = 100;
        int complexityScore;
        int inheritanceScore;

        for (int i = 1; i <= numCycles; i++){
            cycleScore -= (1/Math.pow(2, i)) * 100;
        }

        complexityScore = Math.max(0,100 - cyclomaticComplexity);
        inheritanceScore  =  Math.max(0,100 - (int)avgBranchingFactor - maxBreath);

        score = (complexityScore + cycleScore + inheritanceScore)/3;
        
        return score;
    }
}