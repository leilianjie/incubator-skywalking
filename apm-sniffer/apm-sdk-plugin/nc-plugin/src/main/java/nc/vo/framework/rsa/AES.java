package nc.vo.framework.rsa;

import java.io.IOException;

/**
 * @author dingtsh
 * @date 2012-11-15
 */
public interface AES {

	public byte[] encrypt(byte[] content) throws IOException;

	public byte[] decrypt(byte[] content) throws IOException;

}
