package nc.bs.framework.rmi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author He Guan Yu
 * 
 * @date Apr 27, 2010
 * 
 */
public interface RemoteChannel {

	public void init() throws IOException;
	
	public void setRequestHeader(String key, String value);

	public void destroy();

	public InputStream getInputStream() throws IOException;

	public OutputStream getOutputStream() throws IOException;

	public void processIOException(IOException ioe) throws IOException;

}
