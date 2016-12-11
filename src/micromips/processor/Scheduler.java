package micromips.processor;

import java.util.ArrayList;
import utilities.AssemblyRegex;
import utilities.Errors;

public class Scheduler {
   private ArrayList<Integer> iOpcodes;
   
   public Scheduler(String jText){
      iOpcodes = new ArrayList<>();
      
      for(String input: jText.split("\\n")){
         AssemblyRegex ar = new AssemblyRegex(input);
//         ar.getInstructionContents()
         
      }
      if(!Errors.getParsingErrors().isEmpty()){
         //There are wrong syntaxes. Stop
      }
   }
}
