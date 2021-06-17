package com.ecust.edu.netty;

import lombok.Data;

@Data
public class Message {

    private MessageType type;
    private String echo;



}
