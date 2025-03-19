import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

public class DependencyCyclesTest {

    // Utility method to create a Java file with the given content.
    private File createJavaFile(Path dir, String fileName, String content) throws Exception {
        Path filePath = dir.resolve(fileName);
        Files.write(filePath, content.getBytes());
        return filePath.toFile();
    }

    // Test when there are no cycles. Two classes where B depends on A but A does not depend on B.
    @Test
    void testNoCycle(@TempDir Path tempDir) throws Exception {
        Path projectDir = tempDir.resolve("project");
        Files.createDirectories(projectDir);
        
        // Create a package directory.
        Path packageDir = projectDir.resolve("com/example");
        Files.createDirectories(packageDir);
        
        String contentA = "package com.example;\n" +
                          "public class A {}";
        String contentB = "package com.example;\n" +
                          "public class B {\n" +
                          "    A a;\n" +
                          "}";
        createJavaFile(packageDir, "A.java", contentA);
        createJavaFile(packageDir, "B.java", contentB);
        
        DependencyCycles dc = new DependencyCycles();
        dc.parseProject(projectDir.toFile());
        Set<List<String>> cycles = dc.getCycles();
        
        // Assert that there is no cycle.
        assertTrue(cycles.isEmpty(), "Expected no cycles, but found: " + cycles);
    }

    // Test a direct cycle: A depends on B and B depends on A.
    @Test
    void testDirectCycle(@TempDir Path tempDir) throws Exception {
        Path projectDir = tempDir.resolve("project");
        Files.createDirectories(projectDir);
        
        Path packageDir = projectDir.resolve("com/example");
        Files.createDirectories(packageDir);
        
        String contentA = "package com.example;\n" +
                          "public class A {\n" +
                          "    B b;\n" +
                          "}";
        String contentB = "package com.example;\n" +
                          "public class B {\n" +
                          "    A a;\n" +
                          "}";
        createJavaFile(packageDir, "A.java", contentA);
        createJavaFile(packageDir, "B.java", contentB);
        
        DependencyCycles dc = new DependencyCycles();
        dc.parseProject(projectDir.toFile());
        Set<List<String>> cycles = dc.getCycles();
        
        // Assert that a cycle is detected and it includes both classes.
        assertFalse(cycles.isEmpty(), "Expected a cycle, but found none.");
        boolean foundCycle = cycles.stream().anyMatch(cycle ->
            cycle.contains("com.example.A") && cycle.contains("com.example.B")
        );
        assertTrue(foundCycle, "Cycle should include com.example.A and com.example.B");
    }

    // Test an indirect cycle: A -> B -> C -> A.
    @Test
    void testIndirectCycle(@TempDir Path tempDir) throws Exception {
        Path projectDir = tempDir.resolve("project");
        Files.createDirectories(projectDir);
        
        Path packageDir = projectDir.resolve("com/example");
        Files.createDirectories(packageDir);
        
        String contentA = "package com.example;\n" +
                          "public class A {\n" +
                          "    B b;\n" +
                          "}";
        String contentB = "package com.example;\n" +
                          "public class B {\n" +
                          "    C c;\n" +
                          "}";
        String contentC = "package com.example;\n" +
                          "public class C {\n" +
                          "    A a;\n" +
                          "}";
        createJavaFile(packageDir, "A.java", contentA);
        createJavaFile(packageDir, "B.java", contentB);
        createJavaFile(packageDir, "C.java", contentC);
        
        DependencyCycles dc = new DependencyCycles();
        dc.parseProject(projectDir.toFile());
        Set<List<String>> cycles = dc.getCycles();
        
        // Assert that a cycle is detected and it includes all three classes.
        assertFalse(cycles.isEmpty(), "Expected a cycle, but found none.");
        boolean foundCycle = cycles.stream().anyMatch(cycle ->
            cycle.contains("com.example.A") &&
            cycle.contains("com.example.B") &&
            cycle.contains("com.example.C")
        );
        assertTrue(foundCycle, "Cycle should include com.example.A, com.example.B, and com.example.C");
    }

    // Test when there are no Java files in the directory.
    @Test
    void testNoJavaFiles(@TempDir Path tempDir) throws Exception {
        Path emptyDir = tempDir.resolve("emptyProject");
        Files.createDirectories(emptyDir);
        
        DependencyCycles dc = new DependencyCycles();
        Exception exception = assertThrows(Exception.class, () -> {
            dc.parseProject(emptyDir.toFile());
        });
        String expectedMessage = "Directory not found or no Java files in:";
        assertTrue(exception.getMessage().contains(expectedMessage),
                   "Expected exception message to contain '" + expectedMessage + "'");
    }

    // Test import resolution: one class imports another.
    @Test
    void testImportResolution(@TempDir Path tempDir) throws Exception {
        Path projectDir = tempDir.resolve("project");
        Files.createDirectories(projectDir);
        
        Path packageDir = projectDir.resolve("com/example");
        Files.createDirectories(packageDir);
        
        String contentA = "package com.example;\n" +
                          "public class A {}";
        String contentB = "package com.example;\n" +
                          "import com.example.A;\n" +
                          "public class B {\n" +
                          "    A a;\n" +
                          "}";
        createJavaFile(packageDir, "A.java", contentA);
        createJavaFile(packageDir, "B.java", contentB);
        
        DependencyCycles dc = new DependencyCycles();
        dc.parseProject(projectDir.toFile());
        Set<List<String>> cycles = dc.getCycles();
        
        // Even though B depends on A, there should be no cycle.
        assertTrue(cycles.isEmpty(), "Expected no cycles, but found: " + cycles);
    }
}
