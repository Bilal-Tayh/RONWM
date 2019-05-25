/*


    this class simulates a Network,
    
    after constructing this class call addSwitch() function to add switches to the network.
    
    and after constructing the network if you want a packet to run in the network call addFlow().
    
    after you finish adding all the flows then you can call any URWA or NWPS functions.....





*/




import java.util.*;


class SwitchDontExistException extends Exception {

  public SwitchDontExistException(int id){
     super("switch id: "+ Integer.toString(id) +" doesnt exist");
  }

}

public class Network{ 
    Map<Integer,NWPSswitch> networkNWPSswitches;
    Map<Integer,UWRAswitch> networkSwitches;
    int tSize;
    boolean compined;

    public Network(int tableSize){
        networkSwitches = new HashMap<Integer,UWRAswitch>();
        networkNWPSswitches = new HashMap<Integer,NWPSswitch>();
        tSize = tableSize;
        compined = false;
    }
    
    
    
    
/*
 * add switch to the network, the sId should be unique and no two switches should share the same switchesId
 * neighbors should contain sId of switches that aleady in the network.
 *
*/
   public void addSwitch(int sId ,int[] neighbors) throws SwitchDontExistException {
         Integer[] ns = Arrays.stream( neighbors ).boxed().toArray( Integer[]::new );
        networkSwitches.put(sId,new UWRAswitch(sId,tSize));
        networkNWPSswitches.put(sId,new NWPSswitch(sId,tSize));
   }
    
    
    
/*
 *
 * flowId: is the id of the flow, should be unique and no two flows should have the same flowId.
 * packetID: each packet that the flow send should have a diffrent id.
 * switchesId: the ids of the switches in the path that the packet will travel through.
 *
*/
    public void addFlow(int flowId, int packetID, int w, int[] switchesId){
        for(int i=0; i<switchesId.length; i++){
            if(networkSwitches.containsKey(switchesId[i])){
                if(w>0){
                    networkSwitches.get(switchesId[i]).addFlow(flowId,packetID,w);
                    networkNWPSswitches.get(switchesId[i]).addFlow(flowId,packetID,w);
                }
            }
        }
    
    }
    
    
    
    
    public UWRAswitch getSwitch(int id) throws SwitchDontExistException {
         if(networkSwitches.containsKey(id)){
             return networkSwitches.get(id);
         }
         else{
            throw new SwitchDontExistException(id);
         }
    }
    
    
      public NWPSswitch getNWPSswitch(int id) throws SwitchDontExistException {
         if(networkNWPSswitches.containsKey(id)){
             return networkNWPSswitches.get(id);
         }
         else{
            throw new SwitchDontExistException(id);
         }
    }
    
    
    
    public UWRAswitch[] getSwitchs() {
        UWRAswitch[] s = new UWRAswitch[networkSwitches.size()];
        int i=0;
        for(int sId : networkSwitches.keySet()){
            s[i] = networkSwitches.get(sId);
            i++;
        }
        return s;
    }
    
    
    
    
    public NWPSswitch[] getNWPSswitchs() {
        NWPSswitch[] s = new NWPSswitch[networkNWPSswitches.size()];
        int i=0;
        for(int sId : networkNWPSswitches.keySet()){
            s[i] = networkNWPSswitches.get(sId);
            i++;
        }
        return s;
    }
    
    
      public int[] getSwitchsIds() {
        int[] s = new int[networkSwitches.size()];
        int i=0;
        for(int sId : networkSwitches.keySet()){
            s[i] = sId;
            i++;
        }
        return s;
    }
    
    
    public int[] getNWPSswitchsIds() {
        int[] s = new int[networkNWPSswitches.size()];
        int i=0;
        for(int sId : networkNWPSswitches.keySet()){
            s[i] = sId;
            i++;
        }
        return s;
    }
    
    public int switchesNum(){
        return networkSwitches.size();
    }
    
    
    PriorityQueue<UWRAswitch.Triple> min_heap;
    PriorityQueue<NWPSswitch.Triple> Pmin_heap;

    

    /*
    *
    * its very important to call this function before calling any of the URWA or NWPS functions.
    *
    */
    public void compine(){
        min_heap = new PriorityQueue<UWRAswitch.Triple>(tSize, new Comparator<UWRAswitch.Triple>() {
            public int compare(UWRAswitch.Triple t1, UWRAswitch.Triple t2) {
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
        Set<Double> a = new HashSet<Double>();
        for(int i=0; i<networkSwitches.size();i++){
            UWRAswitch s = networkSwitches.get(i);
            while(s.min_heap.size()>0){
                UWRAswitch.Triple t = s.min_heap.poll();
                if(!a.contains(t.first)){
                    a.add(t.first);
                    min_heap.add(t);
                }
            }
	    s.min_heap = null;
        }
         Pmin_heap = new PriorityQueue<NWPSswitch.Triple>(tSize, new Comparator<NWPSswitch.Triple>() {
            public int compare(NWPSswitch.Triple t1, NWPSswitch.Triple t2) {
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
    
    
        Set<Double> aa = new HashSet<Double>();
        for(int i=0; i<networkNWPSswitches.size();i++){
            NWPSswitch ps = networkNWPSswitches.get(i);
            while(ps.min_heap.size()>0){
                NWPSswitch.Triple t = ps.min_heap.poll();
                if(!aa.contains(t.first)){
                    aa.add(t.first);
                    Pmin_heap.add(t);
                }
            }
	    ps.min_heap = null;
        }
        compined = true;
    }
    
    
    
    
    
    
    
///////////////////////////////////////////////////////////////////////////// URWA FUNCTIONS /////////////////////////////////////////////////////////////////////////////
//	    
//
//
//
//
//
    public double getN(){
        if(!compined){
            compine();
        }
        double T = min_heap.peek().first;
        return (double)min_heap.size()/(1.0-T);
    }
    

    
    
    
    public  Map<Integer,Double> SgetHeap(){
    if(!compined){
            compine();
        }
        PriorityQueue<UWRAswitch.Triple> MH = new PriorityQueue<UWRAswitch.Triple>(min_heap.size(), new Comparator<UWRAswitch.Triple>() {
                public int compare(UWRAswitch.Triple t1, UWRAswitch.Triple t2) {
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

            
            Map<Integer,Double> flowsCount = new HashMap<Integer,Double>();
            double T = min_heap.peek().first;
            double N = (double)min_heap.size()/(1.0-T);
            double P = min_heap.size()/N;
            
            while(min_heap.size()>0){
                UWRAswitch.Triple l = min_heap.poll();
            MH.add(l);
                if(! flowsCount.containsKey(l.second)){
                    flowsCount.put(l.second,1.0/P);
                }
                else{
                    flowsCount.replace(l.second,flowsCount.get(l.second)+1.0/P);
                }
            }
        min_heap = MH;
            return flowsCount;
    }

        
        
        
        
    public  Map<Integer,Double> SgetHeapNphi(double phi){
    if(!compined){
            compine();
        }
        double epsoilon = phi/10;
        double T = min_heap.peek().first;
        double N = (double)min_heap.size()/(1.0-T);
        int S = (int)((phi - epsoilon) * getN());
        PriorityQueue<UWRAswitch.Triple> MH = new PriorityQueue<UWRAswitch.Triple>(min_heap.size(), new Comparator<UWRAswitch.Triple>() {
                public int compare(UWRAswitch.Triple t1, UWRAswitch.Triple t2) {
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

            
            Map<Integer,Double> flowsCount = new HashMap<Integer,Double>();
        
            double P = min_heap.size()/N;
            while(min_heap.size()>0){
                UWRAswitch.Triple l = min_heap.poll();
            MH.add(l);
                if(! flowsCount.containsKey(l.second)){
                    flowsCount.put(l.second,1.0/P);
                }
                else{
                    flowsCount.replace(l.second,flowsCount.get(l.second)+1.0/P);
                }
            }
        min_heap = MH;
        Map<Integer,Double> topHitters = new HashMap<Integer,Double>();
        for (Integer name: flowsCount.keySet()){
            if(flowsCount.get(name)>=S){
                topHitters.put(name,flowsCount.get(name));
            }
        }
            return topHitters;
    }
//
//
//
//
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////









//                             @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                             //
    
    
    
    
    
    
    
    
    
///////////////////////////////////////////////////////////////////////////// NWPS FUNCTIONS /////////////////////////////////////////////////////////////////////////////
//	    
//
//
//
//
//


    
    public long PSgetN(){
         if(!compined){
            compine();
        }
        PriorityQueue<NWPSswitch.Triple> MH = new PriorityQueue<NWPSswitch.Triple>(Pmin_heap.size(), new Comparator<NWPSswitch.Triple>() {
        public int compare(NWPSswitch.Triple t1, NWPSswitch.Triple t2) {
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
        double T = Pmin_heap.peek().first;
        while(Pmin_heap.size()>0){
             NWPSswitch.Triple l = Pmin_heap.poll();
	     MH.add(l);
             Sum+=Math.max(l.third,T);
        }
	 Pmin_heap = MH;
        return Sum;
    }
    
   
   
    
    public  Map<Integer,Double> PSgetHeap(){
        if(!compined){
            compine();
        }
        PriorityQueue<NWPSswitch.Triple> MH = new PriorityQueue<NWPSswitch.Triple>(Pmin_heap.size(), new Comparator<NWPSswitch.Triple>() {
            public int compare(NWPSswitch.Triple t1, NWPSswitch.Triple t2) {
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

        Map<Integer,Double> flowsCount = new HashMap<Integer,Double>();
        double T = Pmin_heap.peek().first;
        while(Pmin_heap.size()>0){
             NWPSswitch.Triple l = Pmin_heap.poll();
	     MH.add(l);
             if(! flowsCount.containsKey(l.second)){
                flowsCount.put(l.second,Math.max(l.third,T));
            }
            else{
                flowsCount.replace(l.second,flowsCount.get(l.second)+Math.max(l.third,T));
            }
        }
        Pmin_heap = MH;
        return flowsCount;
    }
    
    
    

    
    
    
    
    
    
    
    public  Map<Integer,Double> PSgetHeapNphi(double phi){
    if(!compined){
            compine();
        }
        double epsoilon = phi/10;
        int S = (int)((phi - epsoilon) * (PSgetN()));
        PriorityQueue<NWPSswitch.Triple> MH = new PriorityQueue<NWPSswitch.Triple>(Pmin_heap.size(), new Comparator<NWPSswitch.Triple>() {
            public int compare(NWPSswitch.Triple t1, NWPSswitch.Triple t2) {
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
        double T = Pmin_heap.peek().first;
        Map<Integer,Double> flowsCount = new HashMap<Integer,Double>();
        while(Pmin_heap.size()>0){
             NWPSswitch.Triple l = Pmin_heap.poll();
	     MH.add(l);
             if(! flowsCount.containsKey(l.second)){
                flowsCount.put(l.second,Math.max(l.third,T));
            }
            else{
                flowsCount.replace(l.second,flowsCount.get(l.second)+Math.max(l.third,T));
            }
        }
	 Pmin_heap = MH;
       Map<Integer,Double> topHitters = new HashMap<Integer,Double>();
     for (Integer name: flowsCount.keySet()){
		if(flowsCount.get(name)>=S){
			topHitters.put(name,flowsCount.get(name));
		}
	}
        return topHitters;
    }

//
//
//
//
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



}




