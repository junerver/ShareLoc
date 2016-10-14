package com.junerver.locserver;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junerver on 2016/8/12.
 * 不采取实现IoHandler接口的方式，而是继承自mina的一个实现类
 */
public class SocketHandlder extends IoHandlerAdapter {

    private List<IoSession> mSessionList =new ArrayList<>();

    //连接创建
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        mSessionList.add(session);

        System.out.println("create");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        mSessionList.remove(session);
    }

    //接收到消息
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);

        System.out.println((String)message);
        //客户端自己发送的消息自己不接受
        for (IoSession temp : mSessionList) {
            if (temp.equals(session)) {
                continue;
            } else {
                temp.write(message);
            }
        }
    }

}
