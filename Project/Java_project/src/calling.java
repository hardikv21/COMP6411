import java.util.List;

public class calling extends Thread
{
	List<String> Receiver = null;
	Master master;
	public calling(List<String> Receiver,Master mast)
	{
		// TODO Auto-generated constructor stub
		this.Receiver = Receiver;
		this.master = mast;
	}

	public void run()
	{	
		String introMessage = "intro";
		for (int i = 0;i<Receiver.size();i++)
		{
			String receiverName = Receiver.get(i);
			try
			{
				Thread.sleep(1000);
				
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long time = System.currentTimeMillis();
			intro(this.getName(),receiverName,introMessage,time);
		}
		try
		{
			Thread.sleep(1000);
		} 
		catch (InterruptedException e)
		{	
			e.printStackTrace();
		}
		try
		{
			master.terminate(this.getName());
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private synchronized void intro(String call, String rec, String i, long time)
	{	
		String msg = rec+" received "+i+" message from "+call;
		System.out.println(msg + " [" + time + "]");
		String replyMessage = reply(call,rec,time);
		System.out.println(replyMessage + " [" + time + "]");
	}

	private synchronized String reply(String caller, String receiver, long time)
	{
		String replyMessage = "reply";
		try
		{
			Thread.sleep((long)(Math.random() * 1000));
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		String message =  caller+" received "+replyMessage+" message from "+receiver;
		return message;
	}
}