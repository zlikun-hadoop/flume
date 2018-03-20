package com.zlikun.hadoop;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.SimpleEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 通过配置文件来创建（配置）客户端
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-20 13:23
 */
public class RpcClientTest {

    private RpcClient client;

    @Before
    public void init() throws IOException {
        Properties properties = new Properties();
        properties.load(RpcClientTest.class.getClassLoader().getResourceAsStream("flume-client.properties"));
        client = RpcClientFactory.getInstance(properties);
    }

    @After
    public void destroy() {
        if (client != null) {
            client.close();
        }
    }

    @Test
    public void test() {

        ExecutorService exec = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 5; i++) {
            final int index = i;
            exec.submit(() -> {
                List<Event> list = IntStream.range(0, 7)
                        .mapToObj(j -> {
                            Event event = new SimpleEvent();
                            event.setBody(String.format("number_%d_%d", index, j).getBytes());
                            return event;
                        })
                        .collect(Collectors.toList());
                // 批量发送事件
                try {
                    client.appendBatch(list);
                } catch (EventDeliveryException e) {
                    e.printStackTrace();
                }
            });
        }

        exec.shutdown();
        while (!exec.isTerminated());
        System.out.println("数据发送完成 ...");

    }

}
