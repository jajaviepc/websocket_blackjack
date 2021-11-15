package com.ws.wsblackjack2.globals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Deck {
    private List<String> deck;

    public Deck() {
        deck = new ArrayList<>(Arrays.asList("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"));
    }

    public List<String> getDeck() {
        return deck;
    }
}
