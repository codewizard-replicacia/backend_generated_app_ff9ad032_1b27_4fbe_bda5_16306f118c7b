package com.mycompany.group234.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static Date getNextDateFromDate(Date fromDate, String delta) {
		int afterDays = 365;
		if (RecurranceType.WEEKLY.toString().equalsIgnoreCase(delta)) {
			afterDays = 7;
		}
		if (RecurranceType.MONTHLY.toString().equalsIgnoreCase(delta)) {
			afterDays = 30;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fromDate);
		calendar.add(Calendar.DAY_OF_MONTH, afterDays);
		return calendar.getTime();
	}
}
