package com.xinyunkeji.bigdata.convenience.server.utils.cache;

import java.time.LocalDateTime;

/**
 * 附加当前时间点的缓存封装类,V的具体类型，视实际情况而定
 *
 * @author Yuezejian
 * @date 2020年 09月03日 10:23:55
 */
public class TimedLocalCachedDataDTO<V> {
    private final LocalDateTime time;
    private final V data;

    public TimedLocalCachedDataDTO( V data) {
        this.time = LocalDateTime.now();
        this.data = data;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public V getData() {
        return data;
    }

    public void print() {
        System.out.println("V的实际类型是："+ data.getClass().getName());
    }
}