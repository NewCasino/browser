package com.youkes.browser.utils;

import java.math.BigInteger;
import java.security.SecureRandom;


public class RandomUtil {
	
	public static String secureRandomString() {
		SecureRandom random = new SecureRandom();
	    return new BigInteger(130, random).toString(32);
	}
	
}
