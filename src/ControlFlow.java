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


public class ControlFlow extends Evaluator {
    
    public static final IntegerCustom ifStatementCount = new IntegerCustom(0);
    public static final IntegerCustom  forStatementCount =  new IntegerCustom(0);
    public static final IntegerCustom  forEachStatementCount =  new IntegerCustom(0);
    public static final IntegerCustom  whileStatementCount = new IntegerCustom(0);
    public static final IntegerCustom  doStatementCount = new IntegerCustom(0);
    public static final IntegerCustom  switchCaseCount = new IntegerCustom(0);
    public static final IntegerCustom  methodNumberCount = new IntegerCustom(0);
    
    

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
            VoidVisitorAdapter<IntegerCustom> ifVisitor = new ifVisitor();
            VoidVisitorAdapter<IntegerCustom> forVisitor = new forVisitor();
            VoidVisitorAdapter<IntegerCustom> forEachVisitor = new forEachVisitor();
            VoidVisitorAdapter<IntegerCustom> whileVisitor = new whileVisitor();
            VoidVisitorAdapter<IntegerCustom> doWhileVisitor = new doWhileVisitor();
            VoidVisitorAdapter<IntegerCustom> methodNumberVisitor = new methodNumberVisitor();
            VoidVisitorAdapter<IntegerCustom> switchCaseVisitor = new switchVisitor();
            
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
        int decisionPoints = ControlFlow.ifStatementCount.getValue() + ControlFlow.forEachStatementCount.getValue() + ControlFlow.forStatementCount.getValue() + ControlFlow.whileStatementCount.getValue() + ControlFlow.switchCaseCount.getValue();
        return (decisionPoints / ControlFlow.methodNumberCount.getValue()) + 1;
    }
    /**
     * This class extends voidVisitorAdapter to count the number of if statements 
     */
    private static class ifVisitor extends VoidVisitorAdapter<IntegerCustom> {
        @Override
        public void visit(IfStmt ifStmt, IntegerCustom counter) {
            super.visit(ifStmt, counter);
            counter.increment(); // Increments count for each if statement
        }
    }

    /**
     * This class extends voidVisitorAdapter to count the number of for statements 
     */
    private static class forVisitor extends VoidVisitorAdapter<IntegerCustom> {
        @Override
        public void visit(ForStmt forStmt, IntegerCustom counter) {
            super.visit(forStmt, counter);
            counter.increment(); // Increments count for each for statement
        }
    }


    /**
     * This class extends voidVisitorAdapter to count the number of for statements 
     */
    private static class forEachVisitor extends VoidVisitorAdapter<IntegerCustom> {
        @Override
        public void visit(ForEachStmt forEachStmt, IntegerCustom counter) {
            super.visit(forEachStmt, counter);
            counter.increment(); // Increments count for each for statement
        }
    }

    /**
     * This class extends voidVisitorAdapter to count the number of while statements 
     */
    private static class whileVisitor extends VoidVisitorAdapter<IntegerCustom> {
        @Override
        public void visit(WhileStmt whileStmt, IntegerCustom counter) {
            super.visit(whileStmt, counter);
            counter.increment(); // Increments count for each while statement
        }
    }

    /**
     * This class extends voidVisitorAdapter to count the number of do...while statements 
     */
    private static class doWhileVisitor extends VoidVisitorAdapter<IntegerCustom> {
        @Override
        public void visit(DoStmt doStmt, IntegerCustom counter) {
            super.visit(doStmt, counter);
            counter.increment(); // Increments count for each while statement
        }
    }

    /**
    * This class extends voidVisitorAdapter to count the number of do...while statements 
    */
    private static class methodNumberVisitor extends VoidVisitorAdapter<IntegerCustom> {
        @Override
        public void visit(MethodDeclaration methodDec, IntegerCustom counter) {
            super.visit(methodDec, counter);
            counter.increment(); // Increments count for each while statement
        }
    }

    /**
     * This class extends voidVisitorAdapter to count the number switch cases
     */
    private static class switchVisitor extends VoidVisitorAdapter<IntegerCustom> {
        @Override
        public void visit(SwitchEntry switchStmt, IntegerCustom counter) {
            super.visit(switchStmt, counter);
            counter.increment(); // Increments count for each while statement
        }
    }

}
