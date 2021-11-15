var ws;

function connect() {
    var username = document.getElementById("username").value;

    var host = document.location.host;
    var pathname = document.location.pathname;

    var ronda;
    var carta1;
    var carta2;
    var usuario;
    var puntaje;

    ws = new WebSocket("ws://" + host + pathname + "blackjack/" + username);

    ws.onmessage = function(event) {
        var log = document.getElementById("log");
        console.log(event.data);
        var message = JSON.parse(event.data);
        console.log (message.tipo + " :--1---: " + message.valor + "\n");
        //log.innerHTML += message.tipo + " : " + message.valor + "\n";



        if (message.tipo == 1)
        {
            console.log("1");
            log.innerHTML += " Tipo:" + message.tipo + " Conectado  \n";
        } else if (message.tipo == 2)
        {
            console.log("2");
            log.innerHTML += " Tipo:" + message.tipo + "Numero de ronda: " + message.valor + "\n";


        }else if (message.tipo == 3)
        {

            console.log("3");

        }else if (message.tipo == 4)
        {
            console.log("4");

            let split = message.valor.split('##');
            //console.log(split);

            ronda = split[0];
            carta1 = split[1];
            carta2 = split[2];
            //console.log(ronda);
            //console.log(carta1);
            //console.log(carta2);
            log.innerHTML += " Tipo: " + message.tipo + " Valor: " + message.valor + "\n Carta 1: " + carta1 +"\n Carta 2: " + carta2 + "\n";

        }else if (message.tipo == 5 && message.valor == username)
        {   mensaje="Hola mundo"
            log.innerHTML += " Tipo:" + message.tipo + " Es tu turno ronda: " + message.valor + "\n";
            console.log("5");
            buttons.innerHTML = '<button type="button" onclick="send(6,mensaje);" >Pedir Carta</button>';
            buttons.innerHTML += '<button type="button" onclick="send(7,mensaje);" >Quedarse</button>';


        }else if (message.tipo == 6)
        {
            log.innerHTML += " Tipo:" + message.tipo + " Recibir carta: " + message.valor + "\n";
            console.log("6");

        }else if (message.tipo == 7)
        {


            let split = message.valor.split('##');
            //console.log(split);

            usuario = split[0];
            puntaje = split[1];
            console.log("7");
            log.innerHTML += " Tipo:" + message.tipo +  " El puntaje de "+usuario+ " es: "+ puntaje + "\n";
            buttons.innerHTML= '<a></a>';

        }else if (message.tipo == 8)
        {
            log.innerHTML += " Tipo:" + message.tipo + "\n";
            console.log("8");

        }else if (message.tipo == 99)
        {
            log.innerHTML += " Tipo:" + message.tipo + " " + message.valor + ":\n";

        }
    };
}

function send(tipo, valor) {
    var json = JSON.stringify({
        "tipo": tipo,
        "valor": valor
    });
    console.log("message", json)
    ws.send(json);
}