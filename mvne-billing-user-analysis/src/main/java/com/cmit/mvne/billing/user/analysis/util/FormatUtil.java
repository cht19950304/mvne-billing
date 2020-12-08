package com.cmit.mvne.billing.user.analysis.util;

import com.cmit.mvne.billing.user.analysis.common.MvneException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
@Component
public class FormatUtil {
	public Date changeFormat(String time) throws ParseException, MvneException {
		if(!StringUtils.isEmpty(time)) {
			Date date = null;
			if(time.length() == 19) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				date = sdf.parse(time);
			}else if(time.length() == 14) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d = new SimpleDateFormat("yyyyMMddHHmmss").parse(time);
				String t = sdf.format(d);
				date = sdf.parse(t);
			}else {
				throw new MvneException("日期格式不正确！");
			}
			return date;
		}else {
			throw new MvneException("日期不能为空！");
		}
		
	}
	
}
