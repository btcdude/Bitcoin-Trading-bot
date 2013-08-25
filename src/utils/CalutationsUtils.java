package utils;

import java.math.BigDecimal;

public class CalutationsUtils {
	public static BigDecimal calulateAmountOfBitcoins(BigDecimal bitcoinPrice, BigDecimal USD){
		return USD.divide(bitcoinPrice, 8, BigDecimal.ROUND_HALF_UP);
	}
	public static BigDecimal FundsToTradeAsk(){
		if(settings.market.getBTCFunds().compareTo(settings.funds)>=0){
			return settings.funds;
		}else{
			return settings.market.getBTCFunds();
		}
	}
	public static int mintuesToMilliseconds(int mintues){
		return mintues*60000;
	}
	public static int secondsToMilliseconds(int seconds){
		return seconds*1000;
	}
}
