package utilities;

import java.util.ArrayList;

public class Errors {
   private static final ArrayList<MipsException> parsingErrors = new ArrayList<>();
   private static final ArrayList<MipsException> runtimeErrors = new ArrayList<>();

   public static void addParsingError(String instruction, String message){
      Errors.parsingErrors.add(new MipsException(instruction, message));
   }

   public static ArrayList<MipsException> getParsingErrors() {
      return Errors.parsingErrors;
   }

   public static void addRuntimeError(String message){
      Errors.runtimeErrors.add(new MipsException(message));
   }

   public static ArrayList<MipsException> getRuntimeErrors() {
      return runtimeErrors;
   }
}
