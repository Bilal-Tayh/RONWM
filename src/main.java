import java.util.*;
import java.io.*;

public class main
{
    static BufferedWriter writer;
     
	public static void main(String [] args)throws Exception
	{	
	    writer = new BufferedWriter(new FileWriter("./result.txt", true));
            int flowsNum =200000000;
            double phi = 0.001;
            
            int rounds = 10;

            File file = new File(args[0]);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) (4.4448E8)];
            fis.read(data);
            InputStream is = new ByteArrayInputStream(data);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            
            int r=0;
            int cells = Integer.parseInt(args[1]);
        writer.flush();
            
            while(r < rounds)
            {
                int counter=0;
                long PacketsNum=0;
                Network net = new Network(cells);
                FatTree ft = new FatTree(8,net);
                String st;
                String st1 = br.readLine();
                st = st1;
                
                Map<Integer, Integer> flows = new HashMap<Integer, Integer>();
                Map<Integer, Integer> Count = new HashMap<Integer, Integer>();
                
                while(counter<flowsNum)
                {
		    st = st1;


		    if(st.length()<20){
			st = br.readLine();
		    }


		    if(st==null || (st1 = br.readLine())==null){
			fis.read(data);
            		is = new ByteArrayInputStream(data);
            		br = new BufferedReader(new InputStreamReader(is));
			st = br.readLine();
			st1 = br.readLine();
		    }


  		    if(st.length()<20){
			st = br.readLine();
		    }


                    int i = 2;
                    int source=0;
                    int dist =0;
                    int w=0;
                    if(st.charAt(i) == '6')
                    {
                        i+=2;
                        
                        while(st.charAt(i)!=' ')
                        {
                            if(Character.isDigit(st.charAt(i))){
                                if(source == 0)
                                {
                                    source = st.charAt(i) - '0';
                                }
                                else
                                {
                                    source*=10;
                                    source+=st.charAt(i) - '0';
                                }
                            }
                            i++;
                        }
                        i+=3;
                        
                        while(st.charAt(i)!=' ')
                        {
                            if(Character.isDigit(st.charAt(i))){
                                if(dist == 0)
                                {
                                    dist = st.charAt(i) - '0';
                                }
                                else
                                {
                                    dist*=10;
                                    dist+=st.charAt(i) - '0';
                                }
                            }
                            i++;
                        }
                        
                        while(i<st.length() && !Character.isDigit(st.charAt(i)))
                        {
                            i++;
                        }
                        
                        while(i<st.length() && Character.isDigit(st.charAt(i)))
                        {
                            if(w == 0)
                            {
                                w = st.charAt(i) - '0';
                            }
                            else
                            {
                                w*=10;
                                w+=st.charAt(i) - '0';
                            }
                            i++;
                        }
                    }
                    else
                    {
                        i++;
                        
                        while(Character.isDigit(st.charAt(i)) || st.charAt(i)=='.')
                        {
                            if(st.charAt(i)!='.'){
                                if(source == 0)
                                {
                                    source = st.charAt(i) - '0';
                                }
                                else
                                {
                                    source*=10;
                                    source+=st.charAt(i) - '0';
                                }
                            }
                            i++;
                        }
                                        
                        i+=3;
                        
                        while(Character.isDigit(st.charAt(i)) || st.charAt(i)=='.')
                        {
                            if(st.charAt(i)!='.'){
                                if(dist == 0)
                                {
                                    dist = st.charAt(i) - '0';
                                }
                                else
                                {
                                    dist*=10;
                                    dist+=st.charAt(i) - '0';
                                }
                            }
                            i++;
                        }
                        
                        
                        while(i<st.length() &&!Character.isDigit(st.charAt(i)))
                        {
                            i++;
                        }
                        
                        while(i<st.length() && Character.isDigit(st.charAt(i)))
                        {
                            if(w == 0)
                            {
                                w = st.charAt(i) - '0';
                            }
                            else
                            {
                                w*=10;
                                w+=st.charAt(i) - '0';
                            }
                            i++;
                        }
                    }
                
                    source = Math.abs(source);
                    dist = Math.abs(dist);
                    w = Math.abs(w);
                    if(w == 0)
                    {
                        w = 1;
                    }
		    
		    //uncomment for unwieghted
		    //w=1;
			
                    int newid = source;
                    while(dist>1){
                        int n = dist%10;
                        dist/=10;
                        newid*=10;
                        newid+=n;
                    }
                    
                    newid = Math.abs(newid);
                        
                    if(flows.containsKey(newid))
                    {
                        flows.replace(newid,Math.abs(flows.get(newid)+w));
                        Count.replace(newid,Math.abs(Count.get(newid)+1));
                        
                    }
                    else
                    {
                        flows.put(newid,w);
                        Count.put(newid,1);
                    }
			int tempPacketId = newid;
			int newid1 = Count.get(newid);
			while(tempPacketId>1){
			    int n = tempPacketId%10;
			    tempPacketId/=10;
			    newid1*=10;
			    newid1+=n;
			}
		 // h.add(newid1,(long)w);
                    ft.addFlow(newid, Count.get(newid), w);
                    st = st1;
                    counter+=1;
                    PacketsNum+=w;
        }
        net.compine();
	    writer.append("N:");
	    writer.append("\n");
	    double NN = net.PSgetN();
	    writer.append("   URWA: ");
	    writer.append(Double.toString((double)Math.abs(net.getN()-PacketsNum)/Math.max(PacketsNum,net.getN())));
        writer.append("\n");
         writer.append("   NWPS: ");
        writer.append(Double.toString((double)Math.abs(NN-PacketsNum)/Math.max(PacketsNum,NN)));
	    writer.append("\n");
	    
        checkAll( net.SgetHeap(),net.PSgetHeap(),flows);
	    int tHold = (int)(phi*PacketsNum);
	   
	    ////////////////search for the real heavy hitters and put the result in topflowM////////////////
	    //	    
	    //
	    //
		Map<Integer, Integer> topflowM =  new HashMap<Integer, Integer>();
		for (Integer name: flows.keySet()){
			int p = flows.get(name);
			int  flowId= name;
			if(p>=tHold){
				topflowM.put(flowId,p);
			}
		}
	    //	    
	    //
	    //
	    ////////////////////////////////////////////////////////////////////////////////////////////////
	    Map<Integer, Double> qs =  net.SgetHeapNphi(phi);
	    Map<Integer, Double> qps = net.PSgetHeapNphi(phi);
	    checkHeavyHitters2( qs,qps,topflowM);
       	    checkHeavyHitters( qs,qps,topflowM);
	    checkHeavyHitters1( qs,qps,topflowM);
            r++;
        }
	fis.close();
	writer.close();
    }



    
    
    
    
    
    
    
    
    
    public static void checkAll(Map<Integer, Double> qs,Map<Integer, Double> qps,Map<Integer, Integer> flows) throws Exception
{
        
        double diffSum=0;
        double P_diffSum=0;
        long counter1=0;
	long counter2=0;
        
        for (Integer name: flows.keySet()){ 
                long diff=0;
                long P_diff=0;
                if(qs.containsKey(name)){
		    counter1+=flows.get(name);
                    diff = (long)Math.pow(flows.get(name) - qs.get(name),2);
                }
                
                if(qps.containsKey(name)){
		    counter2+=flows.get(name);
                    P_diff = (long)Math.pow(flows.get(name) - qps.get(name),2);
                }
                
                diffSum += diff;
                P_diffSum += P_diff;
                
                
                
            }
        writer.append("RMSE");
	writer.append("\n");
	 writer.append("   URWA: ");
        writer.append(Long.toString((long)Math.sqrt(diffSum/counter1)));
	writer.append("\n");
	 writer.append("   NWPS: ");
        writer.append(Long.toString((long)Math.sqrt(P_diffSum/counter2)));
	writer.append("\n");
    
    
    
    }
    
    
    
    
    
    
     public static void checkHeavyHitters1(Map<Integer, Double> qs,Map<Integer, Double> qps,Map<Integer, Integer> topflowM)throws Exception
{
        int HHsp =0;
        int HHfp =0;



        
        for (Integer name: qps.keySet()){ 
		 HHsp++;
		 if(topflowM.containsKey(name)){
		     HHfp++;
		 }
                 
        }
         writer.append("   NWPS: ");
       writer.append(Double.toString((double)(HHsp - HHfp)/HHsp)); 
	writer.append("\n");writer.append("\n");writer.append("\n");writer.append("\n");writer.append("\n");
	writer.flush();
    }

    









public static void checkHeavyHitters2(Map<Integer, Double> qs,Map<Integer, Double> qps,Map<Integer, Integer> topflowM)throws Exception
{
        long HHcounter=0;
        long sCounter=0;
        long psCounter=0;
        
        double FNRs;
        double FNRsp;
        
        for (Integer name: topflowM.keySet()){ 
            HHcounter++;
            if(qs.containsKey(name)){
                    sCounter++;
            }
            if(qps.containsKey(name)){
                    psCounter++;
            } 
        }
        FNRs = (double)(HHcounter-sCounter)/HHcounter;
        FNRsp = (double)(HHcounter-psCounter)/HHcounter;

	writer.append("FNR");
	writer.append("\n");
	 writer.append("   URWA: ");
        writer.append(Double.toString(FNRs));
	writer.append("\n");
	 writer.append("   NWPS: ");
        writer.append(Double.toString(FNRsp));
	writer.append("\n");
	writer.flush();
    }
    

    
    
    
    
    
    

    public static void checkHeavyHitters(Map<Integer, Double> qs,Map<Integer, Double> qps,Map<Integer, Integer> topflowM)throws Exception
{        
        int HHs =0;
        int HHf =0;
   
        for (Integer name: qs.keySet()){ 
                HHs++;
                if(topflowM.containsKey(name)){
                    HHf++;
                }
                
        }

	writer.append("FPR");
	writer.append("\n");
         writer.append("   URWA: ");
        writer.append(Double.toString((double)(HHs - HHf)/HHs));
	writer.append("\n");
    }
}








