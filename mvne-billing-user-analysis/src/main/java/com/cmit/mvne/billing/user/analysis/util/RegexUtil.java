package com.cmit.mvne.billing.user.analysis.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class RegexUtil {
	public boolean isElevenNum(String str) {
		String regex = "\\d{11}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
	public boolean isFourthNum(String str) {
		String regex = "\\d{14}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		return m.matches();
	}
}
