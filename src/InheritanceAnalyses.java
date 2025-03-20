
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

/**
 * This class is responsible for analyzing the inheritance hierarchy of a directory of Java source files.
 * In particular, it finds the maximum breadth of the inheritance hierarchy and the average branching factor.
 * @author 190031593
 */
public class InheritanceAnalyses {
    private  HashMap<String, SubClassCollection> parentChildMap; // key is parent class, value is set of children as SubClassCollection object
    private List<CompilationUnit> asts; //list of the ASTs for the files in the directory
    private double avgBranchingFactor; //average branching factor for the inheritance hierarchy
    private int maxBreadth;  //maximum breadth of the inheritance hierarchy
    private double numberOfParentClasses; //number of parent classes
    private double numberOfSubClasses; //number of subclasses
    /**
     * Constructor for the InheritanceAnalyses class.
     * @param dir - the directory containing the Java source files to be analyzed
     */
    public InheritanceAnalyses(String dir) {
        this.parentChildMap = new HashMap<>();
        this.asts = convertSourceCodeToAsts(dir);
        this.avgBranchingFactor = 0;
        this.maxBreadth = 0;
        this.numberOfParentClasses = 0;
        this.numberOfSubClasses = 0;
    }

    /**
     * This method converts the source code in the directory to ASTs.
     * @param dir - the directory containing the Java source files
     * @return a list of CompilationUnits, each representing the AST for a file in the directory
     */
    private List<CompilationUnit> convertSourceCodeToAsts(String dir) {
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            System.out.println("This directory does not exsit!");
            System.exit(0);
        }
        SourceRoot root = new SourceRoot(dirFile.toPath());
        try {
            root.tryToParse();
        } catch (IOException e) {
            System.out.println("Something went wrong when trying to parse source files." + e);
        }
        return root.getCompilationUnits();

    }

    /**
     * This method gets the name of all classes contained in the directory/amongst all ASTs.
     */
    private void getAllClasses() {
        asts.forEach(ast -> getAllClassesInFile(ast));
    }

    /**
     * This method counts the number of subclass each class in the directory has. 
     */
    private void findChildrenForAllClasses() {
        asts.forEach(ast -> findAllChildrenForClass(ast));
    }

    /**
     * This method gets the name of all classes in a file and adds them to the parentChildMap.
     * 
     * @param ast - Compilation Unit - the AST for the file
     */
    private void getAllClassesInFile(CompilationUnit ast) {
        VoidVisitor<HashMap<String, SubClassCollection>> classCollector = new ClassCollector();
        classCollector.visit(ast, parentChildMap);
    }

    /**
     * This adaptee class is used to collect all class declarations/ class names within an AST.
     * It is used to initialise the parentChildMap.
     */
    private static class ClassCollector extends VoidVisitorAdapter<HashMap<String, SubClassCollection>> {

        @Override
        public void visit(ClassOrInterfaceDeclaration parent, HashMap<String, SubClassCollection> map) {
            super.visit(parent, map);
            if(parent.isInterface()) {
                return;
            }
            map.put(parent.getNameAsString(),new SubClassCollection(parent.getNameAsString(), false));
        }
    }

    /**
     * This adapter class is used to collect all child classes present within an AST for its associated parent class within parentChildMap.
     * It is used to populate the parentChildMap. 
     * 
     */
    private static class ChildCollector extends VoidVisitorAdapter<HashMap<String, SubClassCollection>> {

        @Override
        public void visit(ClassOrInterfaceDeclaration child, HashMap<String,  SubClassCollection> map) {
            super.visit(child, map);

            if (!child.getExtendedTypes().isEmpty()) {
                child.getExtendedTypes().forEach((cl) -> {
                    String parent = cl.getNameAsString(); //parent class name
                    SubClassCollection parentCollection = map.get(parent);
                    parentCollection.addSubClass(child.getNameAsString()); //adds the child to the parent's set of children
                    parentCollection.setIsParentClass(true); //sets the parent as a base class
                }); 
            }else{
                map.get(child.getNameAsString()).setIsParentClass(true); //sets the class as a base class if it has no parent
            }
      
        }
    }

    /**
     * This method finds all children for a class and adds them to the parentChildMap.
     * @param ast - Compilation Unit - the AST for the class source file
     */
    private void findAllChildrenForClass(CompilationUnit ast) {
        VoidVisitor<HashMap<String,  SubClassCollection>> childCollector = new ChildCollector();
        childCollector.visit(ast, parentChildMap);

    }

    /**
     * This method finds the maximum breadth of the inheritance hierarchy in the directory.
     * It prints the maximum breadth and the classes that have that breadth.
     */
    public Map<String, HashSet<String>> findMaximumBreadth() {
        getAllClasses();
        findChildrenForAllClasses();
        Map<String, HashSet<String>> broadest = new HashMap<>();


        for(Entry<String, SubClassCollection> entry : parentChildMap.entrySet()) {    
            int numberOfChildren = entry.getValue().getNumberOfSubClasses();
            if(numberOfChildren > maxBreadth) {
                maxBreadth = numberOfChildren;
            }
        }

        System.out.println("The Maximum Breadth of the inheritance hierarchy in this directory is " + maxBreadth);

        if(maxBreadth == 0) {
            return;
        }

        System.out.println("The following classes have "+maxBreadth+" subclasses : ");

        for(Entry<String, SubClassCollection> entry : parentChildMap.entrySet()) {    
            int numberOfChildren = entry.getValue().getNumberOfSubClasses();
            if(numberOfChildren == maxBreadth) {
                System.out.println("\t- "+ entry.getKey() + ", with subclasses:");
                entry.getValue().getSubClasses().forEach((child) -> {
                    System.out.println("\t\t- "+child);
                });
            }
        };
        return broadest;
    }

    /**
     * This method finds the average branching factor of the inheritance hierarchy in the directory.
     * It prints the average branching factor.
     * The average branching factor is calculated as:
     *  num subclasses /num of parent classes
     */
    public double findAverageBranchingFactor() {


        numberOfParentClasses = (double) parentChildMap.entrySet().stream().filter(x -> x.getValue().isParentClass()).count();
        //numberOfDerivedClasses = (double) parentChildMap.entrySet().stream().filter(x -> !x.getValue().isParentClass()).count();
        //A parent class can also be a child class
        HashSet<String> derivedClasses = new HashSet<>();
        parentChildMap.forEach((k,v) -> {
            v.getSubClasses().forEach((child) -> {
                derivedClasses.add(child);
            });
        });
        numberOfSubClasses = derivedClasses.size();
        avgBranchingFactor = numberOfSubClasses / numberOfParentClasses;
        avgBranchingFactor = Math.round(avgBranchingFactor * 100.0) / 100.0;

       // System.out.println("Average branching factor for the inheritence hierarchy is : " + avgBranchingFactor +" children per class");
        return avgBranchingFactor;
    }
    /**
     *  This method returns the average branching factor for the inheritance hierarchy.
     * @return the average branching factor
     */
    public double getAverageBranchingFactor(){
        return avgBranchingFactor;
    }


    /**
     * This method returns the maximum breadth of the inheritance hierarchy.
     * @return the maximum breadth of the inheritance hierarchy
     */ 
    public int getMaximumBreadth(){
        return maxBreadth;
    }

    /**
     * This method returns the number of classes in the directory.
     * @return the number of classes in the directory
     */
    public int getNumberOfClasses(){
        return parentChildMap.size();
    }

    /**
     * This method returns the number of parent classes in the directory/hierarchy.
     * @return the number of parent classes
     */
    public double getNumberOfParentClasses(){
        return numberOfParentClasses;
    }

    /**
     * This method returns the number of subclasses in the directory/hierarchy.
     * @return the number of subclasses
     */
    public double getNumberOfSubClasses(){
        return numberOfSubClasses;
    }
}