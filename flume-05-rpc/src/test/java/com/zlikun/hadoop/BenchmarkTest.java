package com.zlikun.hadoop;

import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 通过配置文件来创建（配置）客户端
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-20 13:23
 */
public class BenchmarkTest {

    private RpcClient client;
    private long time;

    @Before
    public void init() throws IOException {
        Properties properties = new Properties();
        properties.load(BenchmarkTest.class.getClassLoader().getResourceAsStream("flume-client.properties"));
        client = RpcClientFactory.getInstance(properties);
        this.time = System.currentTimeMillis();
    }

    @After
    public void destroy() {
        if (client != null) {
            client.close();
        }
        System.out.println(String.format("程序执行耗时：%d 毫秒!", System.currentTimeMillis() - time));
    }

    @Test
    public void test() {

        ExecutorService exec = Executors.newFixedThreadPool(1);

        final AtomicLong counter = new AtomicLong();
        for (int i = 0; i < 10_000; i++) {
            exec.submit(() -> {
                long index = counter.incrementAndGet();
                // 批量发送事件
                try {
                    /* --------------------------------------------------------------------------------------------------------------------
                     * 不知道为什么批次超过10的时候会出错
                    org.apache.flume.EventDeliveryException: NettyAvroRpcClient { host: flume.zlikun.com, port: 4353 }: Failed to send batch
                        at org.apache.flume.api.NettyAvroRpcClient.appendBatch(NettyAvroRpcClient.java:314)
                        at com.zlikun.hadoop.BenchmarkTest.lambda$test$1(BenchmarkTest.java:57)
                        at java.util.concurrent.Executors$RunnableAdapter.call$$$capture(Executors.java:511)
                        at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java)
                        at java.util.concurrent.FutureTask.run$$$capture(FutureTask.java:266)
                        at java.util.concurrent.FutureTask.run(FutureTask.java)
                        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
                        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
                        at java.lang.Thread.run(Thread.java:748)
                    Caused by: org.apache.flume.EventDeliveryException: RPC failed, client in an invalid state: DEAD
                        at org.apache.flume.api.NettyAvroRpcClient.assertReady(NettyAvroRpcClient.java:434)
                        at org.apache.flume.api.NettyAvroRpcClient.appendBatch(NettyAvroRpcClient.java:321)
                        at org.apache.flume.api.NettyAvroRpcClient.appendBatch(NettyAvroRpcClient.java:302)
                        ... 8 more
                    注：原因找到了，服务端的配置`agent1.channels.channel1.transactionCapacity = 10`造成，修改为100即可
                    -------------------------------------------------------------------------------------------------------------------- */
                    client.appendBatch(IntStream.range(0, 100)
                            .mapToObj(j -> EventBuilder.withBody(String.format("[x=%05d,y=%03d]", index, j).getBytes()))
                            .collect(Collectors.toList()));
                } catch (EventDeliveryException e) {
                    e.printStackTrace();
                }
            });
        }

        exec.shutdown();
        while (!exec.isTerminated());

        // 10000 * 100 -> 105,941 ms -> 9.4/ms
        System.out.println("数据发送完成 ...");

    }

}
