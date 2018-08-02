package com.winhxd.b2c.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 锁的骨架实现, 真正的获取锁的步骤由子类去实现.
 */
public abstract class AbstractLock implements Lock {

    protected volatile boolean locked;

    private Thread exclusiveOwnerThread;

    @Override
    public void lock() {
        try {
            //如果没获取到，一直获取
            while (!lock(0, null, false)) {
                Thread.sleep(500L);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        lock(0, null, true);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        try {
            return lock(time, unit, false);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return false;
    }

    @Override
    public boolean tryLock() {
        try {
            return lock(0, null, false);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return false;
    }

    public boolean tryLockInterruptibly(long time, TimeUnit unit) throws InterruptedException {
        return lock(time, unit, true);
    }

    @Override
    public void unlock() {
        // 检查当前线程是否持有锁
        if (Thread.currentThread() == getExclusiveOwnerThread()) {
            unlock0();
            setExclusiveOwnerThread(null);
        }
    }

    protected void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }

    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }

    protected abstract void unlock0();

    /**
     * 阻塞式获取锁的实现
     *
     * @param timeout
     * @param unit
     * @param interrupt 是否响应中断
     * @return
     * @throws InterruptedException
     */
    protected abstract boolean lock(long timeout, TimeUnit unit, boolean interrupt)
            throws InterruptedException;

}