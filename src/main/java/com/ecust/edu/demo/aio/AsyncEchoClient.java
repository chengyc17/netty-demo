package com.ecust.edu.demo.aio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @Author cheng_ye
 * @create 2021/6/17 20:26
 */
public class AsyncEchoClient {
    public static void main(String[] args) {
//        if (args.length != 2) {
//            System.out.println("用法： java AsyncEchoClient <host name> <port number>");
//            System.exit(1);
//        }

//        String hostName = args[1];
//        int portNumber = Integer.parseInt(args[1]);
        String hostName = "10.32.25.134";
        int portNumber = 10331;

        AsynchronousSocketChannel socketChannel = null;
        try {

            socketChannel = AsynchronousSocketChannel.open();
            socketChannel.connect(new InetSocketAddress(hostName, portNumber));
        } catch (IOException e) {
            System.out.println("AsyncEchoClient异常： " + e.getMessage());
            System.exit(1);
        }

        ByteBuffer writeBuffer = ByteBuffer.allocate(32);
        ByteBuffer readBuffer = ByteBuffer.allocate(32);

        try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                writeBuffer.put(userInput.getBytes());
                writeBuffer.flip();
                writeBuffer.rewind();

                //写消息到管道
                socketChannel.write(writeBuffer);

                //管道读消息
                socketChannel.read(readBuffer);

                //清理缓冲区
                writeBuffer.clear();
                readBuffer.clear();
                System.out.println("echo: " + userInput);

            }
        } catch (UnknownHostException e) {
            System.out.println("不明主机，主机名为： " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("不能从主机中获取I/O,主机名为： " + hostName);
            System.exit(1);
        }
    }
}
