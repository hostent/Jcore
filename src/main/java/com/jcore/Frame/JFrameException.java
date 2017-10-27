package com.jcore.Frame;

public class JFrameException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5962595578265058242L;

	public JFrameException(Exception ex, String message) {
		super(ex);

		this.msg = message;

	}

	String msg = "";

	@Override
	public String getMessage() {

		String superMes = super.getMessage();

		return msg + ", " + superMes;
	}

	public static JFrameException configError(String error, Exception ex) {
		JFrameException e = new JFrameException(ex, "配置文件错误：["+error+"]");
		
		Log.errorLog.error(e.getMessage());

		return e;
	}
	
	public static JFrameException apiCallError(String callMsg,String returnMsg, Exception ex) {
		JFrameException e = new JFrameException(ex, "api接口调用错误：call["+callMsg+"]，return["+returnMsg+"]");
		
		Log.errorLog.error(e.getMessage());

		return e;
	}

}
