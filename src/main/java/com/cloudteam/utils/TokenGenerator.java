package com.cloudteam.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TokenGenerator {
	
	public Map<Integer,String> User2Token=new HashMap<Integer, String>();
	public Map<String,Integer> Token2User = new HashMap<String, Integer>();
	public Map<Integer,Integer> Food2Price = new HashMap<Integer, Integer>();
	
	public Map<Integer, Integer> getFood2Price() {
		return Food2Price;
	}

	public void setFood2Price(Map<Integer, Integer> food2Price) {
		Food2Price = food2Price;
	}

	private static class TokenGeneratorHolder {
		private static final TokenGenerator INSTANCE = new TokenGenerator();
	}

	private TokenGenerator() {
		
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
	public void generateToken(){
		for(int i=1;i<1001;i++)
		{
			User2Token.put(i, getCharAndNumr(25));
		}
		this.reverseMap();
	}
	public void setToken(HashMap<Integer, String> token){
		this.User2Token = token;
		this.reverseMap();
	}
	
	private void reverseMap(){
		for(int i=1;i<1001;i++){
			Token2User.put(User2Token.get(i), i);
		}
	}
}
