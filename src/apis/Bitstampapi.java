package apis;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import utils.settings;

import json.JSONException;
import json.JSONObject;
import json.JSONTokener;

import models.MarketModel;
/*
 * Bitstamp Api market api
 */
public class Bitstampapi extends MarketApi {
private String username="";
private String password="";
private String tickerurl="https://www.bitstamp.net/api/ticker/";
private MarketModel currentMarketModel=null;
private boolean needsToUpdateBlances=true;
private int currentOpenOrders=0;
private BigDecimal BTCFunds=new BigDecimal(0.0);
private BigDecimal USDFunds=new BigDecimal(0.0);
public Bitstampapi(String initusername, String initpassword){
	super();
	username=initusername;
	password=initpassword;
}
	@Override
	public MarketModel getMarketModel() {
		return currentMarketModel;
	}

	@Override
	public void update() {
		MarketModel bitstamp=new MarketModel();
		URL url;
		try {
			url = new URL(tickerurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();           
			connection.setRequestMethod("GET"); 
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; Java Test client)");
			JSONObject jo=new JSONObject(new JSONTokener(connection.getInputStream()));
			bitstamp.setHigh(jo.getDouble("high"));
			bitstamp.setLow(jo.getDouble("low"));
			bitstamp.setLast(jo.getDouble("last"));
			bitstamp.setBuy(jo.getDouble("bid"));
			bitstamp.setSell(jo.getDouble("ask"));
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
		currentMarketModel=bitstamp;
	}

	@Override
	public int get_open_orders() {
		return currentOpenOrders;
	}

	@Override
	public void update_get_open_orders() {
		int count=0;
		currentOpenOrders=count;
	}

	@Override
	public void UpdateUSDFunds() {
		try {
			HashMap<String, String> args=new HashMap<String, String>();
			args.put("user", username);
			args.put("password", password);
			JSONObject jo=new JSONObject(query("balance/", args));
			USDFunds=new BigDecimal(jo.getDouble("usd_balance"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void UpdateBTCFunds() {
		try {
			HashMap<String, String> args=new HashMap<String, String>();
			args.put("user", username);
			args.put("password", password);
			JSONObject jo=new JSONObject(query("balance/", args));
			BTCFunds=new BigDecimal(jo.getDouble("btc_balance"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public BigDecimal getUSDFunds() {
		return USDFunds;
	}

	@Override
	public BigDecimal getBTCFunds() {
		return BTCFunds;
	}

	@Override
	public void hasNeedsToUpdateBlances() {
		needsToUpdateBlances=false;
	}

	@Override
	public boolean isNeedsToUpdateBlances() {
		return needsToUpdateBlances;
	}
	public void submit_order(String type, String amount,String price) {
		super.submit_order(type, amount, price);
		HashMap<String, String> args=new HashMap<String, String>();
		args.put("user", username);
		args.put("password", password);
		args.put("amount", amount);
		args.put("price", price);
		if(type.equals("ask")){
			utils.logger.log(false, query("sell/",args));
		}else if(type.equals("bid")){
			utils.logger.log(false, query("buy/",args));
		}
	}
	private String query(String method, HashMap<String, String> args){
		try {
			String urlParameters = build_query(args);
			URL url = new URL("https://www.bitstamp.net/api/"+method);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();           
			connection.setDoOutput(true);
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; Java Test client)");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.getOutputStream().write(urlParameters.getBytes());
			StringWriter writer = new StringWriter();
            IOUtils.copy(connection.getInputStream(), writer, "UTF-8");
            return writer.toString();
		} catch (MalformedURLException e) {
			utils.logger.log(true, e.getMessage());
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	private String build_query(HashMap<String,String> args){
		String results="";
		if(args.size()==0){
			return "";
		}else{
			Object[] param=args.keySet().toArray();
			Object[] value=args.values().toArray();
			for(int i=0; i<args.size(); i++){
				try{
				if(i==args.size()-1){
					results+=(String)param[i]+"="+URLEncoder.encode((String)value[i],"UTF-8");
				}else{
					results+=(String)param[i]+"="+URLEncoder.encode((String)value[i],"UTF-8")+"&";
				}
				}catch(Exception e){
					if(settings.debug){
						utils.logger.log(true, e.getMessage());
					}
				}
			}
		}
		return results;
	}
	@Override
	public void CancelOrder() {
		// TODO Auto-generated method stub
		
	}
}
