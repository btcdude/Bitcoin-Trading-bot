package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class logger {
private static boolean verbose=false;
public static void logTrade(String type, String amount,String price){
	try{
	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
	Date date = new Date();
	boolean wasCreated=false;
	File csvfile=new File("Trades.csv");
	if(!csvfile.exists()){
		csvfile.createNewFile();
		wasCreated=true;
	}
	FileWriter fstream = new FileWriter(csvfile, true);
	BufferedWriter out = new BufferedWriter(fstream);
	if(wasCreated){
		out.write("Date,Type,Amount,Price\n");
	}
	out.write(dateFormat.format(date)+","+type+","+amount+","+price+"\n");
	out.close();
	}catch (Exception e){
		  if(verbose){
			  System.out.println("Error on writing to the Trade file, "+e.getMessage());
		  }
	  }
}
public static void log(final boolean error,final String message){
	try{
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
		Date date = new Date();
		FileWriter fstream = new FileWriter("debug.log", true);
		BufferedWriter out = new BufferedWriter(fstream);
		  if(error){
			  out.write("[ERROR]: "+dateFormat.format(date)+": "+message+"\n");
			  if(verbose){
				  System.out.println("[ERROR]: "+dateFormat.format(date)+": "+message);
			  }
		  }else{
			  out.write("[INFO]: "+dateFormat.format(date)+": "+message+"\n");
			  if(verbose){
				  System.out.println("[INFO]: "+dateFormat.format(date)+": "+message);
			  }
		  }
		  out.close();
		  }catch (Exception e){
			  if(verbose){
				  System.out.println("Error on writing to the log file, "+e.getMessage());
			  }
		  }
	}
public static void SetVerbose(){
	verbose=true;
	utils.logger.log(false, "Verbose was set to true");
}
}
