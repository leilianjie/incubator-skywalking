package nc.bs.framework.comn;

import nc.vo.framework.rsa.DES;

;

/**
 * @author 何冠宇
 * 
 *         Date: 2006-3-30 Time: 19:13:52
 */
public class NetStreamConstants {

	public static final int NC_STREAM_MAGIC = 0x897172;

	public static final DES des = new DES(232);

	// private static AESDecode aesDecode;
	//
	// private static AESEncode aesEncode;
	//
	// private static byte[] transKey;
	//
	// private static byte[] key;

	// private static final byte[] KEY = { -47, -11, 92, -25, 114, 60, 30, -123,
	// -7, 60, 126, 115, -126, 104, 60, -72 };
	//
	// public static final AESEncode localAesEncode = new AESEncode(KEY);
	//
	// public static final AESDecode localAesDecode = new AESDecode(KEY);
	//
	// private static Map<String, byte[]> aesTransKeyMap = new HashMap<String,
	// byte[]>();
	//
	// private static Map<String, AESEncode> aesEncodeMap = new HashMap<String,
	// AESEncode>();
	//
	// private static Map<String, AESDecode> aesDecodeMap = new HashMap<String,
	// AESDecode>();

	public static final int NC_STREAM_BUFFER_SIZE = 8192;

	public final static byte[] NC_STREAM_HEADER = { (byte) NC_STREAM_MAGIC,
			(byte) (NC_STREAM_MAGIC >> 8), (byte) (NC_STREAM_MAGIC >> 16) };

	/**
	 * crypt end code
	 */
	public static final byte ENDEDCODE = (byte) 100;

	public static boolean STREAM_NEED_COMPRESS = "true".equals(System
			.getProperty("nc.stream.compress", "true"));

	public static boolean STREAM_NEED_ENCRYPTED = "true".equals(System
			.getProperty("nc.stream.encrypted", "true"));

	public static int STREAM_ENCRYPTED_TYPE = "DES"
			.equalsIgnoreCase(System
					.getProperty("nc.stream.encryptType", "DES")) ? 0 : ("AES"
			.equalsIgnoreCase(System.getProperty("nc.stream.encryptType")) ? 1
			: 2);

	public static boolean STREAM_AUTO_ADAPT = "true".equals(System
			.getProperty("nc.stream.autoAdapt"));

	public static boolean STREAM_NEED_STATISTIC = "true".equals(System
			.getProperty("nc.stream.statistic"));

	// static AESEncode getAesEncode(String hostName) throws IOException {
	// Logger.debug("----------------Get Encode-----hostname:" + hostName);
	// AESEncode aesEncode = NetStreamConstants.aesEncodeMap.get(hostName);
	// if (aesEncode == null)
	// synchronized (NetStreamConstants.class) {
	// aesEncode = NetStreamConstants.aesEncodeMap.get(hostName);
	// if (aesEncode == null) {
	// byte[] aesKey = NetStreamConstants.getAesTransKey(hostName);
	// if (aesKey == null)
	// throw new IOException(
	// "can not find aesEncode key for hostName:"
	// + hostName);
	// byte[] key = AESKeyGenerator.genKey(aesKey);
	// StringBuffer str = new StringBuffer();
	// for (byte b : key)
	// str.append(b);
	// Logger.debug("-----AES Encode Key:" + str + "---hostName:"
	// + hostName);
	// aesEncode = new AESEncode(key);
	// NetStreamConstants.aesEncodeMap.put(hostName, aesEncode);
	// }
	// }
	// return aesEncode;
	// }
	//
	// static AESDecode getAesDecode(String hostName) throws IOException {
	// Logger.debug("--------------Get Decode-----hostname:" + hostName);
	// AESDecode aesDecode = NetStreamConstants.aesDecodeMap.get(hostName);
	// if (aesDecode == null)
	// synchronized (NetStreamConstants.class) {
	// aesDecode = NetStreamConstants.aesDecodeMap.get(hostName);
	// if (aesDecode == null) {
	// byte[] aesKey = NetStreamConstants.getAesTransKey(hostName);
	// if (aesKey == null)
	// throw new IOException(
	// "can not find aesDecode key for hostName:"
	// + hostName);
	// byte[] key = AESKeyGenerator.genKey(aesKey);
	// StringBuffer str = new StringBuffer();
	// for (byte b : key)
	// str.append(b);
	// Logger.debug("-----AES Decode Key:" + str + "---hostName:"
	// + hostName);
	// aesDecode = new AESDecode(key);
	// NetStreamConstants.aesDecodeMap.put(hostName, aesDecode);
	// }
	// }
	// return aesDecode;
	// }
	//
	// static AESEncode getRemoteAesEncode() throws IOException {
	// Logger.debug("----------Get remote AES Encode---------");
	// if (aesEncode == null)
	// synchronized (NetStreamConstants.class) {
	// if (aesEncode == null) {
	// NetStreamConstants.key = AESKeyGenerator
	// .genKey(getTranskey());
	// StringBuffer str = new StringBuffer();
	// for (byte b : NetStreamConstants.key)
	// str.append(b);
	// Logger.debug("-----remote AES Encode Key:" + str);
	// aesEncode = new AESEncode(NetStreamConstants.key);
	// }
	// }
	// return aesEncode;
	// }
	//
	// static AESDecode getRemoteAesDecode() throws IOException {
	// Logger.debug("-----------Get remote AES Decode----------");
	// if (aesDecode == null)
	// synchronized (NetStreamConstants.class) {
	// if (aesDecode == null) {
	// if (NetStreamConstants.key == null)
	// NetStreamConstants.key = AESKeyGenerator
	// .genKey(getTranskey());
	// StringBuffer str = new StringBuffer();
	// for (byte b : NetStreamConstants.key)
	// str.append(b);
	// Logger.debug("-----remote AES Decode Key:" + str);
	// aesDecode = new AESDecode(NetStreamConstants.key);
	// }
	// }
	// return aesDecode;
	// }
	//
	// static byte[] getAesTransKey(String hostName) throws IOException {
	// Logger.debug("--------------Get AES transKey---hostname:" + hostName);
	// return NetStreamConstants.aesTransKeyMap.get(hostName);
	// }
	//
	// static synchronized void putAesTransKey(String hostName, byte[] key) {
	// Logger.debug("--------------Put AES transKey---hostname:" + hostName);
	// NetStreamConstants.aesTransKeyMap.put(hostName, key);
	// }
	//
	// static byte[] getTranskey() throws IOException {
	// Logger.debug("------------------get transKey ------------");
	// if (NetStreamConstants.transKey == null) {
	// synchronized (NetStreamConstants.class) {
	// if (NetStreamConstants.transKey == null) {
	// NetStreamConstants.transKey = AESKeyGenerator.genTransKey();
	// }
	// }
	// }
	// return NetStreamConstants.transKey;
	// }

	public static class EnctyptType {

		public static final int DES = 0;

		public static final int AES = 1;

		public static final int DynamicAES = 2;
	}
}
