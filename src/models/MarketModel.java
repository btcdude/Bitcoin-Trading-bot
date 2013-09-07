package models;

public class MarketModel {
	private double high=0.0;
	private double low=0.0;
	private double avg=0.0;
	private double buy=0.0;
	private double sell=0.0;
	private double last=0.0;
	private double vwap=0.0;
	public double getHigh() {
		return high;
	}
	public double getLow() {
		return low;
	}
	public double getAvg() {
		return avg;
	}
	public double getBuy() {
		return buy;
	}
	public double getSell() {
		return sell;
	}
	public double getLast() {
		return last;
	}
	public double getVwap(){
		return vwap;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
	public void setBuy(double buy) {
		this.buy = buy;
	}
	public void setSell(double sell) {
		this.sell = sell;
	}
	public void setLast(double last) {
		this.last = last;
	}
	public void setVwap(double vwap) {
		this.vwap = vwap;
	}
}
