package strategies;

import models.MarketModel;
import utils.CalutationsUtils;
import utils.settings;

import java.math.BigDecimal;
import java.util.TimerTask;

public class ExampleStrategy2 extends TimerTask {
	@Override
	public void run() {
        settings.market.update();
        settings.market.update_get_open_orders();
        settings.market.UpdateBTCFunds();
        settings.market.UpdateUSDFunds();
        settings.market.submit_order("ask","0.01","200");
        settings.market.get_open_orders();

	}

}
