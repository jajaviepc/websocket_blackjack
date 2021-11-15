package com.ws.wsblackjack2.model;

public class Message {
    private Integer tipo;
    private String valor;

    public Message() {
    }

    public Message(Integer tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
