package apis;

import java.math.BigDecimal;

import utils.logger;
import utils.settings;

import metrics.Volume;
import models.LastTrade;
import models.MarketModel;
/*
 * This is the interface as to how to add a new market.
 */
public abstract class MarketApi {
	public abstract MarketModel getMarketModel();
	public abstract void update();
	public abstract int get_open_orders();
	public abstract void update_get_open_orders();
	public void submit_order(String type, String amount,String price) {
		logger.logTrade(type, amount, price);
		Volume.AddToVolume(new BigDecimal(amount));
		utils.logger.log(false,"[TRADE] "+type+" Amount: "+amount+" at Price: "+price);
		settings.lasttrade=new LastTrade(type, new BigDecimal(price));
	}
	public abstract void UpdateUSDFunds();
	public abstract void UpdateBTCFunds();
	public abstract BigDecimal getUSDFunds();
	public abstract BigDecimal getBTCFunds();
	public abstract void hasNeedsToUpdateBlances();
	public abstract boolean isNeedsToUpdateBlances();
	public abstract void CancelOrder();
}
