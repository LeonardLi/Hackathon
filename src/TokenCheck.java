public class TokenCheck {

	private static class TokenCheckHolder {
		private static final TokenCheck INSTANCE = new TokenCheck();
	}

	private TokenCheck() {

	}

	public boolean checkToken(String token) {
		return TokenGenerator.getInstance().User2Token.containsValue(token);
	}

	public static final TokenCheck getInstance() {
		return TokenCheckHolder.INSTANCE;
	}
}
