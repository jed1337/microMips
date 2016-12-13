package micromips.processor;

public enum CYCLE_NAME {
   IF(1),
   ID(2),
   EX(3),
   MEM(4),
   WB(5);

   private final int num;

   private CYCLE_NAME(int num) {
      this.num = num;
   }

   public int getNum() {
      return num;
   }
   
   public boolean isPreviousCycle(CYCLE_NAME that){
      return this.num < that.num;
   }
   
   public boolean isGreaterThanOrEqual(CYCLE_NAME that){
      return this.num >= that.num;
   }

   boolean isEqual(CYCLE_NAME that) {
      return this.num == that.num;
   }
}
