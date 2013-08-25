package commandline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

import strategies.ExampleStrategy;
import timertask.UpdateMarketData;
import utils.CalutationsUtils;
/*
 * This class creates the command line interface and starts the bot. Setups the Strategies
 */
public class commandline {
	public static Timer timer=new Timer();
	public commandline(String[] args){
		new arguments(new ArrayList<String>(Arrays.asList(args)));
		Setup();
	}
	public commandline(){
		Setup();
	}
	public void Setup(){
		utils.configurationfile.load();
		utils.logger.log(false, "Bot is Starting...");
		Runtime.getRuntime().addShutdownHook(new ShudownThread());
		metrics.TimeOfBotRunning.StartTime();
		timer.schedule(new UpdateMarketData(), 0, CalutationsUtils.secondsToMilliseconds(15));
		addStrategies();
	}
	public static void main(String[] args) {
		new commandline(args);
	}
	//Add your strategy in the same way of the Example Strategy
	private static void addStrategies(){
		timer.schedule(new ExampleStrategy(), CalutationsUtils.secondsToMilliseconds(25), CalutationsUtils.secondsToMilliseconds(10));		
	}
}
