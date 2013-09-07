package utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;

import apis.ANXapi;
import apis.Btceapi;

import models.LastTrade;

import json.JSONException;
import json.JSONObject;
import json.JSONTokener;

public class configurationfile {
	private static String key;
	private static String secret;
	public static void load(){
		utils.logger.log(false,"Loading configuration file");
		try {
			JSONObject jo=new JSONObject(new JSONTokener(new FileInputStream(new File(settings.ConfigFilePath))));
			settings.funds=new BigDecimal(jo.getDouble("Funds To Trade"));
			JSONObject lasttrade=jo.getJSONObject("Last Trade");
			settings.lasttrade=new LastTrade(lasttrade.getString("Type").toLowerCase(), new BigDecimal(lasttrade.getDouble("Trade")));
			if(jo.has("API Key") && jo.has("API Secret")){
				settings.market=new ANXapi(jo.getString("API Key"), jo.getString("API Secret"));
				key=jo.getString("API Key");
				secret=jo.getString("API Secret");
			}else{
				utils.logger.log(true, "No API key and Secret shutting down...");
				System.exit(0);
			}
		} catch (FileNotFoundException e) {
			utils.logger.log(true, e.getMessage());
			return;
		} catch (JSONException e) {
			utils.logger.log(true, e.getMessage());
			return;
		}
		utils.logger.log(false, "Configuration file loaded");
	}
	public static void save(){
		utils.logger.log(false,"Saving configuration file");
		JSONObject jo=new JSONObject();
		try {
			jo.put("Funds To Trade", settings.funds.doubleValue());
			JSONObject LastTrade=new JSONObject();
			LastTrade.put("Type", settings.lasttrade.getTypeOfTrade());
			LastTrade.put("Trade", settings.lasttrade.getPrice().doubleValue());
			jo.put("Last Trade", LastTrade);

		
			jo.put("API Key", key);
			jo.put("API Secret", secret);
			DataOutputStream dos=new DataOutputStream(new FileOutputStream(new File(settings.ConfigFilePath)));
			dos.write(jo.toString(2).getBytes());
			dos.flush();
			dos.close();
		} catch (JSONException e) {
			utils.logger.log(true, e.getMessage());
			return;
		} catch (FileNotFoundException e) {
			utils.logger.log(true, e.getMessage());
			return;
		} catch (IOException e) {
			utils.logger.log(true, e.getMessage());
			return;
		}
		utils.logger.log(false,"Saved configuration file");
	}
}
