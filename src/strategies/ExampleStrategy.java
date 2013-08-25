package strategies;

import java.math.BigDecimal;
import java.util.TimerTask;

import utils.CalutationsUtils;
import utils.settings;

import models.MarketModel;

public class ExampleStrategy extends TimerTask {
	@Override
	public void run() {
		if(settings.market.get_open_orders()>0){
			return;
		}
		MarketModel marketmodel=settings.market.getMarketModel();
		if(marketmodel==null){
			return;
		}
		if(settings.lasttrade.getTypeOfTrade().equals("bid") && (marketmodel.getHigh()-marketmodel.getBuy()<=1)){
			settings.market.submit_order("ask", CalutationsUtils.FundsToTradeAsk().toPlainString(), Double.toString(marketmodel.getBuy()));
		}else if(settings.lasttrade.getTypeOfTrade().equals("ask") && (marketmodel.getSell()-marketmodel.getLow()<=1)){
			BigDecimal fundstotrade=settings.market.getUSDFunds().divide(BigDecimal.valueOf(marketmodel.getSell()),8,BigDecimal.ROUND_HALF_UP);
			settings.market.submit_order("bid", fundstotrade.toPlainString(), Double.toString(marketmodel.getBuy()));
		}
	}

}
