package com.ws.wsblackjack2.model;

import java.util.List;

public class Player {
    private String id;
    private String username;
    private Integer table;
    private List<Card> cards;
    private Integer count = 0;
    private Integer points = 0;
    private Integer number;
    private Boolean turn;

    public Player() {
    }

    public Player(String username, Integer table, Integer number, String id) {
        this.username = username;
        this.table = table;
        this.number = number;
        this.turn = Boolean.FALSE;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getTable() {
        return table;
    }

    public void setTable(Integer table) {
        this.table = table;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Integer getCount() {
        calculateCount();
        return count;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Boolean getTurn() {
        return turn;
    }

    public void setTurn(Boolean turn) {
        this.turn = turn;
    }

    public String getId() {
        return id;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    private void calculateCount() {
        this.cards.stream().forEach(card -> {
            if (card.getCard().equalsIgnoreCase("A")) {
                this.count += 11;
            } else if (card.getCard().equalsIgnoreCase("2")) {
                this.count += 2;
            } else if (card.getCard().equalsIgnoreCase("3")) {
                this.count += 3;
            } else if (card.getCard().equalsIgnoreCase("4")) {
                this.count += 4;
            } else if (card.getCard().equalsIgnoreCase("5")) {
                this.count += 5;
            } else if (card.getCard().equalsIgnoreCase("6")) {
                this.count += 6;
            } else if (card.getCard().equalsIgnoreCase("7")) {
                this.count += 7;
            } else if (card.getCard().equalsIgnoreCase("8")) {
                this.count += 8;
            } else if (card.getCard().equalsIgnoreCase("9")) {
                this.count += 9;
            } else if (card.getCard().equalsIgnoreCase("10")) {
                this.count += 10;
            } else if (card.getCard().equalsIgnoreCase("J")) {
                this.count += 10;
            } else if (card.getCard().equalsIgnoreCase("Q")) {
                this.count += 10;
            } else if (card.getCard().equalsIgnoreCase("K")) {
                this.count += 10;
            } else if (card.getCard().equalsIgnoreCase("A") && this.count > 21) {
                this.count += 1;
            }
        });

    }
}
