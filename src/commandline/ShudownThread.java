package commandline;

import metrics.Volume;
import utils.configurationfile;

public class ShudownThread extends Thread {
	//This class closes the bot and makes sure the configuration is saved with updated values and writes the rest of the logs.
	public void run(){
		metrics.TimeOfBotRunning.EndTime();
		configurationfile.save();
		utils.logger.log(false,metrics.TimeOfBotRunning.TimeBeenRunning());
		utils.logger.log(false, Volume.VolumeOfBot());
		utils.logger.log(false,"Bot is shutting down...");
	}
}
