package utils;

import java.math.BigDecimal;

import models.LastTrade;

import apis.Bitstampapi;
import apis.Btceapi;
import apis.MarketApi;
import apis.MtGoxapi;

public class settings {
	public static LastTrade lasttrade=null;
	public static BigDecimal funds= new BigDecimal(0.0000000);
	//public static MarketApi market=new MtGoxapi("c80aa348-c74a-4cf7-824b-1cce3d934913", "kqvkdUdQ8jD1xKXMH15wubl+l5/C0qIRUUqQCry8ODrFU7wkMYEc4NQ/xxwOp7YJXj84RMEFsVfhInMkz91GEQ==");
	//public static MarketApi market=new Btceapi("XX2O3PDH-6MZ6OR59-KBBUD64E-JXEODAY5-ABLXTSHH", "d56876086d321f674c9cc6cf50a282f511e8f3ec11d22734725cd519636835e7");
	//public static MarketApi market=new Bitstampapi("43952","PurpleHaze22290");
	public static MarketApi market;
	public static String ConfigFilePath="traderbot.config";
	public static boolean debug=false;
}
