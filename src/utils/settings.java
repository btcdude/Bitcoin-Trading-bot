package utils;

import java.math.BigDecimal;

import apis.*;
import models.LastTrade;

public class settings {
	public static LastTrade lasttrade=null;
	public static BigDecimal funds= new BigDecimal(0.0000000);
	//public static MarketApi market=new MtGoxapi("c80aa348-c74a-4cf7-824b-1cce3d934913", "kqvkdUdQ8jD1xKXMH15wubl+l5/C0qIRUUqQCry8ODrFU7wkMYEc4NQ/xxwOp7YJXj84RMEFsVfhInMkz91GEQ==");
	//public static MarketApi market=new Btceapi("XX2O3PDH-6MZ6OR59-KBBUD64E-JXEODAY5-ABLXTSHH", "d56876086d321f674c9cc6cf50a282f511e8f3ec11d22734725cd519636835e7");
	//public static MarketApi market=new Bitstampapi("43952","PurpleHaze22290");
	public static MarketApi market=new ANXapi("8f965cf3-d69a-480a-bb0a-6b5b846a1f8b","amF2YXguY3J5cHRvLnNwZWMuU2VjcmV0S2V5U3BlY0A1ODg0OTJj");
	public static String ConfigFilePath="traderbot.config";
	public static boolean debug=false;
}
