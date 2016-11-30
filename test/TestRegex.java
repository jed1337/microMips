
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.AssemblyRegex;

import static org.junit.Assert.*;

public class TestRegex {
   private AssemblyRegex ar;
   private HashMap<String, List<String>> invalidInputs;
   private HashMap<String, List<String>> validInputs;

   public TestRegex() {
   }

   @BeforeClass
   public static void setUpClass() {
   }

   @AfterClass
   public static void tearDownClass() {
   }

   @Before
   public void setUp() {
      ar = new AssemblyRegex();
      invalidInputs = new HashMap<>();
      validInputs = new HashMap<>();
   }

   @After
   public void tearDown() {
   }
   
   public void test(Pattern pattern){
      validInputs.forEach((k,v)->{
         assertEquals(v, ar.getMatchedGroups(pattern, k));
      });
      invalidInputs.forEach((k,v)->{
         assertEquals(v, ar.getMatchedGroups(pattern, k));
      });
   }

   @Test
   public void rType() {
      validInputs.put(
         "  label:    O2R    R4, R31,R5    ;Test",
         Arrays.asList("label:","O2R","R4","R31","R5",";Test")
      );
      validInputs.put(
         "               slt r0, r0, r0;            newline",
         Arrays.asList(null,"slt", "r0", "r0", "r0",";            newline")
      );
      test(ar.R_TYPE);
   }

   @Test
   public void loadStore(){
      validInputs.put(
         " label:       ld r1, 2000(r0)   ;Comment",
         Arrays.asList("label:","ld","r1","2000","r0",";Comment")
      );
      validInputs.put(
         "sd                    r3,2000(r2);",
         Arrays.asList(null,"sd","r3","2000","r2",";")
      );
      validInputs.put(
         " sd r3      , label           (r1)",
         Arrays.asList(null,"sd","r3","label","r1",null)
      );
      validInputs.put(
         "ld r1,0(r0);comment",
         Arrays.asList(null,"ld","r1","0","r0",";comment")
      );
      test(ar.LOAD_STORE);
   }
   
   @Test
   public void data(){
      validInputs.put(
         "b: .byte 0x23",
         Arrays.asList("b:",".byte","0x23")
      );
      validInputs.put(      
         "s:    .word16   1337",
         Arrays.asList("s:", ".word16","1337")
      );
      validInputs.put(      
         "word: .word32     0xABCDEF01",
         Arrays.asList("word:",".word32","0xABCDEF01")
      );
      validInputs.put(      
         "asdf64: .word64 1",
         Arrays.asList("asdf64:",".word64","1")
      );
      test(ar.DATA);
   }

   @Test
   public void bne(){
      validInputs.put(
         "label: bne   r1, r2, label2  ;comment",
         Arrays.asList("label:", "bne", "r1", "r2", "label2", ";comment")
      );
      validInputs.put(      
         "    bne r1,r2,destination;comment  ",
         Arrays.asList(null, "bne", "r1", "r2", "destination", ";comment")
      );
      validInputs.put(      
         "bne r1, r2, destination ;comment",
         Arrays.asList(null, "bne", "r1", "r2", "destination", ";comment")
      );
      test(ar.BNE);
   }

   @Test
   public void bc(){
      validInputs.put(
         "label: bc labelTo ;Comment",
         Arrays.asList("label:", "bc", "labelTo", ";Comment")
      );
      validInputs.put(
         "label: bc labelTo;Comment",
         Arrays.asList("label:", "bc", "labelTo", ";Comment")
      );
      validInputs.put(
         "bc labl;Comment",
         Arrays.asList(null, "bc", "labl", ";Comment")
      );
      test(ar.BC);
   }
}
