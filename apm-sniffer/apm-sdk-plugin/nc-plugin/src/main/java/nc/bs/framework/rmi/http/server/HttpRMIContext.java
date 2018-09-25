package nc.bs.framework.rmi.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.rmi.server.AbstractRMIContext;

/**
 * 
 * @author He Guan Yu
 * 
 * @date Nov 9, 2010
 * 
 */
public class HttpRMIContext extends AbstractRMIContext {

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	private HttpServletRequest request;

	private HttpServletResponse response;

	@Override
	public InputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

	public HttpRMIContext(HttpServletRequest request,
			HttpServletResponse response) {
		super();
		this.request = request;
		this.response = response;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return response.getOutputStream();
	}

	@Override
	public String getRemoteAddr() {
		return request.getRemoteAddr();
	}

	@Override
	public int getRemotePort() {
		return request.getRemotePort();
	}

	@Override
	public String getRemoteUser() {
		return request.getRemoteUser();
	}

	@Override
	public void setAttribute(String key, Object value) {
		request.setAttribute(key, value);
	}

	@Override
	public Object getAttribute(String key) {
		return request.getAttribute(key);
	}

	@Override
	public void clearAttribute() {

	}

	@Override
	public void removeAttribute(String key) {
		request.removeAttribute(key);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	@Override
	public int getInputContentLength() {
		return request.getContentLength();
	}

	@Override
	public void setOutputContentLength(int length) {
		response.setContentLength(length);
	}

	@Override
	public String getInputContentType() {
		return request.getContentType();
	}

	@Override
	public void setOutputContentType(String contentType) {
		response.setContentType(contentType);
	}
	*/

}
