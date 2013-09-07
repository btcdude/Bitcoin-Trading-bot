package commandline;

import java.util.ArrayList;

import apis.MtGoxapi;

import utils.settings;
/*
 * This class sets up all the arguments of the command line
 */
public class arguments {
	public arguments(ArrayList<String> initargs){
		if(initargs.contains("-v") || initargs.contains("--verbose")){
			SetVerbose();
		}
		if(initargs.contains("--MTGoxAPIKey") && initargs.contains("--MTGoxAPISecret")){
			SetMtGoxKeysAndSecrets(initargs);
		}
	}
	private void SetVerbose(){
		utils.logger.SetVerbose();
	}
	private void SetMtGoxKeysAndSecrets(ArrayList<String> args){
		if(!(args.indexOf("--MTGoxAPIKey")+1<=args.size()-1)){
			utils.logger.log(true, "Not enough parameters");
			return;
		}
		if(!(args.indexOf("--MTGoxAPISecret")+1<=args.size()-1)){
			utils.logger.log(true, "Not enough parameters");
			return;
		}
		settings.market=new MtGoxapi(args.get(args.indexOf("--MTGoxAPIKey")+1),args.get(args.indexOf("--MTGoxAPISecret")+1));
		utils.logger.log(false, "Mt Gox Api Key and Secret was set");
	}
}
