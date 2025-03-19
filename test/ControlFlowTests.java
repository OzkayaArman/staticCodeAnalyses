import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class ControlFlowTests {
    
    @BeforeAll
    public static void setup() {
        ControlFlow controlFlow = new ControlFlow();
        File controlDir = new File("examples/control");

        try {
            controlFlow.parseProject(controlDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void checkCyclometicComplexity(){
        assertEquals(4,ControlFlow.calculateCyclometicComplexity());
    }
    @Test
    public void ifStatementDetection(){
        assertEquals(4,ControlFlow.ifStatementCount);
    }
    @Test
    public void checkForLoopDetection(){
        assertEquals(1,ControlFlow.forStatementCount);
    }

    @Test
    public void checkStrongForLoopDetection(){
        assertEquals(1,ControlFlow.forEachStatementCount);
    }
    @Test
    public void checkWhileLoopDetection(){
        assertEquals(1,ControlFlow.whileStatementCount);
    }
    @Test
    public void checkDoWhileLoopDetection(){
        assertEquals(1,ControlFlow.doStatementCount);
    }
    @Test
    public void checkSwitchCaseDetection(){
        assertEquals(3,ControlFlow.switchCaseCount);
    }
    @Test
    public void checkMethodDeclerationDetection(){
        assertEquals(2,ControlFlow.methodNumberCount);
    }
}
