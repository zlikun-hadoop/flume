package com.zlikun.hadoop.http;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.source.http.HTTPBadRequestException;
import org.apache.flume.source.http.HTTPSourceHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * HTTP Source写处理程序
 * @see org.apache.flume.source.http.JSONHandler
 * @see org.apache.flume.source.http.BLOBHandler
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-20 18:10
 */
public class MyHttpSourceHandler implements HTTPSourceHandler {

    @Override
    public List<Event> getEvents(HttpServletRequest request) throws HTTPBadRequestException, Exception {
        // 将HTTP请求转换为Event事件


        return null;
    }

    @Override
    public void configure(Context context) {

    }

}
