package apis;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import utils.settings;

import json.JSONException;
import json.JSONObject;
import json.JSONTokener;

import models.MarketModel;
/*
 * BTCE Api market api
 */
public class Btceapi extends MarketApi {
private String apikey="";
private String apisecret="";
private String tickerurl="https://btc-e.com/api/2/btc_usd/ticker";
private MarketModel currentMarketModel=null;
private boolean needsToUpdateBlances=true;
private int currentOpenOrders=0;
private BigDecimal BTCFunds=new BigDecimal(0.0);
private BigDecimal USDFunds=new BigDecimal(0.0);
public Btceapi(String initapikey, String initapisecret){	
	apikey=initapikey;
	apisecret=initapisecret;
}
	public MarketModel getMarketModel() {
		return currentMarketModel;
	}
	public void update() {
		MarketModel btce=new MarketModel();
		URL url;
		try {
			url = new URL(tickerurl);
		    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; Java Test client)");
			JSONObject jo=new JSONObject(new JSONTokener(connection.getInputStream())).getJSONObject("ticker");
			btce.setHigh(jo.getDouble("high"));
			btce.setLow(jo.getDouble("low"));
			btce.setAvg(jo.getDouble("avg"));
			btce.setLast(jo.getDouble("last"));
			btce.setBuy(jo.getDouble("buy"));
			btce.setSell(jo.getDouble("sell"));
		} catch (MalformedURLException e) {
			utils.logger.log(true, e.getMessage());
			currentMarketModel=null;
			return;
		} catch (JSONException e) {
			utils.logger.log(true, e.getMessage());
			currentMarketModel=null;
			return;
		} catch (IOException e) {
			utils.logger.log(true, e.getMessage());
			currentMarketModel=null;
			return;
		}
		currentMarketModel=btce;
	}
	public int get_open_orders() {
		return currentOpenOrders;
	}

	public void update_get_open_orders() {
		try {
			JSONObject jo = new JSONObject(query("getInfo",new HashMap<String, String>())).getJSONObject("return");
			currentOpenOrders=jo.getInt("open_orders");
		} catch (JSONException e) {
			utils.logger.log(true, e.getMessage());
			currentOpenOrders=0;
		}

	}
	public void UpdateUSDFunds() {
		try {
			JSONObject jo = new JSONObject(query("getInfo",new HashMap<String, String>())).getJSONObject("return");
			USDFunds=new BigDecimal(jo.getJSONObject("funds").getDouble("usd"));
		} catch (JSONException e) {
			utils.logger.log(true, e.getMessage());
			USDFunds=new BigDecimal(0);
		}

	}
	public void UpdateBTCFunds() {
		try {
			JSONObject jo = new JSONObject(query("getInfo",new HashMap<String, String>())).getJSONObject("return");
			BTCFunds=new BigDecimal(jo.getJSONObject("funds").getDouble("btc"));
		} catch (JSONException e) {
			utils.logger.log(true, e.getMessage());
			BTCFunds=new BigDecimal(0);
		}

	}
	public BigDecimal getUSDFunds() {
		return USDFunds;
	}
	public BigDecimal getBTCFunds() {
		return BTCFunds;
	}
	public void hasNeedsToUpdateBlances() {
		needsToUpdateBlances=false;
	}
	public boolean isNeedsToUpdateBlances() {
		return needsToUpdateBlances;
	}
	public void submit_order(String type, String amount,String price) {
		super.submit_order(type, amount, price);
		HashMap<String, String> args=new HashMap<String, String>();
		args.put("pair", "btc_usd");
		if(type.equals("bid")){
			args.put("type", "buy");
		}else if(type.equals("ask")){
			args.put("type", "sell");
		}
		args.put("rate", price);
		args.put("amount", amount);
		JSONObject reponse;
		try {
			reponse = new JSONObject(query("Trade",args));
			if(reponse.has("success") && reponse.getInt("success")==1){
				settings.lasttrade.setOrderid(Integer.toString(reponse.getJSONObject("return").getInt("order_id")));
			}
		} catch (JSONException e) {
			reponse=new JSONObject();
		}
		utils.logger.log(false, reponse.toString());
		needsToUpdateBlances=true;
	}
	private String query(String method, HashMap<String, String> args){
	        Mac mac=null;
	        SecretKeySpec key = null;
	        args.put( "method", method);
	        long time=System.currentTimeMillis() / 1000L;
	        args.put( "nonce", "" + (int)(time));
	        String postData = "";
	        for(Iterator argumentIterator = args.entrySet().iterator(); argumentIterator.hasNext(); ) {
	            Map.Entry argument = (Map.Entry)argumentIterator.next();
	           
	            if( postData.length() > 0) {
	                postData += "&";
	            }
	            postData += argument.getKey() + "=" + argument.getValue();
	        }
	        try {
	        key = new SecretKeySpec(apisecret.getBytes( "UTF-8"), "HmacSHA512");
	        mac = Mac.getInstance( "HmacSHA512" );
	        mac.init( key);
	        URL queryUrl = new URL("https://btc-e.com/tapi/");
	        HttpURLConnection connection = (HttpURLConnection)queryUrl.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; Java Test client)");
            connection.setRequestProperty("Key", apikey);
            connection.setRequestProperty("Sign", Hex.encodeHexString( mac.doFinal( postData.getBytes( "UTF-8"))));
            connection.getOutputStream().write(postData.getBytes());
            StringWriter writer = new StringWriter();
            IOUtils.copy(connection.getInputStream(), writer, "UTF-8");
            return writer.toString();
	        } catch (Exception ex) {
	        	utils.logger.log(true,ex.getMessage());
	        }
	        return new String();
	}
	@Override
	public void CancelOrder() {
		HashMap<String, String> args=new HashMap<String, String>();
		args.put("order_id", settings.lasttrade.getOrderid());
		query("CancelOrder",args);
	}
}
