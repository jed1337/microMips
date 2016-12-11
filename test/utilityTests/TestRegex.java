package utilityTests;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.InputMismatchException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utilities.AssemblyRegex;
import utilities.AssemblyRegex;
import utilities.INSTRUCTION_CONTENTS;

import static org.junit.Assert.*;
import utilities.Errors;
import utilities.Errors;
import utilities.INSTRUCTION_CONTENTS;
import static utilities.INSTRUCTION_CONTENTS.*;

public class TestRegex {
   private AssemblyRegex ar;
   private ArrayList<String> parsingError;
   private HashMap<String, EnumMap<INSTRUCTION_CONTENTS, String>> validInputs;
   
   private INSTRUCTION_CONTENTS[] ica; //Instruction contents format

   @Before
   public void setUp() {
      parsingError = new ArrayList<>();
      validInputs = new HashMap<>();
   }

   @After
   public void tearDown() {
      ar = null;
      parsingError = null;
      validInputs  = null;
      ica          = null;
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
      validInputs.forEach((k,v)->{
         ar = new AssemblyRegex(k);
         assertEquals(ar.getInstructionContents(), v);
      });
      parsingError.forEach(str->{
         ar = new AssemblyRegex(str);
         assertTrue(!Errors.getParsingErrors().isEmpty());
         assertTrue(Errors.getRuntimeErrors().isEmpty());
      });
   }
   
   @Test
   public void rType(){
      setIContentsFormat(LABEL, OPCODE, RD, RS, RT, COMMENT);
      validInputs.put(
         "  label:    xor    R4, R31,R5    ;Test",
         getEM("label:","xor","4","31","5",";Test")
      );
      validInputs.put(
         "               DSUBU r0, r0, r0;            newline",
         getEM(null,"DSUBU", "0", "0", "0",";            newline")
      );
      parsingError.add("               DSUBUr0, r0, r0;            newline");
      parsingError.add("Label:DSUBU r0, r0, r0;            newline");
      parsingError.add("Label:DSUBU r0, r0, r0");
      parsingError.add("Label: DSUBU r0, r0, 0x1");
      parsingError.add("Label: DSUBU r0, r0, 1");
      test();
   }

  @Test
  public void loadStore(){
   setIContentsFormat(LABEL, OPCODE, RT, IMM, RS, COMMENT);
     validInputs.put(
        " label:       ld r1, 2000(r0)   ;Comment",
        getEM("label:","ld","1","2000","0",";Comment")
     );
     validInputs.put(
        "sd                    r3,2000(r2);",
        getEM(null,"sd","3","2000","2",";")
     );
     validInputs.put(
        " sd r3      , label           (r1)",
        getEM(null,"sd","3","label","1",null)
     );
     validInputs.put(
        "ld r1,0(r0);comment",
        getEM(null,"ld","1","0","0",";comment")
     );
     parsingError.add("ld1,0(r0);comment");
     parsingError.add("ld1,0(r0) comment");
     parsingError.add("l d1,0(r0) comment");
     parsingError.add("ld 1,0(r0) comment");
     parsingError.add("ld 1,0xf(r0); comment");
     parsingError.add("ld 1,0xcafe(r1)");
     parsingError.add("label:ld r1,cafe(r1)");
     test();
  }

  @Test
  public void bne(){
   setIContentsFormat(LABEL, OPCODE, RS, RT, IMM, COMMENT);
     validInputs.put(
        "   label: BEQC   r1, r2, label2  ;comment",
        getEM("label:", "BEQC", "1", "2", "label2", ";comment")
     );
     validInputs.put(      
        "    beqc r1,r2,destination;comment  ",
        getEM(null, "beqc", "1", "2", "destination", ";comment")
     );
     validInputs.put(      
        "Beqc r1, r2, stuff ;comment",
        getEM(null, "Beqc", "1", "2", "stuff", ";comment")
     );
     parsingError.add("Beqcr1, r2, stuff ;comment");
     parsingError.add("label:Beqc r1, r2, stuff ;comment");
     parsingError.add("label: Beqc r1, r2 stuff ;comment");
     parsingError.add("label: Beqc r1, r2; stuff ;comment");
     parsingError.add("label: Beqc r1, r2,; stuff ;comment");
     parsingError.add("label: Beqc r1, r2 ");
     parsingError.add("label: Beqc r1, r2, r4, r5 ");
     parsingError.add("Beqc r1, r2, r4, r5 ");
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
     parsingError.add("bc labl;Comment");
     parsingError.add("bc labl, label2;Comment");
     parsingError.add("lbl:bc labl, label2");
     parsingError.add("bc2 labl, label2");
     test();
  }
}
