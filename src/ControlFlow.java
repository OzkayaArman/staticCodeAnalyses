import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.WhileStmt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public class ControlFlow extends Evaluator {
    
    public static final AtomicInteger ifStatementCount = new AtomicInteger(0);
    public static final AtomicInteger forStatementCount = new AtomicInteger(0);
    public static final AtomicInteger forEachStatementCount = new AtomicInteger(0);
    public static final AtomicInteger whileStatementCount = new AtomicInteger(0);
    public static final AtomicInteger doStatementCount = new AtomicInteger(0);
    public static final AtomicInteger switchCaseCount = new AtomicInteger(0);
    public static final AtomicInteger methodNumberCount = new AtomicInteger(0);
    
    // Parses all Java files in the given project directory and builds the dependency graph.
    public void parseProject(File projectDir) throws Exception {
        List<File> files = new ArrayList<>();
        traverseFolder(projectDir, files);
        if (files.size() == 0) {
            throw new Exception("Directory not found or no Java files in: " + projectDir);
        }
        //For every file
        for (File file : files) {
            // Compilation unit is the root node of abstract syntax tree 
            CompilationUnit cu = StaticJavaParser.parse(file);
            
            //Accept recursively visits every node in the abstrat syntax tree
            VoidVisitorAdapter<AtomicInteger> ifVisitor = new ifVisitor();
            VoidVisitorAdapter<AtomicInteger> forVisitor = new forVisitor();
            VoidVisitorAdapter<AtomicInteger> forEachVisitor = new forEachVisitor();
            VoidVisitorAdapter<AtomicInteger> whileVisitor = new whileVisitor();
            VoidVisitorAdapter<AtomicInteger> doWhileVisitor = new doWhileVisitor();
            VoidVisitorAdapter<AtomicInteger> methodNumberVisitor = new methodNumberVisitor();
            VoidVisitorAdapter<AtomicInteger> switchCaseVisitor = new switchVisitor();
            
            ifVisitor.visit(cu,ifStatementCount);
            forVisitor.visit(cu,forStatementCount);
            forEachVisitor.visit(cu,forEachStatementCount);
            whileVisitor.visit(cu,whileStatementCount);
            doWhileVisitor.visit(cu,doStatementCount);
            methodNumberVisitor.visit(cu,methodNumberCount);
            switchCaseVisitor.visit(cu,switchCaseCount);
        }
    }

    //GAP: Calculation Methodology Source https://bluinsights.aws/docs/codebase-cyclomatic-complexity/
    public static int calculateCyclometicComplexity(){
        int decisionPoints = ControlFlow.ifStatementCount.get() + ControlFlow.forEachStatementCount.get() + ControlFlow.forStatementCount.get() + ControlFlow.whileStatementCount.get() + ControlFlow.switchCaseCount.get();
        return (decisionPoints / ControlFlow.methodNumberCount.get()) + 1;
    }
    /**
     * This class extends voidVisitorAdapter to count the number of if statements 
     */
    private static class ifVisitor extends VoidVisitorAdapter<AtomicInteger> {
        @Override
        public void visit(IfStmt ifStmt, AtomicInteger counter) {
            super.visit(ifStmt, counter);
            counter.incrementAndGet(); // Increments count for each if statement
        }
    }

    /**
     * This class extends voidVisitorAdapter to count the number of for statements 
     */
    private static class forVisitor extends VoidVisitorAdapter<AtomicInteger> {
        @Override
        public void visit(ForStmt forStmt, AtomicInteger counter) {
            super.visit(forStmt, counter);
            counter.incrementAndGet(); // Increments count for each for statement
        }
    }


    /**
     * This class extends voidVisitorAdapter to count the number of for statements 
     */
    private static class forEachVisitor extends VoidVisitorAdapter<AtomicInteger> {
        @Override
        public void visit(ForEachStmt forEachStmt, AtomicInteger counter) {
            super.visit(forEachStmt, counter);
            counter.incrementAndGet(); // Increments count for each for statement
        }
    }

    /**
     * This class extends voidVisitorAdapter to count the number of while statements 
     */
    private static class whileVisitor extends VoidVisitorAdapter<AtomicInteger> {
        @Override
        public void visit(WhileStmt whileStmt, AtomicInteger counter) {
            super.visit(whileStmt, counter);
            counter.incrementAndGet(); // Increments count for each while statement
        }
    }

    /**
     * This class extends voidVisitorAdapter to count the number of do...while statements 
     */
    private static class doWhileVisitor extends VoidVisitorAdapter<AtomicInteger> {
        @Override
        public void visit(DoStmt doStmt, AtomicInteger counter) {
            super.visit(doStmt, counter);
            counter.incrementAndGet(); // Increments count for each while statement
        }
    }

    /**
    * This class extends voidVisitorAdapter to count the number of do...while statements 
    */
    private static class methodNumberVisitor extends VoidVisitorAdapter<AtomicInteger> {
        @Override
        public void visit(MethodDeclaration methodDec, AtomicInteger counter) {
            super.visit(methodDec, counter);
            counter.incrementAndGet(); // Increments count for each while statement
        }
    }

    /**
     * This class extends voidVisitorAdapter to count the number switch cases
     */
    private static class switchVisitor extends VoidVisitorAdapter<AtomicInteger> {
        @Override
        public void visit(SwitchEntry switchStmt, AtomicInteger counter) {
            super.visit(switchStmt, counter);
            counter.incrementAndGet(); // Increments count for each while statement
        }
    }

}
