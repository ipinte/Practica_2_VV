package org.example;

import main.Commons;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Shot;

import static org.junit.jupiter.api.Assertions.*;

class AlienTest {

    Alien alien;

    @BeforeEach
    void setUp() {
        alien = null;
    }

    @AfterEach
    void tearDown() {
        alien = null;
    }

    // --- Pruebas para initAlien() ---

    @Test
    void initAlien_PosicionValidaDentroLimites() {
        alien = new Alien(50, 100);
        boolean resultado = (alien.getX() == 50) && (alien.getY() == 100);
        assertTrue(resultado);
    }

    @Test
    void initAlien_PosicionNegativa() {
        alien = new Alien(-10, -20);
        boolean resultado = (alien.getX() == 0) && (alien.getY() == 0);
        assertTrue(resultado);
    }

    @Test
    void initAlien_PosicionFueraLimite() {
        alien = new Alien(1000, 1000);
        boolean resultado = (alien.getX() == Commons.BOARD_WIDTH) && (alien.getY() == Commons.BOARD_HEIGHT);
        assertTrue(resultado);
    }

    @Test
    void initAlien_ImagenAsignada() {
        alien = new Alien(50, 50);
        assertNotNull(alien.getImage(), "El alien debería tener una imagen asignada tras inicializarse.");
    }

    // --- Pruebas para act() ---

    @Test
    void act_MovimientoDerecha() {
        alien = new Alien(100, 100);
        int posicionInicialX = alien.getX();

        alien.act(1);
        boolean resultado = (alien.getX() == posicionInicialX + 1) && (alien.getY() == 100);
        assertTrue(resultado);
    }

    @Test
    void act_MovimientoIzquierda() {
        alien = new Alien(100, 100);
        int posicionInicialX = alien.getX();

        alien.act(-1);
        boolean resultado = (alien.getX() == posicionInicialX - 1) && (alien.getY() == 100);
        assertTrue(resultado);
    }

    // --- Pruebas para initBomb() ---

    @Test
    void bomb_InicializacionCorrecta() {
        alien = new Alien(100, 100);
        Alien.Bomb bomb = alien.getBomb();

        boolean resultado = (bomb.getX() == alien.getX()) && (bomb.getY() >= alien.getY()) && (bomb.getImage() != null);
        assertTrue(resultado);
    }

    @Test
    void bomb_PosicionNegativa() {
        alien = new Alien(-10, -20);
        Alien.Bomb bomb = alien.getBomb();

        boolean resultado = (bomb.getX() == 0) && (bomb.getY() >= 0);
        assertTrue(resultado);
    }

    @Test
    void bomb_PosicionFueraLimite() {
        alien = new Alien(1000, 1000);
        Alien.Bomb bomb = alien.getBomb();

        boolean resultado = (bomb.getX() == Commons.BOARD_WIDTH) && (bomb.getY() <= Commons.BOARD_HEIGHT);
        assertTrue(resultado);
    }
}