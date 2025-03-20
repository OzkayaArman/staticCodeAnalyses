
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.ImportDeclaration;

import java.io.File;
import java.util.*;

public class DependencyCycles extends Evaluator {

    // Dependency graph: Fully qualified class name → Set of dependencies (fully
    // qualified names)
    private Map<String, Set<String>> graph = new HashMap<>();
    private Set<List<String>> cycles = new HashSet<>();

    // Parses all Java files in the given project directory and builds the
    // dependency graph.
    public void parseProject(File projectDir) throws Exception {
        //depricated but used here to avoid excessive refactoring due to time constraints
        StaticJavaParser.getConfiguration().setLanguageLevel(LanguageLevel.JAVA_18); 

        // File[] files = projectDir.listFiles((dir, name) -> name.endsWith(".java"));
        List<File> files = new ArrayList<>();
        traverseFolder(projectDir, files);
        if (files.size() == 0)
            throw new Exception("Directory not found or no Java files in: " + projectDir);

        for (File file : files) {
            CompilationUnit cu = StaticJavaParser.parse(file);

            // Extract package name
            String packageName = cu.getPackageDeclaration().map(pd -> pd.getNameAsString()).orElse("");

            // Extract import statements
            Map<String, String> importMap = new HashMap<>();
            for (ImportDeclaration importDecl : cu.findAll(ImportDeclaration.class)) {
                String importName = importDecl.getNameAsString();
                String simpleName = importName.substring(importName.lastIndexOf('.') + 1);
                importMap.put(simpleName, importName); // Map simple class name to fully qualified name
            }

            // Visit class declarations to track dependencies
            cu.accept(new DependencyVisitor(packageName, importMap), null);
        }
    }

    // Adds a dependency from one class to another.
    private void addDependency(String from, String to) {
        if (from.equals(to))
            return; // Prevent self-dependencies
        graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }

    // Visitor to process class declarations and field dependencies
    private class DependencyVisitor extends VoidVisitorAdapter<Void> {
        private String currentClassFQN; // Fully Qualified Name of the class being visited
        private final String packageName;
        private final Map<String, String> importMap; // Maps simple class names to their full names

        public DependencyVisitor(String packageName, Map<String, String> importMap) {
            this.packageName = packageName;
            this.importMap = importMap;
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
            // Construct the fully qualified name of the current class
            currentClassFQN = packageName.isEmpty() ? n.getNameAsString() : packageName + "." + n.getNameAsString();

            // Continue visiting the body (fields, methods, etc.)
            super.visit(n, arg);
        }

        @Override
        public void visit(FieldDeclaration n, Void arg) {
            if (currentClassFQN != null) {
                // Extract field type and register dependency
                String fieldType = resolveFQN(n.getElementType().asString());
                addDependency(currentClassFQN, fieldType);
            }
            super.visit(n, arg);
        }

        // Resolves a class name to a fully qualified name
        private String resolveFQN(String className) {
            // 1. Check if the class is explicitly imported
            if (importMap.containsKey(className)) {
                return importMap.get(className);
            }
            // 2. Check if it's in the same package
            return packageName.isEmpty() ? className : packageName + "." + className;
        }
    }

    // Checks if the dependency graph contains cycles.
    public Set<List<String>> getCycles() {
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();
        ArrayList<String> cycle = new ArrayList<>();
        for (String node : graph.keySet()) {

            if (hasCycleUtil(node, visited, recStack, null)) {
                // return true;
            }
            visited = new HashSet<>();
            recStack = new HashSet<>();

        }
        removeDuplicateCycles();
        return cycles;
    }

    // Helper method for cycle detection using Depth First Search.
    private boolean hasCycleUtil(String node, Set<String> visited, Set<String> recStack, List<String> cycle) {
        if (recStack.contains(node)) {
            if (cycle != null)
                cycles.add(cycle);
            return true;
        }
        ;
        if (visited.contains(node))
            return false;

        if (cycle == null) {
            cycle = new ArrayList<String>();
        }

        visited.add(node);
        recStack.add(node);
        cycle.add(node);

        for (String neighbor : graph.getOrDefault(node, new HashSet<>())) {
            if (hasCycleUtil(neighbor, visited, recStack, new ArrayList<>(cycle)))
                return true;
        }

        recStack.remove(node);
        return false;
    }

    // remove duplicate cycles with different starting points
    private void removeDuplicateCycles() {
        Set<List<String>> newCycles = new HashSet<List<String>>();
        for (List<String> oldCycle : cycles) {
            newCycles.add(rotateList(oldCycle));
        }
        cycles = newCycles;
    }

    // Rotate list so smallest element is first
    private List<String> rotateList(List<String> list) {
        int minIndex = 0;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(list.get(minIndex)) < 0) {
                minIndex = i;
            }
        }

        Collections.rotate(list, -minIndex);
        return list;
    }

}
