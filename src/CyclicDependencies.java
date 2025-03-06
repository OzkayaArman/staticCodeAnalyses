package staticCodeAnalyses.src;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CyclicDependencies {

    // Graph representing dependencies: key is a class, value is a set of classes it depends on.
    private Map<String, Set<String>> graph = new HashMap<>();

    // Adds a dependency from one class to another.
    public void addDependency(String from, String to) {
        // Prevent self-dependency
        if (from.equals(to)) {
            return;
        }
        graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }

    // Parses all Java files in the given project directory and builds the dependency graph.
    public void parseProject(File projectDir) throws Exception {
        File[] files = projectDir.listFiles((dir, name) -> name.endsWith(".java"));
        if (files == null) {
            throw new Exception("Directory not found or no Java files in: " + projectDir);
        }
        for (File file : files) {
            CompilationUnit cu = StaticJavaParser.parse(file);
            // Use our visitor to record dependencies from this compilation unit.
            cu.accept(new DependencyVisitor(), null);
        }
    }

    // Private inner class to visit class/interface declarations and field declarations.
    private class DependencyVisitor extends VoidVisitorAdapter<Void> {
        // Holds the current class name while visiting its members.
        private String currentClass;

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
            // Set the current class name.
            currentClass = n.getNameAsString();

            // Capture inheritance dependencies (extends)
            n.getExtendedTypes().forEach(ext -> addDependency(currentClass, ext.getNameAsString()));

            // Capture interface implementation dependencies (implements)
            n.getImplementedTypes().forEach(impl -> addDependency(currentClass, impl.getNameAsString()));

            // Continue to visit the body (including fields)
            super.visit(n, arg);

            // Clear the current class after finishing this declaration.
            currentClass = null;
        }

        @Override
        public void visit(FieldDeclaration n, Void arg) {
            if (currentClass != null) {
                // Extract the field's type (as a string) and add it as a dependency.
                String fieldType = n.getElementType().asString();
                addDependency(currentClass, fieldType);
            }
            super.visit(n, arg);
        }
    }

    // Checks if the dependency graph contains any cycles.
    public boolean hasCycle() {
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        // For each node in the graph, try to find a cycle.
        for (String node : graph.keySet()) {
            if (hasCycleUtil(node, visited, recStack)) {
                return true;
            }
        }
        return false;
    }

    // Helper method for cycle detection using Depth First Search.
    private boolean hasCycleUtil(String node, Set<String> visited, Set<String> recStack) {
        if (recStack.contains(node)) {
            return true;
        }
        if (visited.contains(node)) {
            return false;
        }

        visited.add(node);
        recStack.add(node);

        for (String neighbor : graph.getOrDefault(node, new HashSet<>())) {
            if (hasCycleUtil(neighbor, visited, recStack)) {
                return true;
            }
        }

        recStack.remove(node);
        return false;
    }
}
