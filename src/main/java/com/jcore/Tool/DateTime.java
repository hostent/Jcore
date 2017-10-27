package com.jcore.Tool;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateTime {
	
	public static Date of( LocalDateTime dt)
	{
		ZoneId zoneId = ZoneId.systemDefault(); 
        ZonedDateTime zdt = dt.atZone(zoneId);

        Date date = Date.from(zdt.toInstant());
        
        return date;
	}
	
	public static LocalDateTime to( Date dt)
	{
 
        Instant instant = dt.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        
        return localDateTime;
	}

}
