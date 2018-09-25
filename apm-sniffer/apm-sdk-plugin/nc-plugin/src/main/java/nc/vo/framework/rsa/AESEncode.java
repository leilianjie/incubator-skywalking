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
 * @date 2012-12-6 下午9:04:51
 */
public class AESEncode implements AES {

    public static final String DEFAULT_PROVIDER_NAME = "BC";
    private Cipher cipher;
    private SecretKeySpec key;
    private boolean encrypt = true;

    /**
     * AES加密
     * 
     * @param keyBytes
     * @throws IOException
     */
    public AESEncode(byte[] keyBytes) {
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
    // cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式
    // // } catch (InvalidAlgorithmParameterException e) {
    // // throw new
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

    /**
     * AES加密
     * 
     * @param keyBytes
     * @param padding
     */
    public AESEncode(byte[] keyBytes, boolean noPadding) {
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
                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式
            } catch (InvalidKeyException e) {
                throw new IOException("initial cipher error :invalid key " + e.getMessage());
            }
        } catch (IOException e) {
            // Logger.error("init AESEncode error:", e);
        }
    }

    /**
     * 初始化加密器为加密模式
     * 
     * @param keyBytes
     * @throws IOException
     */
    public void init(byte[] keyBytes) throws IOException {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式
            encrypt = true;
        } catch (InvalidKeyException e) {
            throw new IOException("initial cipher error :invalid key " + e.getMessage());
        }

    }

    /**
     * 加密
     * 
     * @param content
     * @return
     * @throws IOException
     * @throws BadPaddingException
     */
    public byte[] encrypt(byte[] content) throws IOException {
        try {
            return cipher.doFinal(content);
        } catch (IllegalBlockSizeException e) {
            throw new IOException("encrypt error :illegal block size " + e.getMessage());
        } catch (BadPaddingException e) {
            throw new IOException("encrypt error :bad padding " + e.getMessage());
        }
    }

    @Override
    public byte[] decrypt(byte[] content) throws IOException {
        if (encrypt) {// 如果不是解密模式，则初始化为解密模式
            try {
                cipher.init(Cipher.DECRYPT_MODE, key);
            } catch (InvalidKeyException e) {
                throw new IOException("initial cipher DECRYPT_MODE error :invalid key " + e.getMessage());
            }
            encrypt = false;
        }
        try {
            return cipher.doFinal(content);
        } catch (IllegalBlockSizeException e) {
            throw new IOException("encrypt error :illegal block size " + e.getMessage());
        } catch (BadPaddingException e) {
            throw new IOException("decrypt error :bad padding " + e.getMessage());
        }
    }

}
