package com.xinyunkeji.bigdata.convenience.server.utils.cache;

import java.time.LocalDateTime;

import java.time.Duration;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 可验证是否过期的本地缓存(可设置过期时间，默认5分钟)
 *
 * @author Yuezejian
 * @date 2020年 09月03日 10:28:08
 */
public class TimedLocalCache<K, V> extends WeakHashMap<K,TimedLocalCachedDataDTO<V>> {
    private final long timeout;

    public TimedLocalCache(long timeout) {
        this.timeout = TimeUnit.MINUTES.toSeconds(timeout);
    }

    /**
     *  Cache just keep 5 minutes.
     *  This is the default behiver.
     *
     */
    public TimedLocalCache() {
        this.timeout = TimeUnit.MINUTES.toSeconds(5);
    }

    /**
     * Get timeout
     * @return
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     *  Put cache, use the constructor of TimedLocalCachedDataDTO to put value,
     *  now time will be  added defaulted.
     * @param key
     * @param value
     * @return
     */
    public TimedLocalCachedDataDTO<V> putCache(K key,V value) {
        TimedLocalCachedDataDTO dataDTO = new TimedLocalCachedDataDTO(value);
        return super.put(key,dataDTO);
    }

    /**
     * Get cache,we can get the value,
     * only when the cache time haven't overstep the timeout
     * which we set or defaluted
     * @param key
     * @return
     */
    public V getCache(K key) {
        TimedLocalCachedDataDTO<V> dataDTO = super.get(key);
        if (dataDTO == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = dataDTO.getTime();
        long seconds = Duration.between(time,now).getSeconds();
        if (seconds > getTimeout()) {
            remove(key);
            return null;
        }
        return dataDTO.getData();

    }

}