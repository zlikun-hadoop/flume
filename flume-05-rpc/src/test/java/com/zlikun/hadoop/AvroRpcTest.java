package com.zlikun.hadoop;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.SimpleEvent;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-19 18:49
 */
public class AvroRpcTest {

    @Test
    public void test() {

        // 获取Rpc客户端
        RpcClient client = RpcClientFactory.getDefaultInstance("flume.zlikun.com", 4353);

        // 发送数据
        try {
            Event event = new SimpleEvent();
            Map<String, String> headers = new HashMap<>();
            headers.put("author", "zlikun");
            headers.put("version", "v1.0");
            event.setHeaders(headers);
            event.setBody("Hello Flume !".getBytes());

            // 2018-03-19 18:55:42,928 (SinkRunner-PollingRunner-DefaultSinkProcessor) [INFO - org.apache.flume.sink.LoggerSink.process(LoggerSink.java:95)]
            // Event: { headers:{version=v1.0, author=zlikun} body: 48 65 6C 6C 6F 20 46 6C 75 6D 65 20 21          Hello Flume ! }
            client.append(event);
        } catch (EventDeliveryException e) {
            e.printStackTrace();
        }

        // 关闭客户端
        client.close();

    }

}
