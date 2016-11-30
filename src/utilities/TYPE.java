package utilities;

public enum TYPE {

   R("OR, DSUB, SLT, NOP"),
   I("BNE, LD, SD, DADDIU"),
   J("J");

   private final String[] validArr;

   private TYPE(String valid) {
      this.validArr = valid.toLowerCase().split("\\s*,\\s*");
   }

   public String[] getValidArr() {
      return validArr;
   }
   
   public TYPE getType(String opCode){
      for (TYPE iType : values()) {
         for (String valid : iType.getValidArr()) {
            if(valid.equalsIgnoreCase(opCode)){
               return iType;
            }
         }
      }
      return null;
   }
}

// Things they have in common
   // Label
   // Comment
   // OpCode

   // RType
   //    R1, R2, R3
   // IType
   //    as