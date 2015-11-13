package com.cloudteam.handler;

import com.cloudteam.utils.TokenGenerator;

public class TokenCheck {
	String token = null;
	public TokenCheck(String token) {
		this.token = token;
	}
	public boolean checkToken(){
		if(TokenGenerator.getInstance().TokenMap.containsValue(token)){
		return true;
		}
		return false;
	}
}
