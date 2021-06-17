package com.ecust.edu.netty.server.handler;

import com.ecust.edu.netty.Message;
import com.ecust.edu.netty.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoHandler extends SimpleChannelInboundHandler {

    private Logger LOGGER = LoggerFactory.getLogger(EchoHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        if (message.getType() == MessageType.ECHO) {
            String echo = message.getEcho();
            System.out.println(echo);
        } else {
            ctx.fireChannelRead(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("exception caught");
        LOGGER.error(cause.getMessage(), cause);
    }


}
