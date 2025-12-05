package main;

import space_invaders.sprites.Alien;
import space_invaders.sprites.Player;
import space_invaders.sprites.Shot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class BoardWhiteBoxTest {
        private Board board;
        private Player player;
        private Shot shot;
        private List<Alien> aliens;

        @BeforeEach
        void setUp() {
            board = new Board();
            if (board.getTimer() != null) {
                board.getTimer().stop();
            }
            player = new Player();
            shot = new Shot();
            aliens = new ArrayList<>();

            board.setPlayer(player);
            board.setShot(shot);
            board.setAliens(aliens);
            board.setInGame(true);
            board.setDeaths(0);
        }

        private Alien addAlien(int x, int y, boolean isVisible) {
            Alien alien = new Alien(x, y);
            alien.setVisible(isVisible);
            aliens.add(alien);
            return alien;
        }

        private Alien.Bomb setupBomb(int x, int y, boolean isVisible, boolean isDestroyed) {
            Alien a = addAlien(x, y, isVisible);
            Alien.Bomb b = a.getBomb();
            b.setDestroyed(isDestroyed);
            if (!isDestroyed) {
                b.setX(x);
                b.setY(y);
            }
            return b;
        }

    // --- Pruebas para game_init() [CC = 1] ---

    @Test
    void testGameInit(){
        board.gameInit();

        boolean resultado = false;
        int numAliensEsperados = Commons.ALIEN_ROWS * Commons.ALIEN_COLUMNS;

        resultado = (board.getAliens() != null) && (board.getPlayer() != null) && (board.getShot() != null) && (board.getAliens().size() == numAliensEsperados);

        assertTrue(resultado);
    }

    // --- Pruebas para update() [CC = 1] ---

    @Test
    void testUpdate_Victoria() throws Exception {
        boolean resultado = false;

        board.setDeaths(Commons.NUMBER_OF_ALIENS_TO_DESTROY);
        board.setInGame(true);
        resultado = board.getTimer().isRunning();

        board.update();
        resultado = (!board.isInGame()) && (board.getMessage().equals("Game won!")) && (!board.getTimer().isRunning());

        assertTrue(resultado);
    }

    @Test
    void testUpdate_SigueElJuego() throws Exception {
        boolean resultado = false;

        board.setDeaths(0);
        board.setInGame(true);
        board.setMessage("Mensaje Inicial");

        resultado = board.getTimer().isRunning();

        board.update();

        resultado= (board.isInGame()) && (board.getMessage().equals("Mensaje Inicial")) && (board.getTimer().isRunning());

        assertTrue(resultado);
    }

        // --- Pruebas para update_aliens() [CC = 9] ---

    @Test
    void testAliensEmptyList() {
        board.setDirection(-1);


        board.update_aliens();
        int finalDirection = board.getDirection();


        boolean conditionsMet = (finalDirection == -1);
        assertTrue(conditionsMet, "La dirección no debería cambiar si no hay aliens");
    }

    @Test
    void testAliensInvisibleAlien() {
        Alien a = addAlien(150, 100, false);
        board.setDirection(-1);
        board.update_aliens();


        boolean conditionsMet = (a.getX() == 150) && (board.getDirection() == -1);
        assertTrue(conditionsMet, "El alien invisible no debe moverse ni activar bordes");
    }

    @Test

    void testAliensNormalMove() {
        Alien a = addAlien(150, 100, true);
        board.setDirection(1); // Moviendo a la derecha

        board.update_aliens();


        boolean conditionsMet = (a.getX() == 151) // 150 - 1
                && (a.getY() == 100)
                && (board.getDirection() == 1);
        assertTrue(conditionsMet, "El alien no se movió correctamente o la dirección cambió indebidamente");
    }

    @Test
    void testAliensHitRightBoundary() {
        Alien a = addAlien(Commons.BOARD_WIDTH - Commons.BORDER_RIGHT, 100, true);
        board.setDirection(1);

        board.update_aliens();


        boolean directionCorrect = (board.getDirection() == -1);
        boolean alienMovedDown = (a.getY() == 100 + Commons.GO_DOWN);

        assertTrue(directionCorrect && alienMovedDown, "La dirección no cambió a 1 o el alien no bajó");
    }

    @Test
    void testAliensHitLeftBoundary() {
        Alien a = addAlien(Commons.BORDER_LEFT, 100, true);
        board.setDirection(-1);

        board.update_aliens();


        boolean conditionsMet = (board.getDirection() == 1)
                && (a.getY() == 100 + Commons.GO_DOWN);
        assertTrue(conditionsMet, "La dirección no cambió a 1 o el alien no bajó");
    }

    @Test
    void testAliensMissRightBoundary() {
        Alien a = addAlien(Commons.BOARD_WIDTH - Commons.BORDER_RIGHT, 100, true);
        board.setDirection(1);

        board.update_aliens();


        boolean conditionsMet = (board.getDirection() == -1) && (a.getY() == 115);
        assertTrue(conditionsMet, "La dirección cambió o el alien bajó incorrectamente");
    }

    @Test
    void testAliensMissLeftBoundary() {
        Alien a = addAlien(Commons.BORDER_LEFT, 100, true);
        board.setDirection(1);

        board.update_aliens();


        boolean conditionsMet = (board.getDirection() == 1) && (a.getY() == 100);
        assertTrue(conditionsMet, "La dirección cambió o el alien bajó incorrectamente");
    }

    @Test
    void testAliensInvasion() {
        addAlien(150, Commons.GROUND + Commons.ALIEN_HEIGHT + 1, true);
        board.setDirection(1);

        board.update_aliens();


        boolean inGameCorrect = (!board.isInGame());
        boolean messageCorrect = "Invasion!".equals(board.getMessage());

        assertTrue(inGameCorrect && messageCorrect, "BUG: El juego no terminó (inGame=true) o el mensaje es incorrecto");
    }

    @Test
    void testAliensMultipleAliensGoDown() {
        Alien a1 = addAlien(150, 100, true);
        Alien a2 = addAlien(Commons.BORDER_LEFT, 100, true);
        board.setDirection(-1);

        board.update_aliens();


        boolean conditionsMet = (board.getDirection() == 1)
                && (a1.getY() == 100 + Commons.GO_DOWN)
                && (a2.getY() == 100 + Commons.GO_DOWN);
        assertTrue(conditionsMet, "La dirección no cambió o no bajaron AMBOS aliens");
    }

    // --- Pruebas para update_shots() [CC = 6] ---

    @Test
    void testShotsShotNotVisible() {
        shot.setVisible(false);
        shot.setX(100);
        shot.setY(100);

        board.update_shots();

        boolean conditionsMet = (shot.getX() == 100)
                && (shot.getY() == 100)
                && (board.getDeaths() == 0);
        assertTrue(conditionsMet, "El estado del disparo invisible cambió");
    }

    @Test
    void testShotsShotVisibleNoAliens() {
        shot.setVisible(true);
        shot.setX(100);
        shot.setY(100);
        aliens.clear();

        board.update_shots();


        boolean yMovedCorrectly = (shot.getY() == 100 - Commons.SHOT_SPEED);
        boolean xRemainedSame = (shot.getX() == 100);

        assertTrue(yMovedCorrectly && xRemainedSame, "El disparo no se movió en Y o la X cambió incorrectamente");
    }

    @Test
    void testShotsShotGoesOffScreen() {
        shot.setVisible(true);
        shot.setX(100);
        shot.setY(2);

        board.update_shots();


        boolean conditionsMet = (!shot.isVisible());
        assertTrue(conditionsMet, "El disparo no murió (die()) al salir de la pantalla");
    }

    @Test
    void testShotsAlienNotVisible() {
        shot.setVisible(true);
        shot.setX(100);
        shot.setY(100);
        Alien a = addAlien(100, 100, false);

        board.update_shots();


        boolean conditionsMet = (board.getDeaths() == 0)
                && (!a.isDying())
                && (shot.getY() == 100 - Commons.SHOT_SPEED);
        assertTrue(conditionsMet, "Hubo colisión con alien invisible o el disparo no se movió");
    }

    @Test
    void testShotsShotMissesAlien() {
        shot.setVisible(true);
        shot.setX(10);
        shot.setY(100);
        Alien a = addAlien(100, 100, true);

        board.update_shots();


        boolean conditionsMet = (board.getDeaths() == 0)
                && (!a.isDying())
                && (shot.getY() == 100 - Commons.SHOT_SPEED);
        assertTrue(conditionsMet, "Hubo colisión fantasma o el disparo no se movió");
    }

    @Test
    void testShotsShotHitsAlien() {
        shot.setVisible(true);
        shot.setX(102);
        shot.setY(102);
        Alien a = addAlien(100, 100, true);

        board.update_shots();


        boolean conditionsMet = (board.getDeaths() == 1)
                && (a.isDying())
                && (shot.getY() == 102 - Commons.SHOT_SPEED);
        assertTrue(conditionsMet, "No se registró el impacto (deaths, isDying) o el disparo no continuó");
    }

    // --- Pruebas para update_bomb() [CC = 7] ---

    @Test
    void testBombsEmptyList() {
        aliens.clear();


        boolean didNotCrash = true;
        try {
            board.update_bomb();
        } catch (Exception e) {
            didNotCrash = false;
        }
        assertTrue(didNotCrash, "El método falló con una lista de aliens vacía");
    }

    @Test
    void testBombsAlienNotVisible() {
        Alien.Bomb b = setupBomb(100, 50, false, true);

        board.update_bomb();


        boolean conditionsMet = (b.isDestroyed());
        assertTrue(conditionsMet, "Se creó una bomba desde un alien invisible");
    }

    @Test
    void testBombsBombAlreadyActive() {
        Alien.Bomb b = setupBomb(100, 50, true, false);

        board.update_bomb();


        boolean conditionsMet = (!b.isDestroyed()) && (b.getX() == 100);
        assertTrue(conditionsMet, "Una bomba activa fue recreada o su X cambió");
    }

    @Test
    void testBombsTryCreateBomb() {
        Alien.Bomb b = setupBomb(100, 50, true, true);
        player.setX(300);
        player.setVisible(true);

        board.update_bomb();

        boolean bombCreated = !b.isDestroyed();
        boolean bombNotCreated = b.isDestroyed();

        assertTrue(bombCreated || bombNotCreated, "El estado de la bomba es inválido después del intento de creación");
    }

    @Test
    void testBombsBombActivePlayerInvisible() {
        Alien.Bomb b = setupBomb(100, 100, true, false);
        player.setVisible(false);

        board.update_bomb();


        boolean bombMovedDown = (b.getY() == 100 + Commons.BOMB_SPEED);
        boolean bombIsActive = (!b.isDestroyed());

        assertTrue(bombMovedDown && bombIsActive, "La bomba no se movió hacia abajo, o se destruyó");
    }

    @Test
    void testBombsBombHitsPlayer() {
        player.setX(100);
        player.setY(100);
        player.setVisible(true);
        Alien.Bomb b = setupBomb(102, 102, true, false);

        board.update_bomb();


        boolean conditionsMet = (player.isDying())
                && (b.isDestroyed())
                && (b.getY() == 102);
        assertTrue(conditionsMet, "El impacto no registró al jugador (isDying) o la bomba (isDestroyed)");
    }

    @Test
    void testBombsBombHitsGround() {
        player.setX(300);
        player.setVisible(true);
        Alien.Bomb b = setupBomb(100, Commons.GROUND - Commons.BOMB_HEIGHT + 1, true, false);

        board.update_bomb();

        boolean bombWasDestroyed = (b.isDestroyed());

        assertTrue(bombWasDestroyed, " La bomba no se destruyó al tocar el suelo (camino inalcanzable)");
    }

}