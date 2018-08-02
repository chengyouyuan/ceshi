package com.winhxd.b2c.common.cache;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class RedisLock extends AbstractLock {

    private Cache cache;

    private Random retryMillisRandom;

    protected String lockKey;

    protected long lockExpires;

    /**
     * @param cache
     * @param lockKey
     * @param lockExpires 锁的有效时长(毫秒)
     */
    public RedisLock(Cache cache, String lockKey, long lockExpires) {
        this.cache = cache;
        this.lockKey = lockKey;
        this.lockExpires = lockExpires;
        this.retryMillisRandom = new Random(System.currentTimeMillis());
    }

    @Override
    protected boolean lock(long timeout, TimeUnit unit, boolean interrupt) throws InterruptedException {

        if (interrupt) {
            checkInterruption();
        }
        long timeoutMillis = unit == null ? 0 : unit.toMillis(timeout);

        while (timeoutMillis >= 0) {
            if (interrupt) {
                checkInterruption();
            }

            long lockExpireTime = System.currentTimeMillis() + lockExpires + 1;
            String stringOfLockExpireTime = String.valueOf(lockExpireTime);
            if (setNX(lockKey, stringOfLockExpireTime)) {
                // 成功获取到锁, 设置相关标识
                locked = true;
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }

            String value = this.get(lockKey);
            if (value != null && isTimeExpired(value)) {
                String oldValue = this.getSet(lockKey, stringOfLockExpireTime);
                if (oldValue != null && oldValue.equals(value)) {
                    locked = true;
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
            }

            long delayMillis = randomDelay();
            long sleepMillis = timeoutMillis < delayMillis ? timeoutMillis : delayMillis;
            Thread.sleep(sleepMillis);
            timeoutMillis = timeoutMillis - sleepMillis == 0 ? -1 : timeoutMillis - sleepMillis;
        }
        return false;
    }

    private long randomDelay() {
        return retryMillisRandom.nextInt(50) + 50;
    }

    public boolean isLocked() {
        if (locked) {
            return true;
        } else {
            String value = cache.get(lockKey);
            return !isTimeExpired(value);
        }
    }

    @Override
    protected void unlock0() {
        // 判断锁是否过期
        String value = cache.get(lockKey);
        if (!isTimeExpired(value)) {
            doUnlock();
        }
    }

    private void checkInterruption() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
    }

    private boolean isTimeExpired(String value) {
        return value == null || Long.parseLong(value) < System.currentTimeMillis();
    }

    private void doUnlock() {
        cache.del(lockKey);
    }

    public String getLockKey() {
        return lockKey;
    }

    private String get(final String key) {
        return cache.get(key);
    }

    private boolean setNX(final String key, final String value) {
        Long result = cache.setnx(key, value);
        return result != null && result == 1;
    }

    private String getSet(final String key, final String value) {
        return cache.getSet(key, value);
    }
}