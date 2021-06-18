package com.ecust.edu.demo.aio;

import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author cheng_ye
 * @create 2021/6/18 10:57
 */
public class AsyncEchoServer {
    public static int DEFAULT_PORT = 7;

    public static void main(String[] args) {
        int port = 10331;

//        try {
//            port = Integer.parseInt(args[0]);
//        } catch (RuntimeException e) {
//            port = DEFAULT_PORT;
//        }
        AsynchronousServerSocketChannel serverChannel;
        try {
            serverChannel = AsynchronousServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress(port);
            serverChannel.bind(address);

            //设置参数
            serverChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            System.out.println("AsyncEchoServer已启动，端口：" + port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            Future<AsynchronousSocketChannel> future = serverChannel.accept();
            AsynchronousSocketChannel socketChannel = null;
            try {
                socketChannel = future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                System.out.println("AsyncEchoServer异常!");
            }

            System.out.println("AsyncEchoServer接受客户端的连接：" + socketChannel);

            //分配缓存区
            ByteBuffer buffer = ByteBuffer.allocate(100);

            try {
                while (socketChannel.read(buffer).get() != -1) {
                    buffer.flip();
                    socketChannel.write(buffer).get();

                    System.out.println("AsyncEchoServer -> " + socketChannel.getRemoteAddress() + ":" + buffer.toString());

                    if (buffer.hasRemaining()) {
                        buffer.compact();
                    } else {
                        buffer.clear();
                    }
                }
                socketChannel.close();
            } catch (InterruptedException | ExecutionException | IOException e) {
                e.printStackTrace();
                System.out.println("AsyncEchoServer异常!" + e.getMessage());
            }
        }
    }
}
