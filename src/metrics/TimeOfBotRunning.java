package metrics;

import java.util.concurrent.TimeUnit;

public class TimeOfBotRunning {
	private static long startTime=0;
	private static long endTime=0;
	public static void StartTime(){
		startTime=System.currentTimeMillis()/1000;
	}
	public static void EndTime(){
		endTime=System.currentTimeMillis()/1000;
	}
	public static String TimeBeenRunning(){
		 int day = (int)TimeUnit.SECONDS.toDays(endTime-startTime);        
		 long hours = TimeUnit.SECONDS.toHours(endTime-startTime) - (day *24);
		 long minute = TimeUnit.SECONDS.toMinutes(endTime-startTime) - (TimeUnit.SECONDS.toHours(endTime-startTime)* 60);
		 long second = TimeUnit.SECONDS.toSeconds(endTime-startTime) - (TimeUnit.SECONDS.toMinutes(endTime-startTime) *60);
		return day+" Days "+hours+" Hours "+minute+" Minutes "+second+ " Seconds the bot has been running";
	}
}
