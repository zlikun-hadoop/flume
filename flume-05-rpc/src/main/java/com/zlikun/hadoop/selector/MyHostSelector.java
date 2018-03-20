package com.zlikun.hadoop.selector;

import org.apache.flume.api.HostInfo;
import org.apache.flume.api.LoadBalancingRpcClient;

import java.util.Iterator;
import java.util.List;

/**
 * 自定义HostSelector，用于load-balancing RPC客户端
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-20 14:16
 */
public class MyHostSelector implements LoadBalancingRpcClient.HostSelector {

    @Override
    public void setHosts(List<HostInfo> list) {

    }

    @Override
    public Iterator<HostInfo> createHostIterator() {
        return null;
    }

    @Override
    public void informFailure(HostInfo hostInfo) {

    }

}
