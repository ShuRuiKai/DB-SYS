package com.cy.pj.common.exception;

public class ServiceException extends RuntimeException {

	/**
	 * 自定义业务异常
	 */
	private static final long serialVersionUID = -5598865415547474216L;
	public ServiceException() {
		super();
	}
	public ServiceException(String message) {
		super(message);
	}
	public ServiceException(Throwable cause) {
		super(cause);
	}
	public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}
	public ServiceException(String message, Throwable cause) {
		super(message, cause);

	} 
}

