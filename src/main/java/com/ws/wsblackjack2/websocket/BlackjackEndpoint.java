package com.ws.wsblackjack2.websocket;

import com.ws.wsblackjack2.error.FullTableException;
import com.ws.wsblackjack2.globals.Deck;
import com.ws.wsblackjack2.globals.MessageType;
import com.ws.wsblackjack2.model.Card;
import com.ws.wsblackjack2.model.Message;
import com.ws.wsblackjack2.model.Player;
import com.ws.wsblackjack2.model.decoder.MessageDecoder;
import com.ws.wsblackjack2.model.encoder.MessageEncoder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/blackjack/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class BlackjackEndpoint {

    private Session session;
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    private static final Set<BlackjackEndpoint> blackjackEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, Player> users = new HashMap<>();
    private Player crupier = new Player();
    private Integer roundNumber = 1;
    private Integer playerCounter = 1;
    private Boolean roundInit = Boolean.TRUE;
    Deck deck = new Deck();
    Random random = new Random();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        if (users.size() < 6) {
            this.session = session;
            sessions.add(session);
            blackjackEndpoints.add(this);
            users.put(session.getId(), new Player(username, 1, users.size() + 1, session.getId()));
            Message message = new Message(MessageType.CONNECTION_ACCEPTED, null);
            initRound(session);
            unicast(this.session, message);
        } else {
            throw new FullTableException("Table already reach maximum of player");
        }
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        handleMessage(session, message);
    }

    @OnClose
    public void onClose(Session session) {
        blackjackEndpoints.remove(this);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
    }

    // ------------------------------- game logic ------------------------------------------------


    public void handleMessage(Session session, Message message) throws EncodeException, IOException {
        System.out.println("Recibiendo mensaje de tipo: " + message.getTipo() + " y valor: " + message.getValor());
        if (message.getTipo().equals(MessageType.CONNECTION_REFUSED)) {
        } else if (message.getTipo().equals(MessageType.CONNECTION_REQUEST)) {
        } else if (message.getTipo().equals(MessageType.CONNECTION_ACCEPTED)) {
        } else if (message.getTipo().equals(MessageType.ROUND_INIT)) {
        } else if (message.getTipo().equals(MessageType.OTHER_PLAYER_INFO)) {
        } else if (message.getTipo().equals(MessageType.GET_TWO_FIRST_CARDS)) {
            giveTwoCardsToPlayer(session);
        } else if (message.getTipo().equals(MessageType.PLAYER_TURN)) {
            validateTurn(session);
        } else if (message.getTipo().equals(MessageType.ASK_CARD)) {
            giveCard(session);
        } else if (message.getTipo().equals(MessageType.STAY)) {
            stay(session);
        } else if (message.getTipo().equals(MessageType.END_GAME)) {
        }
    }

    private void stay(Session session) {
        users.get(session.getId()).setTurn(Boolean.FALSE);
        nextTurn(session);
    }

    private void giveCard(Session session) throws EncodeException, IOException {
        System.out.println("Entregando una carta nueva...");
        Card newCard = generateRandomCard();
        List<Card> cards = users.get(session.getId()).getCards();
        cards.add(newCard);
        users.get(session.getId()).setCards(cards);
        if (users.get(session.getId()).getCount() > 21) {
            System.out.println("El usuario ha superado el puntaje permitido...");
            users.get(session.getId()).setTurn(Boolean.FALSE);
            nextTurn(session);
        }
        unicast(session, new Message(MessageType.ASK_CARD, roundNumber + "##" + newCard.getCard()));
    }

    private void nextTurn(Session session) {
        Integer nextTurn = users.get(session.getId()).getNumber() + 1;
        System.out.println("nexturn: " + nextTurn);
        try {
            if (nextTurn <= users.size()) {
                broadcast(new Message(MessageType.STAY, users.get(session.getId()).getUsername() + "##" + users.get(session.getId()).getCount()));
                System.out.println("Siguiente jugador: " + users.get(String.valueOf(nextTurn - 1)).getUsername());
                users.get(String.valueOf(nextTurn - 1)).setTurn(Boolean.TRUE);
                broadcast(new Message(MessageType.PLAYER_TURN, users.get(String.valueOf(nextTurn - 1)).getUsername()));
            } else if (this.roundNumber <= 3) {
                this.roundNumber++;
                System.out.println("Fin de ronda... Pasando a la siguiente.... " + this.roundNumber);
                broadcast(new Message(MessageType.STAY, users.get(session.getId()).getUsername() + "##" + users.get(session.getId()).getCount()));
                nextRound();
            } else {
                endOfGame();
            }
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }

    }

    private void endOfGame() throws EncodeException, IOException {
        broadcast(new Message(MessageType.END_GAME, "EndGame"));
    }

    private Boolean validateTurn(Session session) throws EncodeException, IOException {
        if (users.get(session.getId()).getTurn()) {
            unicast(session, new Message(MessageType.PLAYER_TURN, String.valueOf(roundNumber)));
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private void firstMovement() {
        blackjackEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    //First movement.
                    if (users.get(endpoint.session.getId()).getNumber().equals(1) && roundInit) {
                        users.get(endpoint.session.getId()).setTurn(Boolean.TRUE);
                        roundInit = Boolean.FALSE;
                        unicast(endpoint.session, new Message(MessageType.PLAYER_TURN, users.get(endpoint.session.getId()).getUsername()));
                    }
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void nextRound() throws IOException, EncodeException {
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    broadcast(new Message(MessageType.ROUND_INIT, String.valueOf(roundNumber)));
                    blackjackEndpoints.forEach(endpoint -> {
                        synchronized (endpoint) {
                            try {
                                users.get(endpoint.session.getId()).setCount(0);
                                giveTwoCardsToPlayer(endpoint.session);
                            } catch (IOException | EncodeException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    roundInit = Boolean.TRUE;
                    firstMovement();
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer("Timer");
        long delay = 5000L;
        timer.schedule(task, delay);
    }

    private void initRound(Session session) throws IOException, EncodeException {
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    broadcast(new Message(MessageType.ROUND_INIT, String.valueOf(roundNumber)));
                    giveTwoCardsToPlayer(session);
                    firstMovement();
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer("Timer");
        long delay = 5000L;
        timer.schedule(task, delay);
    }

    private List<Card> generateRandomCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            cards.add(generateRandomCard());
        }
        return cards;
    }

    private Card generateRandomCard() {
        return new Card(deck.getDeck().get(random.nextInt(13)));
    }

    private void giveTwoCardsToPlayer(Session session) throws EncodeException, IOException {
        users.get(session.getId()).setCards(generateRandomCards());
        System.out.println("Dando cartas al siguiente usuario: " + users.get(session.getId()).getUsername());
        StringBuilder valor = new StringBuilder(String.valueOf(roundNumber));
        users.get(session.getId()).getCards().stream().forEach(card -> {
            valor.append("##" + card.getCard());
        });
        unicast(session, new Message(MessageType.GET_TWO_FIRST_CARDS, valor.toString()));

    }
// ------------------------------- game logic end ------------------------------------------------

    private static void broadcast(Message message) throws IOException, EncodeException {
        blackjackEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote()
                            .sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void unicast(Session session, Message message) throws IOException, EncodeException {
        try {
            session.getBasicRemote()
                    .sendObject(message);
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }


}
