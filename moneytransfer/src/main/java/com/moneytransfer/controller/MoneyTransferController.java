package com.moneytransfer.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.moneytransfer.exception.MoneyTransferException;
import com.moneytransfer.handler.AccountHandler;
import com.moneytransfer.handler.IHandler;
import com.moneytransfer.handler.MoneyTranserHandler;
import com.moneytransfer.wrapper.ResponseWrapper;

/**
 * This Class will delegate the request to specific handler base on path and method type.
 * @author Vikas Gupta
 *
 */
public class MoneyTransferController extends HttpServlet {

	private static final long serialVersionUID = -8134066033388201713L;

	private static List<IHandler> handlers = new ArrayList<>();
	{
		handlers.add(new MoneyTranserHandler());
		handlers.add(new AccountHandler());
	}

	/**
	 * Using this default service method of HttpServlet to delegate to handler class.
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException  {
		String methodType = request.getMethod();
		String path = request.getRequestURI();
		Map<String, String[]> requestParams = request.getParameterMap();
		final ResponseWrapper output = new ResponseWrapper();
		handlers.forEach(handler -> {
			if (handler.supportPath(path) && handler.supportMethod(methodType)) {
				try {
					output.setResponse(handler.handle(requestParams, readRequestBody(request), methodType));
				} catch (Exception e) {
					response.setStatus(404);
					output.setResponse(e.getCause() == null ? e.getMessage() : (e.getMessage() + e.getCause()));
				}
			}
		});
		writeResponse(response, output.getResponse());
	}
	
	/**
	 * This Method use for parsing request body.
	 * @param request
	 * @return
	 * @throws MoneyTransferException 
	 */
	private String readRequestBody(HttpServletRequest request) throws MoneyTransferException {
		StringBuffer requestBody = new StringBuffer();
		try {
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = "";
			while((line = bufferReader.readLine()) != null) {
				requestBody.append(line);
			}
		} catch (IOException e) {
			throw new MoneyTransferException("Error during read request Body : ", e.getCause());
		}
		return requestBody.toString();
	}

	private void writeResponse(HttpServletResponse response, String output) throws IOException {
		response.getWriter().write(output);
		response.getWriter().flush();
	}
}
