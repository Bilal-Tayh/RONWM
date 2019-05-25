import java.util.*; 
public class NWPSswitch { 

    public class Triple{
        public double first;
        public int second;
        public int third;
        public Triple(){
            first = 0.0;
            second = 0;
            third = 0;
        }
        public Triple(double a,int b,int c){
            first = a;
            second = b;
            third = c;
        }
    
    }


    int sid;
    PriorityQueue<Triple> min_heap;
    int max;
    
    
    
    
    public NWPSswitch(int Id,int cells){
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
        while(rand ==0.0){
            rand = generator.nextDouble();
        }
        double p = (double)w/rand; 
        if(min_heap.size()!= max || p > min_heap.peek().first){
            if(min_heap.size()== max){
                min_heap.poll();
            }
            Triple t = new Triple(p, flowId, w);
            min_heap.add(t);
        }
    }
    
    
    

    
    public int getId(){
        return sid;
    }
    

    
    
    
    public int flowsNum(){
        return min_heap.size();
    }
    
    
    
    public long PSgetN(){
        PriorityQueue<Triple> MH = new PriorityQueue<Triple>(min_heap.size(), new Comparator<Triple>() {
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

        long Sum=0;
        double T = min_heap.peek().first;
        while(min_heap.size()>0){
             Triple l = min_heap.poll();
	     MH.add(l);
             Sum+=Math.max(l.third,T);
        }
	 min_heap = MH;
        return Sum;
    }

   
    

}
