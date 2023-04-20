package com.yzy;
import io.netty.bootstrap.ServerBootstrap; // 导入Netty的ServerBootstrap类
import io.netty.channel.ChannelFuture; // 导入Netty的ChannelFuture类
import io.netty.channel.ChannelInitializer; // 导入Netty的ChannelInitializer类
import io.netty.channel.ChannelOption; // 导入Netty的ChannelOption类
import io.netty.channel.EventLoopGroup; // 导入Netty的EventLoopGroup类
import io.netty.channel.nio.NioEventLoopGroup; // 导入Netty的NioEventLoopGroup类
import io.netty.channel.socket.SocketChannel; // 导入Netty的SocketChannel类
import io.netty.channel.socket.nio.NioServerSocketChannel; // 导入Netty的NioServerSocketChannel类

public class NettyServer {

    private int port; // 服务器端口号

    public NettyServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // 创建主线程组，用于接受客户端连接
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 创建工作线程组，用于处理连接的流量
        try {
            ServerBootstrap b = new ServerBootstrap(); // 创建ServerBootstrap实例
            b.group(bossGroup, workerGroup) // 设置主线程组和工作线程组
                    .channel(NioServerSocketChannel.class) // 设置通道实现类为NioServerSocketChannel
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 设置处理器
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler()); // 将NettyServerHandler添加到处理器管道中
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置通道参数
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // 设置通道参数

            ChannelFuture f = b.bind(port).sync(); // 绑定端口并开始接受连接
            f.channel().closeFuture().sync(); // 监听通道关闭事件
        } finally {
            workerGroup.shutdownGracefully(); // 优雅地关闭工作线程组
            bossGroup.shutdownGracefully(); // 优雅地关闭主线程组
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080; // 设置服务器端口号为8080
        new NettyServer(port).run(); // 创建NettyServer实例并启动
    }

}
