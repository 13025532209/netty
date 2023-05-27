package io.netty.example.mydemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class EventLoopServer {
    public static void main(String[] args) {
        // 细分2：创建一个独立的 EventLoopGroup
        final EventLoopGroup group = new DefaultEventLoopGroup();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        new ServerBootstrap()
                // boss 和 worker
                // 细分1：boss 只负责 ServerSocketChannel 上 accept 事件     worker 只负责 socketChannel 上的读写
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                        .addLast(new StringDecoder())
//                        .addLast("handler1", new ChannelInboundHandlerAdapter() {
//                            @Override                                         // ByteBuf
//                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                ByteBuf buf = (ByteBuf) msg;
//                                ctx.fireChannelRead(msg); // 让消息传递给下一个handler
//                            }
//                        })
                        .addLast(group, "handler2", new ChannelInboundHandlerAdapter() {
                            @Override                                         // ByteBuf
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                ByteBuf buf = (ByteBuf) msg;
                                System.out.println(msg);
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
