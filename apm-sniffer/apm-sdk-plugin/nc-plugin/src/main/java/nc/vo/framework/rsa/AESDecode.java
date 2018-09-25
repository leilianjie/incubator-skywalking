package nc.vo.framework.rsa;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author dingtsh
 * @date 2012-12-6 下午9:06:18
 */
public class AESDecode implements AES {

    public static final String DEFAULT_PROVIDER_NAME = "BC";
    private Cipher cipher;
    private SecretKeySpec key;
    private boolean encrypt = false;

    /**
     * AES加密
     * 
     * @param keyBytes
     * @throws IOException
     */
    public AESDecode(byte[] keyBytes) {
        this(keyBytes, true);
    }

    // try {
    // // IvParameterSpec ips;
    // key = new SecretKeySpec(keyBytes, "AES");
    // try {
    // cipher = Cipher.getInstance("AES/ECB/NoPadding");
    // // byte[] iv = new BASE64Decoder()
    // // .decodeBuffer("t4JPbY+rXgk=t4JPbY+rXgk=");
    // // ips = new IvParameterSpec(iv);
    // } catch (NoSuchAlgorithmException e) {
    // throw new IOException("get cipher error :no such algorithm "
    // + e.getMessage());
    // } catch (NoSuchPaddingException e) {
    // throw new IOException("get cipher error :no such padding "
    // + e.getMessage());
    // }
    // try {
    // cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式
    // // } catch (InvalidAlgorithmParameterException e) {
    // // throw new
    // //
    // IOException("initial cipher error :InvalidAlgorithmParameterException "
    // // + e.getMessage());
    // } catch (InvalidKeyException e) {
    // throw new IOException("initial cipher error :invalid key "
    // + e.getMessage());
    // }
    // } catch (IOException e) {
    // // Logger.error("init AES error:" + e.getMessage());
    // }
    // }

    public AESDecode(byte[] keyBytes, boolean noPadding) {
        if (Security.getProvider(DEFAULT_PROVIDER_NAME) == null) {
            try {
                String clsName = "org.bouncycastle.jce.provider.BouncyCastleProvider";
                Object o = Class.forName(clsName).newInstance();
                Security.addProvider((Provider) o);
            } catch (Exception e) {
                // Logger.error(e.getMessage(), e);
            }
        }
        try {
            key = new SecretKeySpec(keyBytes, "AES");
            try {
                if (noPadding){
                    if(Security.getProvider(DEFAULT_PROVIDER_NAME) == null){
                        cipher = Cipher.getInstance("AES/ECB/NoPadding");
                    }else
                        cipher = Cipher.getInstance("AES/ECB/NoPadding", DEFAULT_PROVIDER_NAME);
                }
                else{
                    if(Security.getProvider(DEFAULT_PROVIDER_NAME) == null){
                        cipher = Cipher.getInstance("AES");
                    }else
                        cipher = Cipher.getInstance("AES",DEFAULT_PROVIDER_NAME);
                }
            } catch (NoSuchProviderException e) {
                throw new IOException("get cipher error :no such provider " + e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                throw new IOException("get cipher error :no such algorithm " + e.getMessage());
            } catch (NoSuchPaddingException e) {
                throw new IOException("get cipher error :no such padding " + e.getMessage());
            }
            try {
                cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式
            } catch (InvalidKeyException e) {
                throw new IOException("initial cipher error :invalid key " + e.getMessage());
            }
        } catch (IOException e) {
            // Logger.error("init AESDecode error:", e);
        }
    }

    /**
     * 初始化加密器为解密模式
     * 
     * @param keyBytes
     * @throws IOException
     */
    public void init(byte[] keyBytes) throws IOException {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为加密模式
            encrypt = false;
        } catch (InvalidKeyException e) {
            throw new IOException("initial cipher error :invalid key " + e.getMessage());
        }

    }

    /**
     * 解密
     * 
     * @param content
     * @return
     * @throws IOException
     * @throws BadPaddingException
     */
    public byte[] decrypt(byte[] keyBytes) throws IOException {
        try {
            return cipher.doFinal(keyBytes);
        } catch (IllegalBlockSizeException e) {
            throw new IOException("decrypt error :illegal block size " + e.getMessage());
        } catch (BadPaddingException e) {
            throw new IOException("decrypt error :bad padding " + e.getMessage());
        }
    }

    @Override
    public byte[] encrypt(byte[] content) throws IOException {
        if (!encrypt) {// 如果不是加密模式，则初始化为加密模式
            try {
                cipher.init(Cipher.DECRYPT_MODE, key);
            } catch (InvalidKeyException e) {
                throw new IOException("initial cipher ENCRYPT_MODE error :invalid key " + e.getMessage());
            }
            encrypt = true;
        }
        try {
            return cipher.doFinal(content);
        } catch (IllegalBlockSizeException e) {
            throw new IOException("encrypt error :illegal block size " + e.getMessage());
        } catch (BadPaddingException e) {
            throw new IOException("encrypt error :bad padding " + e.getMessage());
        }
    }

}
