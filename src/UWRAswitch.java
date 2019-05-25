import java.util.*; 

public class UWRAswitch { 

    public class Triple{
        public double first;
        public int second;
        public int third;
        public Triple(){
            first = 0.0;
            second = 0;
        }
        public Triple(double a,int b,int c){
            first = a;
            second = b;
        }
    
    }


    int sid;
    PriorityQueue<Triple> min_heap;
    int max;
    
    
    
    
    public UWRAswitch(int Id,int cells){
            min_heap = new PriorityQueue<Triple>(cells, new Comparator<Triple>() {
            public int compare(Triple t1, Triple t2) {
                if ((t1.first - t2.first)>0){
                    return 1;
                }
                else if((t1.first - t2.first)<0){
                    return -1;
                }
                else{
                    return 0;
                }
            }
        });
        sid = Id;
        max = cells;
    }
    
    
    
    
    
    public void addFlow(int flowId, int packetId, int w){
        int tempPacketId = flowId;
        int newid = packetId;
        while(tempPacketId>1){
            int n = tempPacketId%10;
            tempPacketId/=10;
            newid*=10;
            newid+=n;
        }
        Random generator = new Random(newid);
        double rand = generator.nextDouble();
        double p = Math.pow(rand,1.0/(double)w);
        int c=0;
        while(c<w  && ( min_heap.size()==0 || p > min_heap.peek().first)){
            if(min_heap.size() == max){
                min_heap.poll();
            }
            Triple t = new Triple(p, flowId, packetId);  
            min_heap.add(t);
            c++;
            rand = generator.nextDouble();
            p*=Math.pow(rand,1.0/(double)(w-c));
        }
    }
    

    
    
    public int getId(){
        return sid;
    }
    
    
    
    /*
    * returns an estimation of the totalWeightsNumber. 
    *
    */
    public double getN(){
        double T = min_heap.peek().first;
        return (double)(flowsNum())/(1.0-T);
    }
    
    
    
    public int flowsNum(){
        return min_heap.size();
    }
    

}
