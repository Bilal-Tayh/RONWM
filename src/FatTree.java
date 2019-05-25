import java.util.*;
import java.io.*; 

public class FatTree {
     Pod[] podes;
     int[] core;
     Network n;
     int k;
     HashFunction hash1;
     HashFunction hash2;    
     HashFunction hash3;
     HashFunction hash4;
     HashFunction hash5;
       HashFunction hash6;
     
     
     
     
     
    public class Pod{
        int[] Aggr;
        int[] edge;
        
        public Pod(int podsNum) throws Exception{
            Aggr = new int[k/2];
            edge = new int[k/2];
            
        for(int t=0; t<Aggr.length;t++){
            int id = core.length + podsNum*k  + t;
            int[] neighbors = new int[k/2];
            int count =0;
            for(int i=(k/2)*t;i<(k/2)*(t+1);i++){
                neighbors[count] = i;
                count++;
            }
            n.addSwitch(id ,neighbors);
            Aggr[t] = id;
        }
        
        
        
         for(int t=0; t<edge.length;t++){
            int id = core.length + podsNum*k + Aggr.length  + t;
            n.addSwitch(id ,Aggr);
            edge[t] =id;
        }
        
        
        }
        
        public int getAggr(int i){
            return Aggr[i];
        }
        
        
        public int getEdge(int i){
            return edge[i];
        }
    }
     
     
     
     
     
     
     
     
     
     
    public FatTree(int ports,Network net) throws Exception{
        hash1 = new HashFunction();
        hash2 = new HashFunction();
        hash3 = new HashFunction();
        hash4 = new HashFunction();
        hash5 = new HashFunction();
        hash6 = new HashFunction();
        k = 8;
        core = new int[(int)Math.pow(k/2,2)];
        n = net;
        for(int t=0; t<core.length;t++){
            n.addSwitch(t ,new int[]{});
            core[t] = t;
        }
        
        podes = new Pod[k];
        for(int i=0;i<k;i++){
            podes[i] = new Pod(i);
        }
        
    }
    
    
    
    
    
    
    public void addFlow(int flowid, int packets,int w){
        Random rn = new Random();
        int h1 = hash1.hash(flowid,k);
        int h2 = hash2.hash(flowid,k);
        int h3 = hash3.hash(flowid,(int)k/2);
        int h4 = hash4.hash(flowid,(int)k/2);
        int h5 = hash5.hash(flowid,(int)k/2);
        int h6 = hash6.hash(flowid,(int)k/2);
        if(h1 == h2){
            if(h3 == h4){
                n.addFlow(flowid,packets,w,new int[]{podes[h2].getEdge(h4)});
            }
            else{
                int randAggr = h5;
                n.addFlow(flowid,packets,w,new int[]{podes[h1].getEdge(h3),podes[h2].getAggr(randAggr),podes[h2].getEdge(h4)});
            }
        }
        else{
                int randAggr = h5;
                int randCore = randAggr*((int)k/2) + h6;
                n.addFlow(flowid,packets,w,new int[]{podes[h1].getEdge(h3),podes[h1].getAggr(randAggr),core[randCore],podes[h2].getAggr(randAggr),podes[h2].getEdge(h4)});
        }
        
    }
    
    
    
    
    
    

}
