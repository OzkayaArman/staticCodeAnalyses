package src;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
    private final HashMap<String, HashSet<String>> parentChildMap; // key is parent class, value is set of children
    private final List<CompilationUnit> asts; //list of the ASTs for the files in the directory

    /**
     * Constructor for the InheritanceAnalyses class.
     * @param dir - the directory containing the Java source files to be analyzed
     */
    public InheritanceAnalyses(String dir) {
        this.parentChildMap = new HashMap<>();
        this.asts = convertSourceCodeToAsts(dir);
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
        VoidVisitor<HashMap<String, HashSet<String>>> classCollector = new ClassCollector();
        classCollector.visit(ast, parentChildMap);
    }

    /**
     * This adaptee class is used to collect all class declarations/ class names within an AST.
     * It is used to initialise the parentChildMap.
     */
    private static class ClassCollector extends VoidVisitorAdapter<HashMap<String, HashSet<String>>> {

        @Override
        public void visit(ClassOrInterfaceDeclaration parent, HashMap<String, HashSet<String>> map) {
            super.visit(parent, map);
            if(parent.isInterface()) {
                return;
            }
            map.put(parent.getNameAsString(),new HashSet<>());
        }
    }

    /**
     * This adapter class is used to collect all child classes present within an AST for its associated parent class within parentChildMap.
     * It is used to populate the parentChildMap. 
     * 
     */
    private static class ChildCollector extends VoidVisitorAdapter<HashMap<String, HashSet<String>>> {

        @Override
        public void visit(ClassOrInterfaceDeclaration child, HashMap<String,  HashSet<String>> map) {
            super.visit(child, map);

            child.getExtendedTypes().forEach((cl) -> {
                String parent = cl.getNameAsString(); //parent class
                map.computeIfPresent(parent, (k, v) -> {
                    v.add(child.getNameAsString()); //adds the child to the parent's set of children
                    return v;
                });
            });
        }
    }

    /**
     * This method finds all children for a class and adds them to the parentChildMap.
     * @param ast - Compilation Unit - the AST for the class source file
     */
    private void findAllChildrenForClass(CompilationUnit ast) {
        VoidVisitor<HashMap<String,  HashSet<String>>> childCollector = new ChildCollector();
        childCollector.visit(ast, parentChildMap);

    }

    /**
     * This method finds the maximum breadth of the inheritance hierarchy in the directory.
     * It prints the maximum breadth and the classes that have that breadth.
     */
    public void findMaximumBreadth() {
        getAllClasses();
        findChildrenForAllClasses();

        int maxBreath = 0;

        for(Entry<String, HashSet<String>> entry : parentChildMap.entrySet()) {    
            int numberOfChildren = entry.getValue().size();
            if(numberOfChildren > maxBreath) {
                maxBreath = numberOfChildren;
            }
        }

        System.out.println("The Maximum Breadth of the inheritance hierarchy in this directory is " + maxBreath);
        System.out.println("The following classes have "+maxBreath+" subclasses : ");

        for(Entry<String, HashSet<String>> entry : parentChildMap.entrySet()) {    
            int numberOfChildren = entry.getValue().size();
            if(numberOfChildren == maxBreath) {
                System.out.println("\t- "+ entry.getKey() + ", with subclasses:");
                entry.getValue().forEach((child) -> {
                    System.out.println("\t\t- "+child);
                });
            }
        };
    }

    /**
     * This method finds the average branching factor of the inheritance hierarchy in the directory.
     * It prints the average branching factor.
     * The average branching factor is calculated as:
     * num of non leaf nodes (incl root node) / num of non root nodes
     */
    public void findAverageBranchingFactor() {

        double numNonLeafNodes = (double) parentChildMap.entrySet().stream().filter(x -> x.getValue().size() > 0).count();
        double numNonRootNodes = parentChildMap.size();

        double root = 1;
        double avgBranchingFactor = numNonRootNodes / (numNonLeafNodes + root);
        avgBranchingFactor = Math.round(avgBranchingFactor * 100.0) / 100.0;

        System.out.println("Average branching factor for the inheritence hierarchy is : " + avgBranchingFactor +" children per class");

    }

}