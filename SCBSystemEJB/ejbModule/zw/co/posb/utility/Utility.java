package zw.co.posb.utility;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Utility {
	
	public static String generateRef(){
		Random r = new Random();
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmmss");
		//System.out.println(sdf.format(date));
		return (char)(65+r.nextInt(25))+""+(char)(65+r.nextInt(25))+""+(char)(65+r.nextInt(25))+""+(char)(65+r.nextInt(25))+""+(sdf.format(date));
	}
	
	public static String getMonth(int month) {     
		return new DateFormatSymbols().getMonths()[month];  
	}  
}
