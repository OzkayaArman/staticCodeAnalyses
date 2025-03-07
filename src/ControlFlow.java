package src;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.IfStmt;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public class ControlFlow {
    
    public AtomicInteger ifStatementCount = new AtomicInteger(0);

    // Parses all Java files in the given project directory and builds the dependency graph.
    public void parseProject(File projectDir) throws Exception {
        File[] files = projectDir.listFiles((dir, name) -> name.endsWith(".java"));
        if (files == null) {
            throw new Exception("Directory not found or no Java files in: " + projectDir);
        }
        //For every file
        for (File file : files) {
            CompilationUnit cu = StaticJavaParser.parse(file);
            // Use our visitor to record dependencies from this compilation unit.
            cu.accept(new controlFlowVisitor(), ifStatementCount);
        }
    }

    private static class controlFlowVisitor extends VoidVisitorAdapter<AtomicInteger> {
        @Override
        public void visit(IfStmt ifStmt, AtomicInteger counter) {
            super.visit(ifStmt, counter);
            counter.incrementAndGet(); // Increments count for each if statement
        }
    }

}
