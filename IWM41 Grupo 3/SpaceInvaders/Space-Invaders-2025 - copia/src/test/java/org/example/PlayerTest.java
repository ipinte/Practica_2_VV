package org.example;

import main.Commons;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Player;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {


    Player player;
    KeyEvent pressLeft;
    KeyEvent pressRight;
    JPanel dummy;

    @BeforeEach
    void setUp() {
        player = new Player();
        dummy = new JPanel();
        pressLeft = new KeyEvent(dummy, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
        pressRight = new KeyEvent(dummy, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);

    }

    @AfterEach
    void tearDown() {
        player = null;
    }

    // --- Pruebas para initPlayer() ---
    @Test
    void inicializarPlayerX() {
        int Xesperada= (Commons.BOARD_WIDTH - Commons.PLAYER_WIDTH) / 2;
        assertEquals(Xesperada, player.getX(), "El jugador no está centrado " );  //ARREGLADO, HABIA Q PONER 171 EN LUGAR DE 179
    }

    @Test
    void inicializarPlayerY() {
        int Yesperada = Commons.GROUND - Commons.PLAYER_HEIGHT;
        assertEquals(Yesperada, player.getY(), "Altura del jugador no está en el suelo");
    }

    // --- Pruebas para act() ---
    @Test
    void actualizarPosicionPlayerDerecha() {
        int startX = player.getX();

        player.keyPressed(pressRight);
        player.act();

        assertEquals(startX + 2, player.getX(), "La posición X del jugador no se ha actualizado correctamente al pulsar derecha");

    }

    @Test
    void actualizarPosicionPlayerIzquierda() {
        int startX = player.getX();
        player.keyPressed(pressLeft);
        player.act();

        assertEquals(startX - 2, player.getX(), "La posición X del jugador no se ha actualizado correctamente al pulsar izquierda");

    }

    @Test
    void actualizarPosicionPlayerNoMover() {
        int startX = player.getX();

        player.act();

        assertEquals(startX, player.getX(), "La posición X del jugador se ha actualizado sin pulsar ninguna tecla");

    }

    @Test
    void playerNoSeMueveFueraDeLimitesDer() {
        int startX = Commons.BOARD_WIDTH;
        player.setX(startX);

        player.keyPressed(pressRight);
        player.act();

        boolean resultado = player.getX() < Commons.BOARD_WIDTH - Commons.PLAYER_WIDTH;
        assertTrue(resultado);
    }

    @Test
    void playerNoSeMueveFueraDeLimitesIzq() {
        int startX = 0;
        player.setX(startX);

        player.keyPressed(pressLeft);
        player.act();

        boolean resultado = player.getX() > 0;
        assertTrue(resultado);

    }

    // --- Pruebas para keyPressed(keyEvent e) ---
    @Test
    void actualizarPosicionKeyPressedOtraTecla(){
        int startX = player.getX();

        KeyEvent otraTecla = new KeyEvent(dummy, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_E, KeyEvent.CHAR_UNDEFINED);

        player.keyPressed(otraTecla);
        player.act();

        assertEquals(startX, player.getX(), "La posición X del jugador no se ha actualizado correctamente al pulsar otra tecla");
    }

    // --- Pruebas para keyReleased(keyEvent e) ---
    @Test
    void actualizarPosicionKeyReleasedIzquierda() {
        int startX = player.getX();

        player.keyPressed(pressLeft);
        player.act();

        player.keyReleased(pressLeft);
        player.act();

        assertEquals(startX - 2, player.getX(), "La posición X del jugador no se ha actualizado correctamente al dejar de pulsar tecla izquierda");
    }



    @Test
    void actualizarPosicionKeyReleasedDerecha() {
        int startX = player.getX();

        player.keyPressed(pressRight);
        player.act();

        player.keyReleased(pressRight);
        player.act();


        assertEquals(startX + 2, player.getX(), "La posición X del jugador no se ha actualizado correctamente al dejar de pulsar tecla derecha");

    }



    @Test
    void actualizarPosicionKeyReleasedOtraTecla() {
        int startX = player.getX();

        KeyEvent OtraTecla = new KeyEvent(dummy, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_E, KeyEvent.CHAR_UNDEFINED);

        player.keyPressed(OtraTecla);
        player.act();

        player.keyReleased(OtraTecla);
        player.act();

        assertEquals(startX, player.getX(), "La posición X del jugador no se ha actualizado correctamente al dejar de pulsar otra tecla");

    }
}