import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.atomic.AtomicInteger;

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
        assertEquals(4,ControlFlow.ifStatementCount.getValue());
    }
    @Test
    public void checkForLoopDetection(){
        assertEquals(1,ControlFlow.forStatementCount.getValue());
    }

    @Test
    public void checkStrongForLoopDetection(){
        assertEquals(1,ControlFlow.forEachStatementCount.getValue());
    }
    @Test
    public void checkWhileLoopDetection(){
        assertEquals(1,ControlFlow.whileStatementCount.getValue());
    }
    @Test
    public void checkDoWhileLoopDetection(){
        assertEquals(1,ControlFlow.doStatementCount.getValue());
    }
    @Test
    public void checkSwitchCaseDetection(){
        assertEquals(3,ControlFlow.switchCaseCount.getValue());
    }
    @Test
    public void checkMethodDeclerationDetection(){
        assertEquals(3,ControlFlow.methodNumberCount.getValue());
    }
}
