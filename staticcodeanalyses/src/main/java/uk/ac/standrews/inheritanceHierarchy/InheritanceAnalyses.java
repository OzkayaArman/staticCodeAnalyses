package uk.ac.standrews.inheritenceHierarchy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

/**
 * 
 */
public class InheritenceAnalyses {
    private final HashMap<String, Integer> parentChildMap;
    private final List<CompilationUnit> asts;

    /**
     * 
     * @param dir
     */
    public InheritenceAnalyses(String dir) {
        this.parentChildMap = new HashMap<>();
        this.asts = getAsts(dir);
    }

    /**
     * 
     */
    private List<CompilationUnit> getAsts(String dir) {
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
     * 
     */
    private void getAllClasses() {
        asts.forEach(ast -> getAllClassesInFile(ast));
    }

    /**
     * 
     */
    private void countChildren() {
        asts.forEach(ast -> findAllParents(ast));
    }

    /**
     * This method gets all classes within a file.
     * 
     * @param ast - Compilation Unit the AST for the file
     */
    private void getAllClassesInFile(CompilationUnit ast) {
        VoidVisitor<HashMap<String, Integer>> classCollector = new ClassCollector();
        classCollector.visit(ast, parentChildMap);
    }

    /**
     * 
     */
    private static class ClassCollector extends VoidVisitorAdapter<HashMap<String, Integer>> {

        @Override
        public void visit(ClassOrInterfaceDeclaration cd, HashMap<String, Integer> map) {
            super.visit(cd, map);
            map.put(cd.getNameAsString(), 0);
        }
    }

    /**
     * 
     */
    private static class ChildCollector extends VoidVisitorAdapter<HashMap<String, Integer>> {

        @Override
        public void visit(ClassOrInterfaceDeclaration cd, HashMap<String, Integer> map) {
            super.visit(cd, map);

            cd.getExtendedTypes().forEach((cl) -> {
                String parent = cl.getNameAsString();
                map.computeIfPresent(parent, (k, v) -> ++v);
            });
        }
    }

    private void findAllParents(CompilationUnit ast) {
        VoidVisitor<HashMap<String, Integer>> childCollector = new ChildCollector();
        childCollector.visit(ast, parentChildMap);

    }

    /**
     * 
     */
    public void findMaximumBreadth() {
        getAllClasses();
        countChildren();

        int maxBreath = 0;
        String maxBreathClass = "";

        for (Entry<String, Integer> entry : parentChildMap.entrySet()) {
            if (entry.getValue() > maxBreath) {
                maxBreath = entry.getValue();
                maxBreathClass = entry.getKey();
            }
        }

        System.out.println("Class with Maximum Bread: " + maxBreathClass + " with " + maxBreath + " children");
    }

    /**
     * num of non leaf nodes (incl root node) / num of non root nodes
     */
    public void findAverageBranchingFactor() {

        double numNonLeafNodes = (double) parentChildMap.entrySet().stream().filter(x -> x.getValue() > 0).count();
        double numNonRootNodes = parentChildMap.size();

        double root = 1;
        double avgBranchingFactor = numNonRootNodes / (numNonLeafNodes + root);
        avgBranchingFactor = Math.round(avgBranchingFactor * 100.0) / 100.0;

        System.out.println("Average branching rate for entire directory: " + avgBranchingFactor);

    }

}
