package timertask;

import java.util.TimerTask;

import utils.settings;

public class UpdateMarketData extends TimerTask {
	@Override
	public void run() {
		settings.market.update();
		settings.market.update_get_open_orders();
		if(settings.market.isNeedsToUpdateBlances() && settings.market.get_open_orders()==0){
			settings.market.UpdateBTCFunds();
			settings.market.UpdateUSDFunds();
			settings.market.hasNeedsToUpdateBlances();
		}
	}

}
