package org.Query.Columnar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class TimeStamp
{
	public static long date2Stamp(String strings) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z");
		Date date = null;
		try {
			date = sdf.parse(strings);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		assert date != null;
		return (date.getTime()) / 1000L;
	}

	public static String stamp2Date(long stamp) {
		Date date = new Date(stamp * 1000L);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
		return sdf.format(date);
	}
}