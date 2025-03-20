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
        assertEquals(new AtomicInteger(4).get(),ControlFlow.ifStatementCount.get());
    }
    @Test
    public void checkForLoopDetection(){
        assertEquals(new AtomicInteger(1).get(),ControlFlow.forStatementCount.get());
    }

    @Test
    public void checkStrongForLoopDetection(){
        assertEquals(new AtomicInteger(1).get(),ControlFlow.forEachStatementCount.get());
    }
    @Test
    public void checkWhileLoopDetection(){
        assertEquals(new AtomicInteger(1).get(),ControlFlow.whileStatementCount.get());
    }
    @Test
    public void checkDoWhileLoopDetection(){
        assertEquals(new AtomicInteger(1).get(),ControlFlow.doStatementCount.get());
    }
    @Test
    public void checkSwitchCaseDetection(){
        assertEquals(new AtomicInteger(3).get(),ControlFlow.switchCaseCount.get());
    }
    @Test
    public void checkMethodDeclerationDetection(){
        assertEquals(new AtomicInteger(3).get(),ControlFlow.methodNumberCount.get());
    }
}
