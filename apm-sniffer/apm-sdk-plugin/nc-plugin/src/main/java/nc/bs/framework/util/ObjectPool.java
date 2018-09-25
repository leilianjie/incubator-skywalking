package nc.bs.framework.util;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 对象池,用于对象池化
 * 
 * @author 赵国滨 2010-7-23
 * 
 */
abstract public class ObjectPool<T> {

    /**
     * 最大数
     */
    protected int max = 8;

    /**
     * 最小数
     */
    protected int min = 0;

    /**
     * 对象池
     */
    LinkedList<T> pool = new LinkedList<T>();

    /**
     * 排它锁
     */
    private ReentrantLock rwLock = new ReentrantLock();

    public ObjectPool() {
        init();
    }

    public ObjectPool(int min, int max) {
        this.min = min;
        this.max = max;
        init();
    }

    public void init() {
        for (int i = 0; i < min; i++) {
            try {
                pool.add(createObject());
            } catch (Exception e) {
                break;
            }
        }
    }

    /**
     * 获取对象，如果池中有对象，则从池中取，否则直接创建新对象，避免阻塞
     */
    public T getObject() throws Exception {
        if (rwLock.tryLock()) {
            try {
                if (pool.size() > 0) {
                    return pool.remove(0);
                }
            } finally {
                rwLock.unlock();
            }
        }
        return createObject();
    }

    /**
     * 将对象归还给池，如果池未满，直接放回，如果池已满，进行必要处理后释放
     */
    public void removeOrStay(T obj) {
        if (rwLock.tryLock()) {
            try {
                if (pool.size() < max) {
                    pool.add(obj);
                    return;
                }
            } finally {
                rwLock.unlock();
            }
        }
        beforeRemoveObj(obj);
    }

    /**
     * 产生新对象,需要重载实现
     */
    abstract protected T createObject() throws Exception;

    /**
     * 移除对象前进行必要处理
     */
    abstract protected void beforeRemoveObj(T obj);
}
