import java.util.*;

public class Timer extends Thread
{
	private int secondsFormat;
	private String minutesSecondsFormat;
	private boolean countUpwards;
	private boolean mayRun;
	
	public Timer(String time, boolean countUpwards)
	{
		minutesSecondsFormat = time;
		this.countUpwards = countUpwards;
		secondsFormat = minutesSecondsToSecondsFormat(time);
		mayRun = true;
	}
	
	private int minutesSecondsToSecondsFormat(String time)
	{
		if(countUpwards)
		{
			int seconds = 0;
			int counter = 0;
			StringTokenizer st = new StringTokenizer(time, ":");
			while(st.hasMoreTokens())
			{
				if(counter == 0) seconds += (Integer.parseInt(st.nextToken()) * 60);
				if(counter == 1) seconds += Integer.parseInt(st.nextToken());
				counter++;
			}
			return seconds;
		}
		else return 0;
	}
	
	private void stopTimer()
	{
		mayRun = false;
	}
	
	private String secondsToMinutesSecondsFormat(int seconds)
	{
		StringBuffer minutesSecondsFormat = new StringBuffer();
		minutesSecondsFormat.append((new Integer(seconds/60)).toString() + ":").append((new Integer(seconds%60)).toString());
		return minutesSecondsFormat.toString();
	}
	
	public String getCurrentTime()
	{
		return minutesSecondsFormat;
	}
	
	public void run()
	{
		if(!countUpwards)
		{
			while(secondsFormat >= 0)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				secondsFormat--;
				minutesSecondsFormat = secondsToMinutesSecondsFormat(secondsFormat);
			}
		}
		else
		{
			while(mayRun)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				secondsFormat++;
				minutesSecondsFormat = secondsToMinutesSecondsFormat(secondsFormat);
			}
		}
	}
}