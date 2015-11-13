package com.cloudteam.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TokenGenerator {
	
	public Map<Integer,String> TokenMap=new HashMap<Integer, String>();
	
	private static class TokenGeneratorHolder {
		private static final TokenGenerator INSTANCE = new TokenGenerator();
	}

	private TokenGenerator() {
		for(int i=1;i<1001;i++)
		{
			TokenMap.put(i, getCharAndNumr(25));
		}
	}

	public static final TokenGenerator getInstance() {
		return TokenGeneratorHolder.INSTANCE;
	}

	public static String getCharAndNumr(int length) {
		  String val = "";
		  Random random = new Random();
		  for (int i = 0; i < length; i++) {
		   String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
		   if ("char".equalsIgnoreCase(charOrNum)) {
		    int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
		    val += (char) (choice + random.nextInt(26));
		   } else if ("num".equalsIgnoreCase(charOrNum)) {
		    val += String.valueOf(random.nextInt(10));
		   }
		  }
		  return val;
	}
	
	public static String getToken(String request_type) {
		String access_token = null;
		if(request_type.contains("access_token=") && (request_type.split("=").length==2))
		{
			if(!request_type.split("=")[1].equals(null))
			{
				access_token = request_type.split("=")[1];
			}
		}
		return access_token;
	}

}
