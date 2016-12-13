package processorTests;

import java.util.ArrayList;
import micromips.processor.Scheduler;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import utilities.Errors;

public class TestScheduler {
   private Scheduler scheduler;
   @Before
   public void setUp() {
      scheduler = new Scheduler();
   }

   @After
   public void tearDown() {
      scheduler = null;
   }
   
   private void hasSameInstructionCount(int size) {
      assertEquals(scheduler.getInstructions().size(), size);
   }
   private void hasSameModificationCount(int size) {
      assertEquals(scheduler.getModifications().size(), size);
   }

   private void instructionCountEqualOpcodeCount() {
      assertEquals(scheduler.getOpcodes().size(), scheduler.getInstructions().size());
   }

   private void hasSameParsingErrorCount(int size) {
      hasSameListSize(Errors.getParsingErrors(), size);
   }
   
   private void hasSameRuntimeErrorCount(int size) {
      hasSameListSize(Errors.getRuntimeErrors(), size);
   }
   
   private void hasSameListSize(ArrayList al, int size){
      assertEquals(size, al.size());
   }

   @Test
   public void testSetInput(){
      scheduler.setInput(
         "BEQC r11, r12, label      " +"\n"+
         "SD r7, 1000(r9)	" +"\n"+
         " NOP 		" +"\n"+
         "label: DADDIU r1, r2, 1000			" +"\n"
      );
      hasSameParsingErrorCount(0);
      hasSameRuntimeErrorCount(0);
      hasSameInstructionCount(4);
      hasSameModificationCount(1);
      
      scheduler.applyModifications();
      scheduler.setOpcodes();
      
      instructionCountEqualOpcodeCount();
   }
}
