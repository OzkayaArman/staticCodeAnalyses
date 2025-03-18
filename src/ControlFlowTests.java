package src;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ControlFlowTests {
   String message = "HelloWorld";	
   String message2 = "HelloWorld";	

   @Test
   public void testPrintMessage() {
      assertEquals(message,message2);
   }
}
