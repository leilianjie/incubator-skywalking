package nc.bs.framework.comn;

import nc.bs.framework.util.ObjectPool;
import nc.vo.framework.rsa.AES;
import nc.vo.framework.rsa.AESDecode;
import nc.vo.framework.rsa.AESEncode;

public class AESObjectPool extends ObjectPool<AES> {

    private boolean encrypt = false;

    private byte[] key;
    
    public AESObjectPool(boolean encrypt, byte[] key) {
        super(0, 20);
        this.encrypt = encrypt;
        this.key = key;
    }

    @Override
    protected AES createObject() throws Exception {
        if (encrypt) {
            return new AESEncode(key);
        } else {
            return new AESDecode(key);
        }
    }

    @Override
    protected void beforeRemoveObj(AES obj) {
        // TODO Auto-generated method stub

    }

}
