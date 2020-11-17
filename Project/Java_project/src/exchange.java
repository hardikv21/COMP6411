import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

class Master extends Thread
{
	private  volatile boolean done = false;
	private HashMap<String, ArrayList<String>> mymap = null;
	public Master(HashMap<String, ArrayList<String>> mymap)
	{
		this.mymap = mymap;
	}
		
	public void run()
	{
		Iterator<Entry<String, ArrayList<String>>> it = mymap.entrySet().iterator();
		//print **calls to be made**
		System.out.println("***Calls to be made***");
		while(it.hasNext())
		{	
			Map.Entry<String, ArrayList<String>> pair = (Map.Entry<String, ArrayList<String>>) it.next();
			System.out.println(pair.getKey()+":"+pair.getValue());	
		}
		
		System.out.println("\n");
		//end of print
		
		//create caller thread
		//print **calls to be made**
		Iterator<Entry<String, ArrayList<String>>> it1 = mymap.entrySet().iterator();
		
		while(it1.hasNext())
		{	
			Map.Entry<String, ArrayList<String>> pair = (Map.Entry<String, ArrayList<String>>) it1.next();
			calling c = new calling(pair.getValue(),this);
			c.setName(pair.getKey());
			c.start();	
		}
		
		//end of create caller thread	
	}
			
	int counter=0;
	int count=0;
	public void terminate(String call) throws InterruptedException
	{
		synchronized (mymap)
		{
			if(counter < mymap.size()-1)
			{
				counter++;
				try
				{
					mymap.wait();
				}
				catch(Exception e)
				{
				}
			}
			else
			{	
				mymap.notifyAll();
			}
			
			// to stop the thread
			done = true;
			System.out.println("\nProcess "+call+" has received no calls for 5 seconds, ending...");
			count++;
			if(count==counter+1)
			{
				System.out.println("\nMaster has received no replies for 10 seconds, ending...");
			}
		}
	}
}
		
	

public class exchange
{
	public static void main(String args[]) throws IOException
	{
		String filePath = "calls.txt"; 
		HashMap<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		File  f  = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String st;
		while((st = br.readLine()) != null)
		{
			String[] tokens = st.split(",");
			String key = tokens[0].replace("{", "");
			ArrayList<String> list = new ArrayList<>();
			for(int i = 1;i<tokens.length;i++)
			{
				list.add(tokens[i].replaceAll("]}.", "").replace("[", "").trim());
			}
			map.put(key,list);
		}
		Master m = new Master(map);
		m.start();	
	}
}

