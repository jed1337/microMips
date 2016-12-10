
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.InputMismatchException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utilities.AssemblyRegex;
import utilities.INSTRUCTION_CONTENTS;

import static org.junit.Assert.*;
import static utilities.INSTRUCTION_CONTENTS.*;

public class TestRegex {
   private AssemblyRegex ar;
   private HashMap<String, EnumMap<INSTRUCTION_CONTENTS, String>> invalidInputs;
   private HashMap<String, EnumMap<INSTRUCTION_CONTENTS, String>> validInputs;
   
   private INSTRUCTION_CONTENTS[] ica; //Instruction contents format

   @Before
   public void setUp() {
      invalidInputs = new HashMap<>();
      validInputs = new HashMap<>();
   }

   @After
   public void tearDown() {
      ar = null;
      invalidInputs = null;
      validInputs   = null;
      ica           = null;
   }
   
   private void setIContentsFormat(INSTRUCTION_CONTENTS... ica){
      this.ica = ica;
   }
   
   private EnumMap<INSTRUCTION_CONTENTS, String> getEM(String... strings){
      if(strings.length != ica.length){
         throw new InputMismatchException(
            String.format("ica.length = %d. stirngs.length = %d"
            , ica.length, strings.length));
      }
      EnumMap<INSTRUCTION_CONTENTS, String> em = new EnumMap<>(INSTRUCTION_CONTENTS.class);
      for (int i = 0; i < ica.length; i++) {
         if(strings[i]==null){
            continue;
         }
         em.put(ica[i], strings[i]);
      }
      return em;
   }
   
   public void test(){
//      invalidInputs.forEach((k,v)->{
//         assertEquals(v, ar.getMatchedGroups(pattern, k));
//      });
      validInputs.forEach((k,v)->{
         ar = new AssemblyRegex(k);
         assertEquals(ar.getInstructionContents(), v);
      });
   }
   
   @Test
   public void rType(){
      setIContentsFormat(LABEL, OPCODE, RD, RS, RT, COMMENT);
      validInputs.put(
         "  label:    xor    R4, R31,R5    ;Test",
         getEM("label:","xor","R4","R31","R5",";Test")
      );
      validInputs.put(
         "               DSUBU r0, r0, r0;            newline",
         getEM(null,"DSUBU", "r0", "r0", "r0",";            newline")
      );
      test();
   }

  @Test
  public void loadStore(){
   setIContentsFormat(LABEL, OPCODE, RT, IMM, RS, COMMENT);
     validInputs.put(
        " label:       ld r1, 2000(r0)   ;Comment",
        getEM("label:","ld","r1","2000","r0",";Comment")
     );
     validInputs.put(
        "sd                    r3,2000(r2);",
        getEM(null,"sd","r3","2000","r2",";")
     );
     validInputs.put(
        " sd r3      , label           (r1)",
        getEM(null,"sd","r3","label","r1",null)
     );
     validInputs.put(
        "ld r1,0(r0);comment",
        getEM(null,"ld","r1","0","r0",";comment")
     );
     test();
  }

  @Test
  public void bne(){
   setIContentsFormat(LABEL, OPCODE, RS, RT, IMM, COMMENT);
     validInputs.put(
        "   label: BEQC   r1, r2, label2  ;comment",
        getEM("label:", "BEQC", "r1", "r2", "label2", ";comment")
     );
     validInputs.put(      
        "    beqc r1,r2,destination;comment  ",
        getEM(null, "beqc", "r1", "r2", "destination", ";comment")
     );
     validInputs.put(      
        "Beqc r1, r2, stuff ;comment",
        getEM(null, "Beqc", "r1", "r2", "stuff", ";comment")
     );
     test();
  }

  @Test
  public void bc(){
   setIContentsFormat(LABEL, OPCODE, IMM, COMMENT);
     validInputs.put(
        "label: bc labelTo ;Comment",
        getEM("label:", "bc", "labelTo", ";Comment")
     );
     validInputs.put(
        "label: bc labelTo;Comment",
        getEM("label:", "bc", "labelTo", ";Comment")
     );
     validInputs.put(
        "bc labl;Comment",
        getEM(null, "bc", "labl", ";Comment")
     );
     test();
  }
}
