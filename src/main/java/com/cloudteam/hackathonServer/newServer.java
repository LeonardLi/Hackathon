/**
 * 
 * @author leonard
 *
 */
package com.cloudteam.hackathonServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;

import com.cloudteam.handler.AdminOrders;
import com.cloudteam.handler.Carts;
import com.cloudteam.handler.Food;
import com.cloudteam.handler.Foods;
import com.cloudteam.handler.Login;
import com.cloudteam.handler.Order;
import com.cloudteam.handler.Orders;
import com.cloudteam.handler.TokenCheck;
import com.cloudteam.utils.RedisOperator;
import com.cloudteam.utils.TokenGenerator;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class newServer {
	public static String APP_HOST = "0.0.0.0";
	public static int APP_PORT = 8080;

	public static String DB_HOST = "localhost";
	public static int DB_PORT = 3306;
	public static String DB_NAME = "eleme";
	public static String DB_USER = "root";
	public static String DB_PASS = "toor";

	public static String REDIS_HOST = "localhost";
	public static int REDIS_PORT = 6379;

	public final static int SuccessCode = 200;
	public final static int NoContent = 204;
	public final static int ErrorCode_INVALID_ACCESS_TOKEN = 401;
	public final static int ErrorCode_MALFORMED_JSON = 400;
	public final static int ErrorCode_USER_AUTH_FAIL = 403;
	public final static int ErrorCode_NOT_AUTHORIZED_TO_ACCESS_CART = 401;
	public final static int ErrorCode_FOOD_OUT_OF_LIMIT = 403;
	public final static int ErrorCode_FOOD_NOT_FOUND = 404;
	public final static int ErrorCode_CART_NOT_FOUND = 414;
	public final static int ErrorCode_FOOD_OUT_OF_STOCK = 423;
	public final static int ErrorCode_ORDER_OUT_OF_LIMIT = 413;
	public final static int ErrorCode_EMPTY_REQUEST = 400;

	public static String ErrorInfo_INVALID_ACCESS_TOKEN = "{\"code\":\"INVALID_ACCESS_TOKEN\",\"message\":\"无效的令牌\"}";
	public static String ErrorInfo_MALFORMED_JSON = "{\"code\":\"MALFORMED_JSON\",\"message\":\"格式错误\"}";
	public static String ErrorInfo_USER_AUTH_FAIL = "{\"code\":\"USER_AUTH_FAIL\",\"message\":\"用户名或密码错误\"}";
	public static String ErrorInfo_NOT_AUTHORIZED_TO_ACCESS_CART = "{\"code\":\"NOT_AUTHORIZED_TO_ACCESS_CART\",\"meaasge\":\"无权限访问指定的篮子\"}";
	public static String ErrorInfo_FOOD_OUT_OF_LIMIT = "{\"code\":\"FOOD_OUT_OF_LIMIT\",\"message\":\"篮子中食物数量超过了三个\"}";
	public static String ErrorInfo_FOOD_NOT_FOUND = "{\"code\":\"FOOD_NOT_FOUND\",\"message\":\"食物不存在\"}";
	public static String ErrorInfo_CART_NOT_FOUND = "{\"code\":\"CART_NOT_FOUND\",\"message\":\"篮子不存在\"}";
	public static String ErrorInfo_FOOD_OUT_OF_STOCK = "{\"code\":\"FOOD_OUT_OF_STOCK\",\"message\":\"食物库存不足\"}";
	public static String ErrorInfo_ORDER_OUT_OF_LIMIT = "{\"code\":\"ORDER_OUT_OF_LIMIT\",\"message\":\"每个用户只能下一单\"}";
	public static String ErrorInfo_EMPTY_REQUEST = "{\"code\":\"EMPTY_REQUEST\",\"message\": \"请求体为空\" }";

	public static void main(String[] args) throws Exception {
		if (beforeStartServer()) {
			start();
		} else {
			System.out.println("init error");
		}
	}

	public static void start() throws IOException {
		HttpServerProvider provider = HttpServerProvider.provider();

		// 指定端口号和最大并发数
		HttpServer httpServer = provider.createHttpServer(
				new InetSocketAddress(8081), 3000);

		// 绑定处理器
		httpServer.createContext("/login", new loginHandler());
		httpServer.createContext("/carts", new cartsHandler());
		httpServer.createContext("/foods", new foodsHandler());
		httpServer.createContext("/orders", new ordersHandler());
		httpServer.createContext("/admin/orders", new admin_ordersHandler());
		httpServer.setExecutor(null);
		httpServer.start();
	}

	public static boolean beforeStartServer() {
		RedisOperator op = new RedisOperator();
		TokenCheck.getInstance();
		// ///////////////////////////////////
		if (!isFirstLoadToken()) {
			// 加载Token到Redis中，懒加载，
			TokenGenerator.getInstance().generateToken();

			op.copyToken2Redis(TokenGenerator.getInstance().User2Token);

		} else {
			// get token from redis
			TokenGenerator.getInstance().setToken(op.getTokenMap());
		}
		// ///////////////////////////////////
		if (!isFirstLoadFoods()) {
			// 加载库存表到Redis服务器中
			op.copy2Redis();
		}
		
		//
		
		return true;
	}
	
	public static void initVariebles() {
		Map<String, String> map = System.getenv();
		APP_HOST = map.get("APP_HOST");
		APP_PORT = Integer.parseInt(map.get("APP_PORT"));
		DB_HOST = map.get("DB_HOST");
		DB_PORT = Integer.parseInt(map.get(DB_PORT));
		REDIS_HOST = map.get("REDIS_HOST");
		REDIS_PORT = Integer.parseInt(map.get("REDIS_PORT"));
	}

/**
 * 
 * @author leonard
 * 
 * Request:
 * POST /login
 * {
 * "username": "root",
 * "password": "toor"
 * }
 * 
 * Response:
 * 200 OK
 * {
 *  "user_id": 1,
 *   "username": "robot",
 *   "access_token": "xxx"
 *	}
 *
 * 403 Forbidden
 *	{
 *   "code": "USER_AUTH_FAIL",
 *   "message": "用户名或密码错误"
 *	}
 *
 *

 */

	static class loginHandler extends myHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			String response = "";
			// 获取请求体，
			URI uri = t.getRequestURI();
			System.out.println(uri.toString());
			// 获取请求数据，非阻塞
			BufferedReader body = new BufferedReader(new InputStreamReader(
					t.getRequestBody()));
			String data = body.readLine();

			System.out.println("data!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + data);
			if (data == null || data.equals("")) {
				response = ErrorInfo_EMPTY_REQUEST;
				t.sendResponseHeaders(ErrorCode_EMPTY_REQUEST,
						response.getBytes().length);
			} else {
				Login lh = new Login();
				int status_code = lh.LoginHand(data);
				response = lh.return_info;
				switch (status_code) {
				case SuccessCode:
					t.sendResponseHeaders(SuccessCode,
							response.getBytes().length);
					break;
				case ErrorCode_MALFORMED_JSON:
					response = ErrorInfo_MALFORMED_JSON;
					t.sendResponseHeaders(ErrorCode_MALFORMED_JSON,
							response.getBytes().length);
					break;
				case ErrorCode_USER_AUTH_FAIL:
					response = ErrorInfo_USER_AUTH_FAIL;
					t.sendResponseHeaders(ErrorCode_USER_AUTH_FAIL,
							response.getBytes().length);
					break;
				default:
					break;
				}
			}
			System.out.println(response + "\n");

			OutputStream os = t.getResponseBody();

			os.write(response.getBytes());
			os.close();
		}
	}

/**
 * 
 * @author leonard
 * 创建篮子
 * POST /carts?access_token=xxx
 * 
 * 200 OK
 *	{
 *   "cart_id ": "e0c68eb96bd8495dbb8fcd8e86fc48a3"
 *	}
 *
 * 添加食物
 * PATCH /carts/:cartid?access_token=xxx
 *	{
    "food_id": 2,
    "count": 1
	}
 *
 * 204 No content
 * 
 * 
 *
 */
	static class cartsHandler extends myHandler { // 创建篮子或者添加食物到篮子
		@Override
		public void handle(HttpExchange t) throws IOException {
			String response = "";
			System.out.println(t.getRequestMethod());
			URI uri = t.getRequestURI();
			System.out.println("uri:.............." + uri.toString());
			// 获取请求体，
			String token = this.getToken(uri, t);
			if (token == null || !TokenCheck.getInstance().checkToken(token)) {
				response = ErrorInfo_INVALID_ACCESS_TOKEN;
				t.sendResponseHeaders(ErrorCode_INVALID_ACCESS_TOKEN,
						response.getBytes().length);
			} else {
				// token有效
				if (t.getRequestMethod().equals("PATCH")) {// 添加食物PATCH
					BufferedReader body = new BufferedReader(
							new InputStreamReader(t.getRequestBody()));
					String data = body.readLine();
					if (data == null || data.equals("")) {
						// 数据为空
						response = ErrorInfo_EMPTY_REQUEST;
						t.sendResponseHeaders(ErrorCode_EMPTY_REQUEST,
								response.getBytes().length);
					} else {
						// 数据不为空
						System.out.println("data:......" + data); 
						//考虑到两种token传递方式来获取cart_id
						String cart_id = uri.getPath().substring(7);
						System.out.println("cart_id............:" + cart_id);
						Food fh = new Food();
						int status_code = fh.AddFoodHand(data, token, cart_id);

						switch (status_code) {
						case SuccessCode:
							t.sendResponseHeaders(NoContent, -1);
							break;
						case ErrorCode_FOOD_OUT_OF_LIMIT:
							response = ErrorInfo_FOOD_OUT_OF_LIMIT;
							t.sendResponseHeaders(ErrorCode_FOOD_OUT_OF_LIMIT,
									response.getBytes().length);
							break;
						case ErrorCode_NOT_AUTHORIZED_TO_ACCESS_CART:
							response = ErrorInfo_NOT_AUTHORIZED_TO_ACCESS_CART;
							t.sendResponseHeaders(
									ErrorCode_NOT_AUTHORIZED_TO_ACCESS_CART,
									response.getBytes().length);
							break;
						case ErrorCode_CART_NOT_FOUND:
							response = ErrorInfo_CART_NOT_FOUND;
							t.sendResponseHeaders(
									ErrorCode_CART_NOT_FOUND - 10,
									response.getBytes().length);
							break;
						case ErrorCode_MALFORMED_JSON:
							response = ErrorInfo_MALFORMED_JSON;
							t.sendResponseHeaders(ErrorCode_MALFORMED_JSON,
									response.getBytes().length);
							break;
						case ErrorCode_FOOD_NOT_FOUND:
							response = ErrorInfo_FOOD_NOT_FOUND;
							t.sendResponseHeaders(ErrorCode_FOOD_NOT_FOUND,
									response.getBytes().length);
							break;
						default:
							break;
						}
					}
				}else{// 创建篮子
					Carts carts = new Carts();
					response = carts.CreateCartsHand(token);
					t.sendResponseHeaders(SuccessCode,
							response.getBytes().length);
				}
			}

			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

	}

	/**
	 * Request:
	 * GET /foods?access_token=xxx
	 * @author leonard
	 *
	 * Response:
	 * 200 OK
	 * [
     *  {"id": 1, "price": 12, "stock": 99},
     *  {"id": 2, "price": 10, "stock": 89},
     *  {"id": 3, "price": 22, "stock": 91}
     * ]
	 *
	 */
	static class foodsHandler extends myHandler {

		@Override
		public void handle(HttpExchange t) throws IOException {
			String response = "";
			URI uri = t.getRequestURI();
			System.out.println(uri.toString());
			String token = this.getToken(uri, t);
			System.out.println("token:..............." + token);
			if (token == null || !TokenCheck.getInstance().checkToken(token)) {
				// token为空
				response = ErrorInfo_INVALID_ACCESS_TOKEN;
				t.sendResponseHeaders(ErrorCode_INVALID_ACCESS_TOKEN,
						response.getBytes().length);
			} else {
				// token有效
				Foods foods = new Foods();
				response = foods.QueryFoodsHand();
				t.sendResponseHeaders(SuccessCode, response.getBytes().length);
				JSONArray test = JSONArray.fromObject(response);
				System.out.println(test);
			}
			
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

	static class ordersHandler extends myHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			String response = "";
			URI uri = t.getRequestURI();
			System.out.println(uri.toString());
			String token = this.getToken(uri, t);
			String method = t.getRequestMethod();
			if (token == null || !TokenCheck.getInstance().checkToken(token)) {
				// token为空或无效
				response = ErrorInfo_INVALID_ACCESS_TOKEN;
				t.sendResponseHeaders(ErrorCode_INVALID_ACCESS_TOKEN,
						response.getBytes().length);
			} else {
				if (method.equals("GET")) {
					// 查询订单
					Orders orders = new Orders();
					response = orders.QueryOrdersHand(token); // 返回数据
					t.sendResponseHeaders(SuccessCode,
							response.getBytes().length);
				} else {
					// POST 下单
					 // 获取请求数据，非阻塞
			          BufferedReader body = new BufferedReader(
			              new InputStreamReader(t.getRequestBody()));
			          String data = body.readLine();
			          if (data == null || data.equals("")) {
			            response = ErrorInfo_EMPTY_REQUEST;
			            t.sendResponseHeaders(ErrorCode_EMPTY_REQUEST,
			                response.getBytes().length);
			          }else{			          			          
			          
			        	  JSONObject orderjson = new JSONObject();
			        	  orderjson = JSONObject.fromObject(data);
			        	  if(orderjson.toString().equals("")){
			        		  response = ErrorInfo_MALFORMED_JSON;
				        	  t.sendResponseHeaders(ErrorCode_MALFORMED_JSON, response.getBytes().length);
			        	  }else{
			            Order oh = new Order();
			            int status_code = oh.OrderHand(data, token);
			            
			            response = oh.return_info;
			            switch (status_code) {
			            case SuccessCode:
			            	System.out.println(response + "............................" + "\n");
			              t.sendResponseHeaders(SuccessCode,
			                  response.getBytes().length);
			              break;

			            case ErrorCode_NOT_AUTHORIZED_TO_ACCESS_CART:
			              response = ErrorInfo_NOT_AUTHORIZED_TO_ACCESS_CART;
			              t.sendResponseHeaders(
			                  ErrorCode_NOT_AUTHORIZED_TO_ACCESS_CART,
			                  response.getBytes().length);
			              break;
			            case ErrorCode_CART_NOT_FOUND:
			              response = ErrorInfo_CART_NOT_FOUND;
			              t.sendResponseHeaders(
			                  ErrorCode_CART_NOT_FOUND - 10,
			                  response.getBytes().length);
			              break;

			            case ErrorCode_ORDER_OUT_OF_LIMIT:
			              response = ErrorInfo_ORDER_OUT_OF_LIMIT;
			              t.sendResponseHeaders(
			                  ErrorCode_ORDER_OUT_OF_LIMIT - 10,
			                  response.getBytes().length);
			              break;
			            case ErrorCode_MALFORMED_JSON:
			              response = ErrorInfo_MALFORMED_JSON;
			              t.sendResponseHeaders(ErrorCode_MALFORMED_JSON,
			                  response.getBytes().length);
			              break;
			            case ErrorCode_FOOD_OUT_OF_STOCK:
			              response = ErrorInfo_FOOD_OUT_OF_STOCK;
			              t.sendResponseHeaders(
			                  ErrorCode_FOOD_OUT_OF_STOCK - 20,
			                  response.getBytes().length);
			            default:
			              break;
			            }
			          }
			          }
			}
			}
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		
	}
		}

	static class admin_ordersHandler extends myHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			// GET /admin/orders?access_token=xxx
			String response = "";
			URI uri = t.getRequestURI();
			System.out.println(uri.toString());
			String token = this.getToken(uri, t);
			if (token == null || TokenCheck.getInstance().checkToken(token)) {
				// token为空
				response = ErrorInfo_INVALID_ACCESS_TOKEN;
				t.sendResponseHeaders(ErrorCode_INVALID_ACCESS_TOKEN,
						response.getBytes().length);
			} else {
					// token有效
					AdminOrders adminorders = new AdminOrders();
					response = adminorders.QueryAdminOrdersHand(token); // 返回数据
					t.sendResponseHeaders(SuccessCode,
							response.getBytes().length);
			}

			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}
	
	private static boolean isFirstLoadToken() {
		// 连接数据库，是否第一次加载
		RedisOperator op = new RedisOperator();
		boolean is = op.checkLoadToken();
		return is;
	}

	private static boolean isFirstLoadFoods() {
		RedisOperator op = new RedisOperator();
		return op.checkLoadAmount();
	}

}

class myHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange t) throws IOException {

		// TODO Auto-generated method stub

	}

	public String getToken(URI uri, HttpExchange t) {
		URI tokenUri = uri;
		tokenUri.getAuthority();
		// 通过Header来传token
		Headers headers = t.getRequestHeaders();
		String token = headers.getFirst("Access-Token");

		// 不在Header中从URi中拿token
		if (token == null || token.equals("")) {
			String query = tokenUri.getQuery();
			if (query == null)
				return null;

			token = query.substring(query.indexOf("=") + 1, query.length());

			System.out.println("parameter:" + token);
			System.out.println("authority:" + tokenUri.getAuthority()
					+ "Userinfo:" + tokenUri.getUserInfo() + "Path:"
					+ tokenUri.getPath() + "Query:" + tokenUri.getQuery()
					+ // token所在的地方
					"fragment:" + tokenUri.getFragment()
					+ "SchemeSpecificpart:" + tokenUri.getSchemeSpecificPart());
			return token;
		} else {
			System.out.println("Header:" + token);
			return token;
		}
		// 解析URI结构
		// System.out.println("authority:"+tokenUri.getAuthority()+
		// "Userinfo:"+tokenUri.getUserInfo()+
		// "Path:"+tokenUri.getPath()+
		// "Query:"+tokenUri.getQuery()+//token所在的地方
		// "fragment:"+tokenUri.getFragment()+
		// "SchemeSpecificpart:"+tokenUri.getSchemeSpecificPart());

		// String query = tokenUri.getQuery();
		// if(query== null) return null;
		//
		// String token = query.substring(query.indexOf("=")+1,query.length());
		// System.out.println(token);

	}
}