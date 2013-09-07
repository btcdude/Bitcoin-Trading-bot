package models;

import java.math.BigDecimal;

public class LastTrade {
private String typeOfTrade="";
private BigDecimal price;
private long time;
private String Orderid="";
public LastTrade(String inittypeOfTrade, BigDecimal initprice){
	typeOfTrade=inittypeOfTrade;
	price=initprice;
	time=System.currentTimeMillis()/1000;
}
public String getTypeOfTrade() {
	return typeOfTrade;
}
public BigDecimal getPrice(){
	return price;
}
public long getTime() {
	return time;
}
public String getOrderid() {
	return Orderid;
}
public void setOrderid(String orderid) {
	Orderid = orderid;
}
}
