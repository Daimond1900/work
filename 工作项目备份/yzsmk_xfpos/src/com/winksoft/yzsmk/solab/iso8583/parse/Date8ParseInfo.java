/*
j8583 A Java implementation of the ISO8583 protocol
Copyright (C) 2011 Enrique Zamudio Lopez

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
*/
package com.winksoft.yzsmk.solab.iso8583.parse;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.winksoft.yzsmk.solab.iso8583.CustomField;
import com.winksoft.yzsmk.solab.iso8583.IsoType;
import com.winksoft.yzsmk.solab.iso8583.IsoValue;

/** This class is used to parse fields of type DATE4.
 * 
 * @author Enrique Zamudio
 */
public class Date8ParseInfo extends FieldParseInfo {

	public Date8ParseInfo() {
		super(IsoType.DATE8, 8);
	}

	@Override
	public IsoValue<Date> parse(byte[] buf, int pos, CustomField<?> custom) throws ParseException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid DATE8 position %d", pos), pos);
		}
		if (pos+8 > buf.length) {
			throw new ParseException(String.format("Insufficient data for DATE8 field, pos %d", pos), pos);
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//Set the month in the date
		cal.set(Calendar.YEAR, ((buf[pos] - 48) * 1000) + (buf[pos + 1] - 48) * 100+(buf[pos + 2] - 48) * 10 + buf[pos + 3] - 48);
		cal.set(Calendar.MONTH, ((buf[pos+4] - 48) * 10) + buf[pos + 5] - 49);
		cal.set(Calendar.DATE, ((buf[pos + 6] - 48) * 10) + buf[pos + 7] - 48);
		Date10ParseInfo.adjustWithFutureTolerance(cal);
		return new IsoValue<Date>(type, cal.getTime(), null);
	}

	@Override
	public IsoValue<Date> parseBinary(byte[] buf, int pos, CustomField<?> custom) throws ParseException {
		int[] tens = new int[3];
		int start = 0;
		int i = pos;
		
//		tens[start++] = (((buf[i] & 0xf0) >> 4) * 1000) + ((buf[i] & 0xf0) * 100)+(((buf[i+1] & 0xf0) >> 4) * 10) + (buf[i+1] & 0x0f);
		tens[start] = (((buf[i] & 0xf0) >> 4) * 1000) ;
		tens[start] += ((buf[i] & 0x0f) * 100);
		tens[start] += (((buf[i+1] & 0xf0) >> 4) * 10); 
		tens[start] +=  (buf[i+1] & 0x0f);
		
		start++;
		i += 2;
		for (; i < pos + 2 + tens.length-1; i++) {
			tens[start++] = (((buf[i] & 0xf0) >> 4) * 10) + (buf[i] & 0x0f);
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		//Set the month in the date
		cal.set(Calendar.YEAR, tens[0]);
		cal.set(Calendar.MONTH, tens[1] - 1);
		cal.set(Calendar.DATE, tens[2]);
		cal.set(Calendar.MILLISECOND,0);
		Date10ParseInfo.adjustWithFutureTolerance(cal);
		return new IsoValue<Date>(type, cal.getTime(), null);
	}

}
