package nc.bs.framework.rmi.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nc.bs.framework.common.InvocationInfo;
import nc.bs.framework.comn.Result;

/**
 * 
 * @author He Guan Yu
 * 
 * @date Oct 20, 2010
 * 
 */
public interface RMIContext {

	public static final String SVC_ATTR = "__SVC_ATTR";

	public InputStream getInputStream() throws IOException;

	public OutputStream getOutputStream() throws IOException;

	public String getRemoteAddr();

	public int getRemotePort();

	public String getRemoteUser();

	public void setAttribute(String key, Object value);

	public Object getAttribute(String key);

	public void clearAttribute();

	public void removeAttribute(String key);

	public InvocationInfo getInvocationInfo();

	public void setInvocationInfo(InvocationInfo info);

	public void setResult(Result result);

	public Result getResult();

	public boolean isCompressed();

	public boolean isEncrypted();

	public void setCompressed(boolean compressed);

	public void setEncrypted(boolean encrypted);

	public int getEncryptType();

	public void setEncryptType(int type);

	public void setTransKey(byte[] key);

	public byte[] getTransKey();

	public int getInputContentLength();

	public void setOutputContentLength(int length);

	public String getInputContentType();

	public void setOutputContentType(String contentType);

}
