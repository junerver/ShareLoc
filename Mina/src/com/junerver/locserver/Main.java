package com.junerver.locserver;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Junerver on 2016/8/12.
 */
public class Main {

    public static void main(String[] args) {
        //使用mina框架创建NioSocket接收器
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        //为接收器设置过滤链
        //将会自动将客户端的数据设置成一行行的数据
        acceptor.getFilterChain().addLast("textLineCodec",new ProtocolCodecFilter(new TextLineCodecFactory()));
        //为接收器设置自定义的处理
        acceptor.setHandler(new SocketHandlder());
        try {
            //接收器绑定本机8000端口
            acceptor.bind(new InetSocketAddress(8000));

            System.out.println("本地服务启动，端口8000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
