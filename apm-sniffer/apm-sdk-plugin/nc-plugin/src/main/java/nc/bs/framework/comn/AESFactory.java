package nc.bs.framework.comn;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import nc.vo.framework.rsa.AES;

/**
 * @author dingtsh
 * @date 2013-3-20 下午1:38:37
 */
public class AESFactory {

    // private static final int AES_MAP_MAX_SIZE = 128;

    private static final int AES_KEY_LENGTH = 16;

    private static final int LONG_TO_BYTE = 8;

    private static final int BYTE_LENGTH = 8;

    private static final long SUB_KEY_INFO = 0xDA425C9AB37FEB6CL;

    private static int aesKeyLength = AES_KEY_LENGTH;

    public static final byte[] DEFAULT_KEY = genAesKey(0);
    // { -47, -11, 92, -25, 114, 60, 30, -123,
    // -7, 60, 126, 115, -126, 104, 60, -72 };

    // private static final AESObjectPool defaultAesEncode = new
    // AESObjectPool(true, KEY);

    // private static final AESObjectPool defaultAesDecode = new
    // AESObjectPool(false, KEY);

    private static Map<Integer, AESObjectPool> aesEncodeMap = new HashMap<Integer, AESObjectPool>();

    private static Map<Integer, AESObjectPool> aesDecodeMap = new HashMap<Integer, AESObjectPool>();

    private static String ClientIP = "";

    private static byte[] transKey = { -1 };

    static AES getAesEncode(byte transKey) throws IOException {
        int index = transKey & 0x7F;// (int) (hash % AES_MAP_MAX_SIZE);
        AESObjectPool pool = AESFactory.aesEncodeMap.get(index);
        AES aesEncode = null;
        if (pool == null) {
            synchronized (AESFactory.aesEncodeMap) {
                pool = AESFactory.aesEncodeMap.get(index);
                if (pool == null) {
                    byte[] aesKey = genAesKey(index);
                    pool = new AESObjectPool(true, aesKey);
                    AESFactory.aesEncodeMap.put(index, pool);
                }
            }
        }
        try {
            aesEncode = pool.getObject();
//            AESFactory.aesEncodeMap.put(index, pool);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return aesEncode;
    }

    static AES getAesDecode(byte transKey) throws IOException {
        int index = transKey & 0x7F;// (int) (hash % AES_MAP_MAX_SIZE);
        AESObjectPool pool = AESFactory.aesDecodeMap.get(index);
        AES aesDecode = null;
        if (pool == null) {
            synchronized (AESFactory.aesDecodeMap) {
                pool = AESFactory.aesDecodeMap.get(index);
                if (pool == null) {
                    byte[] aesKey = genAesKey(index);
                    pool = new AESObjectPool(false, aesKey);
                    AESFactory.aesDecodeMap.put(index, pool);
                }
            }
        }
        try {
            aesDecode = pool.getObject();
//            AESFactory.aesDecodeMap.put(index, pool);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return aesDecode;
    }

    static void returnAesEncode(byte transKey, AES aes) throws IOException {
        int index = transKey & 0x7F;// (int) (hash % AES_MAP_MAX_SIZE);
        AESObjectPool pool = AESFactory.aesEncodeMap.get(index);
        if (pool == null) {
            synchronized (AESFactory.aesEncodeMap) {
                pool = AESFactory.aesEncodeMap.get(index);
                if (pool == null) {
                    byte[] aesKey = genAesKey(index);
                    pool = new AESObjectPool(true, aesKey);
                    AESFactory.aesEncodeMap.put(index, pool);
                }
            }
        } else {
            pool.removeOrStay(aes);
//            AESFactory.aesEncodeMap.put(index, pool);
        }
    }

    static void returnAesDecode(byte transKey, AES aes) throws IOException {
        int index = transKey & 0x7F;// (int) (hash % AES_MAP_MAX_SIZE);
        AESObjectPool pool = AESFactory.aesDecodeMap.get(index);
        if (pool == null) {
            synchronized (AESFactory.aesDecodeMap) {
                pool = AESFactory.aesDecodeMap.get(index);
                if (pool == null) {
                    byte[] aesKey = genAesKey(index);
                    pool = new AESObjectPool(false, aesKey);
                    AESFactory.aesDecodeMap.put(index, pool);
                }
            }
        } else {
            pool.removeOrStay(aes);
//            AESFactory.aesDecodeMap.put(index, pool);
        }
    }

    private static long getKeyInfo() {
        if (ClientIP == null || ClientIP.equals("")) {
            try {
                ClientIP = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
            }
        }
        if (ClientIP == null || ClientIP.equals(""))
            ClientIP = "uap:localHost";

        return ClientIP.hashCode();
    }

    public static byte[] genTransKey() {
        if (transKey[0] > -1) {
            return transKey;
        }
        byte[] keys = long2bytes(getKeyInfo());
        // byte[] transKey = new byte[1];
        transKey[0] = keys[7];
        return transKey;
    }

    private static byte[] genAesKey(int index) {
        int length = AESFactory.aesKeyLength;
        byte[] key = new byte[length];
        int count = length / AESFactory.LONG_TO_BYTE;
        byte[] tempBytes = new byte[BYTE_LENGTH];
        long tempLong = 0L;
        for (int i = 0; i < count; i++) {
            tempLong = (AESFactory.SUB_KEY_INFO + index) >>> ((i + 1) * 2);
            tempBytes = long2bytes(tempLong);
            System.arraycopy(tempBytes, 0, key, i * BYTE_LENGTH, BYTE_LENGTH);
        }
        return key;
    }

    // /**
    // *
    // * 将一个8字节数组转变成一个长整形。
    // */
    // private static long bytes2long(byte[] rd) {
    // long dd = 0;
    // for (int i = 0; i <= 7; i++)
    // dd = (dd << 8) | ((long) rd[i] & 0xff);
    // return dd;
    // }

    /**
     * 
     * 将long变成一个数组
     */
    private static byte[] long2bytes(long sd) {
        byte[] dd = new byte[8];
        for (int i = 7; i >= 0; i--) {
            dd[i] = (byte) sd;
            sd >>>= 8;
        }
        return dd;
    }

    public static int getAesKeyLength() {
        return aesKeyLength;
    }

    public static void setAesKeyLength(int aesKeyLength) {
        AESFactory.aesKeyLength = aesKeyLength;
    }

}
