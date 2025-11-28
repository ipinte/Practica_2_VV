package space_invaders.sprites;

import main.Commons;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class PlayerWhiteBoxTest {

    Player player;
    KeyEvent pressLeft;
    KeyEvent pressRight;
    JPanel dummy;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        player = new Player();
        dummy = new JPanel();
        pressLeft = new KeyEvent(dummy, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
        pressRight = new KeyEvent(dummy, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    // --- Pruebas para initPlayer() [CC = 1] ---
    @Test
    void initPlayer_probarInicializacion() {
        Player player = new Player();

        boolean resultado = (player.getX() == 179) && (player.getY() == 280) && (player.getImage() != null);
        assertTrue(resultado);
    }

    // --- Pruebas para act() [CC = 3] ---

    @Test
    void act_dentroLimites() {
        Player player = new Player();
        player.setX(100);
        player.keyPressed(pressRight);
        player.act();
        assertEquals(102, player.getX(), "La posición X del jugador no se ha actualizado correctamente dentro de los límites");
    }

    @Test
    void act_fueraLimiteIzquierdo() {
        Player player = new Player();
        player.setX(3);
        player.keyPressed(pressLeft);
        player.act();
        assertEquals(2, player.getX(), "La posición X del jugador no se ha corregido correctamente al salir por el límite izquierdo");
    }


    @Test
    void act_fueraLimiteDerecho() {
        Player player = new Player();
        var playerImg = "src/main/resources/images/player.png";
        var ii = new ImageIcon(playerImg);
        int PlayerWidth = ii.getImage().getWidth(null);
        int limiteDerecho = Commons.BOARD_WIDTH - 2 * PlayerWidth;
        player.setX(limiteDerecho - 1);
        player.keyPressed(pressRight);
        player.act();
        assertEquals(Commons.BOARD_WIDTH + 2 * PlayerWidth, player.getX(), "La posición X del jugador no se ha corregido correctamente al salir por el límite derecho");
    }

    //--- Pruebas para keyPressed() [CC = 2] ---

    @Test
    void keyPressedTeclaIzda(){
        player.keyPressed(pressLeft);
        assertEquals(-2, player.getDx(), "La posición X del jugador no se ha actualizado correctamente por la izquierda");
    }


    @Test
    void keyPressedTeclaDcha(){
        player.keyPressed(pressRight);
        assertEquals(2, player.getDx(), "La posición X del jugador no se ha actualizado correctamente por la derecha");
    }

    //--- Pruebas para keyReleased() [CC = 2] ---
    @Test
    void keyReleasedTeclaIzda(){
        player.keyReleased(pressLeft);
        assertEquals(0, player.getDx(), "La posición X del jugador no se ha actualizado correctamente tras dejar de pulsar  la izquierda");
    }

    @Test
    void keyReleasedTeclaDcha(){
        player.keyReleased(pressRight);
        assertEquals(0, player.getDx(), "La posición X del jugador no se ha actualizado correctamente tras dejar de pulsar la derecha");
    }

}