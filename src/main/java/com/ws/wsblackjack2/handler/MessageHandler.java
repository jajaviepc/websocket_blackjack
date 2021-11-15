package com.ws.wsblackjack2.handler;

import com.ws.wsblackjack2.globals.MessageType;
import com.ws.wsblackjack2.model.Message;

public class MessageHandler {

    public static Message handleMessage(Message message) {
        //TODO - Hacer todos los casos...
        if (message.getTipo() == MessageType.CONNECTION_REFUSED) {
            return new Message();
        } else if (message.getTipo() == MessageType.CONNECTION_REQUEST) {
            return new Message();
        } else if (message.getTipo() == MessageType.CONNECTION_ACCEPTED) {
            return new Message();
        } else if (message.getTipo() == MessageType.ROUND_INIT) {
            return new Message();
        } else if (message.getTipo() == MessageType.OTHER_PLAYER_INFO) {
            return new Message();
        } else if (message.getTipo() == MessageType.GET_TWO_FIRST_CARDS) {
            return new Message();
        } else if (message.getTipo() == MessageType.PLAYER_TURN) {
            return new Message();
        } else if (message.getTipo() == MessageType.ASK_CARD) {
            return new Message();
        } else if (message.getTipo() == MessageType.STAY) {
            return new Message();
        } else if (message.getTipo() == MessageType.WELCOME) {
            return new Message();
        } else {
            return new Message();
        }
    }
}
