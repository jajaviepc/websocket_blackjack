package com.ws.wsblackjack2.globals;

public class MessageType {
    public static final Integer CONNECTION_REQUEST = 0;
    public static final Integer CONNECTION_REFUSED = -1;
    public static final Integer CONNECTION_ACCEPTED = 1;
    public static final Integer ROUND_INIT = 2;
    public static final Integer OTHER_PLAYER_INFO = 3;
    public static final Integer GET_TWO_FIRST_CARDS = 4;
    public static final Integer PLAYER_TURN = 5;
    public static final Integer ASK_CARD = 6;
    public static final Integer STAY = 7;
    public static final Integer END_GAME = 8;
    public static final Integer WELCOME = 99;
    public static final Integer ERROR_CODE=500;

}
