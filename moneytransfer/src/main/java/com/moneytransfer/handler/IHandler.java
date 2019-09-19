package com.moneytransfer.handler;

public interface IHandler {
	
	/**
	 * This Method used to handle request based on methodType and supported path.
	 * @param handle
	 * @param body
	 * @param methodType
	 * @return
	 * @throws Exception
	 */
	String handle(Object handle, String body, String methodType) throws Exception;

	/**
	 * This used to define the supported path like '/account' and '/transfer'.
	 * @param path
	 * @return
	 */
	boolean supportPath(String path);

	/**
	 * This Method used for restrict method type supported like GET/POST/PUT.
	 * @param methodType
	 * @return
	 */
	boolean supportMethod(String methodType);
}
