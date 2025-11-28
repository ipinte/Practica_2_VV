package space_invaders.sprites;

import main.Commons;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlienWhiteBoxTest {
    Alien alien;

    @BeforeEach
    void setUp() {
        alien = null;
    }

    @AfterEach
    void tearDown() {
        alien = null;
    }

    // --- Pruebas para initAlien() [CC = 5] ---

    @Test
    void testInitAlien_XMayorQueMaximo() {
        alien = new Alien(Commons.BOARD_WIDTH + 10, 50);
        boolean ok = alien.getX() == Commons.BOARD_WIDTH && alien.getY() == 50;
        assertTrue(ok);
    }

    @Test
    void testInitAlien_XNegativo() {
        alien = new Alien(-15, 55);
        boolean ok = alien.getX() == 0 && alien.getY() == 55;
        assertTrue(ok);
    }

    @Test
    void testInitAlien_YMayorQueMaximo() {
        alien = new Alien(40, Commons.BOARD_HEIGHT + 20);
        boolean ok = alien.getX() == 40 && alien.getY() == Commons.BOARD_HEIGHT;
        assertTrue(ok);
    }

    @Test
    void testInitAlien_YNegativo() {
        alien = new Alien(38, -25);
        boolean ok = alien.getX() == 38 && alien.getY() == 0;
        assertTrue(ok);
    }

    @Test
    void testInitAlien_ValoresValidos() {
        alien = new Alien(77, 44);
        boolean ok = alien.getX() == 77 && alien.getY() == 44;
        assertTrue(ok);
    }

    // --- Pruebas para act() [CC = 1] ---

    @Test
    void act_UnicoCamino() {
        alien = new Alien(100, 100);
        int xInicial = alien.getX();
        alien.act(10);
        boolean ok = alien.getX() == (xInicial - 10);
        assertTrue(ok);
    }

    // --- Pruebas para initBomb() [CC = 2] ---

    @Test
    void testInitBomb_CondicionTrue() {
        Alien.Bomb bomb = new Alien(25, 35).getBomb();
        boolean ok = bomb.getX() == 25 && bomb.getY() == 35;
        assertTrue(ok);
    }

    @Test
    void testInitBomb_CondicionFalse() {
        Alien.Bomb bomb = new Alien(Commons.BOARD_WIDTH + 11, Commons.BOARD_HEIGHT + 11).getBomb();
        boolean ok = bomb.getX() == Commons.BOARD_WIDTH && bomb.getY() == Commons.BOARD_HEIGHT;
        assertTrue(ok);
    }
}
