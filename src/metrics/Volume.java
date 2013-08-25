package metrics;

import java.math.BigDecimal;

public class Volume {
private static BigDecimal volume=new BigDecimal(0.0);
public static void AddToVolume(BigDecimal amount){
	volume=volume.add(amount);
}
public static BigDecimal getVolume(){
	return volume;
}
public static String VolumeOfBot(){
	return volume.toPlainString()+" Bitcoins has been trade by this bot";
}
}
