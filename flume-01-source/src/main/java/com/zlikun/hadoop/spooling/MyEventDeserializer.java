package com.zlikun.hadoop.spooling;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.serialization.EventDeserializer;
import org.apache.flume.serialization.ResettableInputStream;

import java.io.IOException;
import java.util.List;

/**
 * 自定义反序列化器
 * @see org.apache.flume.serialization.LineDeserializer
 * @see org.apache.flume.serialization.AvroEventDeserializer
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-20 18:49
 */
public class MyEventDeserializer implements EventDeserializer {

    private Context context;
    private ResettableInputStream in;

    public MyEventDeserializer(Context context, ResettableInputStream in) {
        this.context = context;
        this.in = in;
    }

    @Override
    public Event readEvent() throws IOException {
        return null;
    }

    @Override
    public List<Event> readEvents(int numEvents) throws IOException {
        return null;
    }

    @Override
    public void mark() throws IOException {

    }

    @Override
    public void reset() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }

    public static class Builder implements EventDeserializer.Builder {

        @Override
        public EventDeserializer build(Context context, ResettableInputStream in) {
            return new MyEventDeserializer(context, in);
        }

    }

}
