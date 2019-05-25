import java.util.*;

public class HashFunction {
    int randomXor;
    int randomRightShift;
    
    
    public HashFunction(){
        Random rn = new Random();
        randomXor = rn.nextInt();
        randomRightShift = rn.nextInt();
    }
   
   
   /*
   *
   * return xor(a,b)
   *
   */
    private int bitsXor(int a, int b){
        return a^b;
    }
   
   
   
   /*
   *
   * shift a(bits) to the rigth by k
   * and return the result
   *
   */
    private int rtCircShift(int a, int k)
    {
        return (a >>> k) | (a << (Integer.SIZE - k));
    }
   
    
    
   /*
   *
   * hash "num" to a range(0,max)
   *
   */
    public int hash(int num,int max){
        if(max>0){
            int a = String.valueOf(num).hashCode();
            int b = String.valueOf(randomRightShift).hashCode();
            int shiftedNum = rtCircShift(a,b);
            int xoredShiftedNum = bitsXor(shiftedNum,randomXor);
            int c = String.valueOf(xoredShiftedNum).hashCode();
            return (Math.abs(c))%(max);
        }
        return -1;
    }
    
    
    
    
    
    
    
      public int h(int num){
            int a = String.valueOf(num).hashCode();
            int b = String.valueOf(randomRightShift).hashCode();
            int shiftedNum = rtCircShift(a,b);
            int xoredShiftedNum = bitsXor(shiftedNum,randomXor);
            int c = String.valueOf(xoredShiftedNum).hashCode();
            return Math.abs(c);
    }
    
    
    
    
 
   
} 
