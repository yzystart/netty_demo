package com.yzy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.NettyRuntime;



public class NettyTest {


    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup =new NioEventLoopGroup();
        EventLoopGroup workerGroup =new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                // tcp/ip协议listen函数中的backlog参数,等待连接池的大小
                .option(ChannelOption.SO_BACKLOG, 100)
                //日志处理器
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    //初始化channel，添加handler
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        //日志处理器
                        p.addLast(new LoggingHandler(LogLevel.INFO));
                    }
                });

        // 启动服务器
        ChannelFuture f = serverBootstrap.bind(9999).sync();
        System.out.println("服务器启动成功。");

        // 等待channel关闭
        f.channel().closeFuture().sync();
    }
}
