package apis;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import json.JSONTokener;
import models.MarketModel;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import utils.settings;
/*
 * Mt Gox Api market api
 */
public class MtGoxapi extends MarketApi{
	private String tickerurl="http://data.mtgox.com/api/2/BTCUSD/money/ticker";
	private MarketModel currentMarketModal=null;
	private int currentOpenOrders=0;
	private BigDecimal BTCFunds=new BigDecimal(0.0);
	private BigDecimal USDFunds=new BigDecimal(0.0);
	private boolean needsToUpdateBlances=true;
	private String apikey="";
	private String apisecret="";
public MtGoxapi(String initApiKey, String initApiSecret){
	apikey=initApiKey;
	apisecret=initApiSecret;
}
public void update(){
		URL url;
		MarketModel mtgox=new MarketModel();
		try {
			url = new URL(tickerurl);
			JSONObject jo=new JSONObject(new JSONTokener(url.openStream())).getJSONObject("data");
			mtgox.setHigh(jo.getJSONObject("high").getDouble("value"));
			mtgox.setLow(jo.getJSONObject("low").getDouble("value"));
			mtgox.setAvg(jo.getJSONObject("avg").getDouble("value"));
			mtgox.setBuy(jo.getJSONObject("buy").getDouble("value"));
			mtgox.setSell(jo.getJSONObject("sell").getDouble("value"));
			mtgox.setLast(jo.getJSONObject("last").getDouble("value"));
			mtgox.setVwap(jo.getJSONObject("vwap").getDouble("value"));
		} catch (MalformedURLException e) {
			utils.logger.log(true, e.getMessage());
			currentMarketModal=null;
			if(settings.debug){
				e.printStackTrace();
			}
			return;
		} catch (JSONException e) {
			utils.logger.log(true, e.getMessage());
			currentMarketModal=null;
			if(settings.debug){
				e.printStackTrace();
			}
			return;
		} catch (IOException e) {
			utils.logger.log(true, e.getMessage());
			currentMarketModal=null;
			if(settings.debug){
				e.printStackTrace();
			}
			return;
		}
		currentMarketModal=mtgox;
}
public void update_get_open_orders(){
	int count=0;
	try {
		JSONObject jo = new JSONObject(query("1/generic/private/orders",new HashMap<String, String>()));
		JSONArray ja=jo.getJSONArray("return");
		for(int i=0; i<ja.length(); i++){
			if(ja.getJSONObject(i).has("status") && ja.getJSONObject(i).getString("status").equals("open")){
				count++;
			}
		}
	} catch (JSONException e) {
		utils.logger.log(true, e.getMessage());
		if(settings.debug){
			e.printStackTrace();
		}
	}
	currentOpenOrders=count;
}
public void submit_order(String type, String amount_int,String price_int){
		super.submit_order(type, amount_int, price_int);
		HashMap<String, String> args=new HashMap<String, String>();
		args.put("type", type);
		DecimalFormat myFormatter=new DecimalFormat(".00000000");
		args.put("amount_int", myFormatter.format(Double.parseDouble(amount_int)).replace(".", ""));
		myFormatter = new DecimalFormat(".00000");
		args.put("price_int", myFormatter.format(Double.parseDouble(price_int)).replace(".", ""));
		JSONObject reponse;
		try {
			reponse = new JSONObject(query("1/BTCUSD/private/order/add",args));
			if(reponse.has("result") && reponse.getString("result").equals("success")){
				settings.lasttrade.setOrderid(reponse.getString("return"));
			}
		} catch (JSONException e) {
			reponse=new JSONObject();
		}
		utils.logger.log(false, reponse.toString());
		needsToUpdateBlances=true;
}
public void UpdateUSDFunds(){
	try {
		JSONObject jo = new JSONObject(query("1/generic/private/info",new HashMap<String, String>()));
		USDFunds=BigDecimal.valueOf(jo.getJSONObject("return").getJSONObject("Wallets").getJSONObject("USD").getJSONObject("Balance").getDouble("value"));
	} catch (JSONException e) {
		utils.logger.log(true, e.getMessage());
		USDFunds=BigDecimal.valueOf(0.0);
		if(settings.debug){
			e.printStackTrace();
		}
	}
}
public void UpdateBTCFunds(){
	try {
		JSONObject jo = new JSONObject(query("1/generic/private/info",new HashMap<String, String>()));
		BTCFunds=BigDecimal.valueOf(jo.getJSONObject("return").getJSONObject("Wallets").getJSONObject("BTC").getJSONObject("Balance").getDouble("value"));
	} catch (JSONException e) {
		utils.logger.log(true, e.getMessage());
		BTCFunds=BigDecimal.valueOf(0.0);
		if(settings.debug){
			e.printStackTrace();
		}
	}
}
 private String query(String path, HashMap<String, String> args) {
        try {
            args.put("nonce", String.valueOf(System.currentTimeMillis()));
            String post_data = buildQueryString(args);
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_spec = new SecretKeySpec(Base64.decodeBase64(apisecret), "HmacSHA512");
            mac.init(secret_spec);
            String signature = Base64.encodeBase64String(mac.doFinal(post_data.getBytes()));
            URL queryUrl = new URL("https://data.mtgox.com/api/" + path);
            HttpURLConnection connection = (HttpURLConnection)queryUrl.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; Java Test client)");
            connection.setRequestProperty("Rest-Key", apikey);
            connection.setRequestProperty("Rest-Sign", signature.replaceAll("\n", ""));
            connection.getOutputStream().write(post_data.getBytes());
            StringWriter writer = new StringWriter();
            IOUtils.copy(connection.getInputStream(), writer, "UTF-8");
            return writer.toString();
        } catch (Exception e) {
        	utils.logger.log(true,e.getMessage());
        	if(settings.debug){
    			e.printStackTrace();
    		}
        }
		return new String();
    }
 
private String buildQueryString(HashMap<String, String> args) {
        String result = new String();
        for (String hashkey : args.keySet()) {
            if (result.length() > 0) result += '&';
            try {
                result += URLEncoder.encode(hashkey, "UTF-8") + "="
                        + URLEncoder.encode(args.get(hashkey), "UTF-8");
            } catch (Exception e) {
                utils.logger.log(true, e.getMessage());
                if(settings.debug){
        			e.printStackTrace();
        		}
            }
        }
        return result;
    }
	public MarketModel getMarketModel() {
		return currentMarketModal;
	}
	public int get_open_orders() {
		return currentOpenOrders;
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
	@Override
	public void CancelOrder() {
		HashMap<String, String> args=new HashMap<String, String>();
		args.put("oid", settings.lasttrade.getOrderid());
		query("2/BTCUSD/money/order/cancel",args);
	}
}
