package com.cloudteam.handler;


import java.io.BufferedReader;
import java.util.*;



public class MyHandler {
	public String ErrorCode_INVALID_ACCESS_TOKEN = "401 Unauthorized\n{\n\"code\":\"INVALID_ACCESS_TOKEN\",\n\"message\":\"��Ч������\"\n}";
	public String ErrorCode_MALFORMED_JSON = "400 Bad Request\n{\n\"code\":\"MALFORMED_JSON\",\n\"message\":\"��ʽ����\" \n}";
	public String ErrorCode_USER_AUTH_FAIL = "403 Forbidden\n{\n\"code\":\"USER_AUTH_FAIL\",\n\"message\":\"�û������������\"\n}";
	public String ErrorCode_NOT_AUTHORIZED_TO_ACCESS_CART = "401 Unauthorized\n{\n\"code\":\"NOT_AUTHORIZED_TO_ACCESS_CART\",\n\"meaasge\":��Ȩ�޷���ָ��������\"\n}";
	public String ErrorCode_FOOD_OUT_OF_LIMIT = "403 Forbidden\n{\n\"code\":\"FOOD_OUT_OF_LIMIT\",\n\"message\":\"������ʳ����������������\"\n}";
	public String ErrorCode_FOOD_NOT_FOUND = "404 Not Found\n{\n\"code\":\"FOOD_NOT_FOUND\",\n\"message\":\"ʳ�ﲻ����\"\n}";
	public String ErrorCode_FOOD_OUT_OF_STOCK = "403 Forbidden\n{\n\"code\":\"FOOD_OUT_OF_STOCK\",\n\"message\":\"ʳ���治��\"\n}";
	public String ErrorCode_ORDER_OUT_OF_LIMIT = "403 Forbidden\n{\n\"code\":\"ORDER_OUT_OF_LIMIT\",\n\"message\":\"ÿ���û�ֻ����һ��\"\n}";

	public MyHandler() {
		// TODO Auto-generated constructor stub
	}	 
}

class FoodHandler{  //���ʳ��
	String FoodHand(BufferedReader in) {
		return "";
	}
}

class CartsHandler{ //��������
	String CartsHand(BufferedReader in) {
		return "";
	}
}

class OrderHandler{  //�µ�
	String OrderHand(BufferedReader in) {
		return "";
	}
}

class FoodsHandler{  //��ѯʳ����
	String FoodsHand(BufferedReader in) {
		return "";
	}
}

class OrdersHandler{  //��ѯ����
	String OrdersHand(BufferedReader in) {
		return "";
	}
}

class AdminOrdersHandler{  //��̨ͳһ��ѯ����
	String AdminOrdersHand(BufferedReader in) {
		return "";
	}
}
