package com.zlikun.hadoop;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 自定义Interceptor必须实现`org.apache.flume.interceptor.Interceptor`及其内部接口`org.apache.flume.interceptor.Interceptor.Builder`
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-12 17:10
 */
public class MyInterceptor implements Interceptor {

    private static final String CONF_HEADER_KEY = "header";
    private static final String DEFAULT_HEADER = "count";

    private final String headerKey;
    private final AtomicLong currentCount;

    public MyInterceptor(Context context) {
        headerKey = context.getString(CONF_HEADER_KEY, DEFAULT_HEADER);
        currentCount = new AtomicLong(0);
    }

    @Override
    public void initialize() {
        System.out.println("initialize MyInterceptor");
    }

    /**
     * 方法必须是线程安全的
     * @param event
     * @return
     */
    @Override
    public Event intercept(Event event) {
        long count = currentCount.incrementAndGet();
        event.getHeaders().put(headerKey, String.valueOf(count));
        return event;
    }

    /**
     * 方法必须是线程安全的
     * @param events
     * @return
     */
    @Override
    public List<Event> intercept(List<Event> events) {
        for (Event event : events) {
            this.intercept(event);
        }
        return events;
    }

    @Override
    public void close() {
        System.out.println("close MyInterceptor");
    }

    public static class Builder implements Interceptor.Builder {

        private Context context;

        @Override
        public Interceptor build() {
            return new MyInterceptor(this.context);
        }

        @Override
        public void configure(Context context) {
            this.context = context;
        }

    }

}
