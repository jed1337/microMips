package micromips.processor;

public abstract class InstructionSuperClass {
   protected boolean hasOldValue = false;
   protected CYCLE_NAME cyclename;
   
   protected InstructionSuperClass instance = null;

   protected InstructionSuperClass getInstance(InstructionSuperClass isc, CYCLE_NAME cycleName){
      if(instance == null){
         this.cyclename = cycleName;
         instance =  isc;
      }
      return isc;
   }
   
   public abstract void printContents();
}
