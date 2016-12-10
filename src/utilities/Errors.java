package utilities;

import java.util.ArrayList;

public class Errors {
   private static Errors instance = null;
   
   private final ArrayList<mipsException> mipsExceptions;
   
   public static Errors getInstance(){
      if (instance == null) {
         instance = new Errors();
      } 
      return instance;
   }
   
   private Errors(){
      mipsExceptions = new ArrayList<>();
   }
   
   public void addException(mipsException me){
      instance.addException(me);
   }

   public ArrayList<mipsException> getMipsExceptions() {
      return mipsExceptions;
   }
}
