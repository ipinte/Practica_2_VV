package org.example;

import main.Board;
import main.Commons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Player;
import space_invaders.sprites.Shot;
import space_invaders.sprites.Sprite;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


class BoardTest {
    private Board board;
    private Player player;
    private List<Alien> aliens;
    private Shot shot;

    private Method getAccessibleMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method;
    }

    private Field getAccessibleField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    private void invokePrivateMethod(String methodName) throws Exception {
        try {
            getAccessibleMethod(Board.class, methodName).invoke(board);
        } catch (InvocationTargetException e) {
            throw (Exception) e.getTargetException();
        }
    }

    private void setDirection(int direction) throws Exception {
        getAccessibleField(Board.class, "direction").set(board, direction);
    }

    private int getDirection() throws Exception {
        return (int) getAccessibleField(Board.class, "direction").get(board);
    }

    private String getMessage() throws Exception {
        return (String) getAccessibleField(Board.class, "message").get(board);
    }

    private boolean isInGame() throws Exception {
        return (boolean) getAccessibleField(Board.class, "inGame").get(board);
    }

    private int getDeaths() throws Exception {
        return (int) getAccessibleField(Board.class, "deaths").get(board);
    }

    private void setSpriteVisible(Sprite sprite, boolean visible) throws Exception {
        Method setVisible = getAccessibleMethod(Sprite.class, "setVisible", boolean.class);
        setVisible.invoke(sprite, visible);
    }

    @BeforeEach
    void setUp() {
        board = new Board();
        player = board.getPlayer();
        aliens = board.getAliens();
        shot = board.getShot();
    }

    // --- Pruebas para update_aliens() ---

    @Test
    void testAliensMoveWithDirection() throws Exception {

        Alien alien = aliens.getFirst();
        int initialX = alien.getX();

        invokePrivateMethod("update_aliens");

        assertEquals(initialX + 1, alien.getX());
    }

    @Test
    void testAliensChangeDirectionAtLeftBorder() throws Exception {
        board.setDirection(-1);
        Alien alien = aliens.getFirst();
        alien.setX(Commons.BORDER_LEFT);
        int initialY = alien.getY();

        invokePrivateMethod("update_aliens");

        assertTrue(getDirection() == 1 && alien.getY() == (initialY + Commons.GO_DOWN), "La dirección debe ser 1 y el alien debe bajar");
    }

    @Test
    void testAliensAtRightBorder() throws Exception {
        board.setDirection(1);
        Alien alien = aliens.getFirst();
        alien.setX(Commons.BOARD_WIDTH - Commons.BORDER_RIGHT);
        int initialY = alien.getY();

        invokePrivateMethod("update_aliens");


        assertTrue(getDirection() == -1 && alien.getY() == (initialY + Commons.GO_DOWN), "La dirección debe ser - 1 y el alien debe bajar");
    }

    @Test
    void testAliensTriggerInvasion() throws Exception {
        Alien alien = aliens.getFirst();
        setSpriteVisible(alien, true);
        alien.setY(Commons.GROUND + Commons.ALIEN_HEIGHT + 1);

        invokePrivateMethod("update_aliens");

        assertTrue(board.getMessage().equals("Invasion!") && !board.isInGame(), "El mensaje debe ser 'Invasion!' y el juego debe terminar");
    }

    // --- Pruebas para update_shots() ---

    @Test
    void testShotMovesUp() throws Exception {
        setSpriteVisible(shot, true);
        int initialY = 100;
        int initialX = 50;
        shot.setY(initialY);
        shot.setX(initialX);

        invokePrivateMethod("update_shots");

        int expectedY = initialY - Commons.SHOT_SPEED;

        assertTrue(shot.getY() == expectedY && shot.getX() == initialX, "El disparo debe moverse verticalmente (Y cambia, X no)");
    }

    @Test
    void testShotDiesAtTopBorder() throws Exception {
        setSpriteVisible(shot, true);
        shot.setY(1);

        invokePrivateMethod("update_shots");

        assertFalse(shot.isVisible(), "El disparo debería eliminarse al salir de la pantalla");
    }

    @Test
    void testShotHitsAlien() throws Exception {
        setSpriteVisible(shot, true);
        Alien alien = aliens.getFirst();
        setSpriteVisible(alien, true);
        alien.setDying(false);
        int initialDeaths = getDeaths();

        shot.setX(alien.getX());
        shot.setY(alien.getY());

        invokePrivateMethod("update_shots");

        assertTrue(alien.isDying() && getDeaths() == (initialDeaths + 1), "El alien debe morir y las muertes deben incrementar");
    }

    // --- Pruebas para update_bomb() ---

    @Test
    void testBombMoves() throws Exception {
        Alien.Bomb bomb = aliens.getFirst().getBomb();
        bomb.setDestroyed(false);
        int initialY = 100;
        bomb.setY(initialY);

        invokePrivateMethod("update_bomb");

        assertEquals(initialY + Commons.BOMB_SPEED, bomb.getY(), "La bomba debe moverse (hacia abajo)");
    }

    @Test
    void testActiveBombIsNotRecreated() throws Exception {
        Alien alien = aliens.getFirst();
        setSpriteVisible(alien, true);
        Alien.Bomb bomb = alien.getBomb();

        bomb.setDestroyed(false);
        bomb.setX(50);
        bomb.setY(50);

        int expectedY = 50 + Commons.BOMB_SPEED;

        invokePrivateMethod("update_bomb");

        assertTrue(bomb.getX() != alien.getX() && bomb.getX() == 50 && bomb.getY() == expectedY, "La bomba activa no debe resetearse y debe moverse");
    }

    @Test
    void testBombHitsPlayer() throws Exception {
        setSpriteVisible(player, true);
        player.setDying(false);

        Alien.Bomb bomb = aliens.getFirst().getBomb();
        bomb.setDestroyed(false);

        bomb.setX(player.getX());
        bomb.setY(player.getY());

        invokePrivateMethod("update_bomb");

        assertTrue(player.isDying() && bomb.isDestroyed(),
                "El jugador debe morir y la bomba debe destruirse");
    }

    @Test
    void testBombDestroysAtGround() throws Exception {
        Alien.Bomb bomb = aliens.getFirst().getBomb();
        bomb.setDestroyed(false);

        int ground = Commons.GROUND;
        int bombHeight = Commons.BOMB_HEIGHT;

        int triggerY = (ground - bombHeight) - 1;
        bomb.setY(triggerY);

        invokePrivateMethod("update_bomb");

        assertTrue(bomb.isDestroyed(), "La bomba debería destruirse al alcanzar la altura del suelo");
    }

    // --- Pruebas para gameinit() ---

    private void invokegameInitMethod() throws Exception {
        Method gameInitMethod = Board.class.getDeclaredMethod("gameInit");
        gameInitMethod.setAccessible(true);
        try {
            gameInitMethod.invoke(board);
        } catch (InvocationTargetException e) {
            throw (Exception) e.getTargetException();

        }
    }

    @Test
    void crearNumeroCorrectoDeAliens() throws Exception{
        boolean resultado;

        invokegameInitMethod();

        resultado = (board.getAliens().size() == Commons.ALIEN_ROWS * Commons.ALIEN_COLUMNS);

        assertTrue(resultado, "El número de aliens creados no es correcto.");
    }

    @Test
    void primerAlienPosicionadoCorrectamente() throws Exception{
        boolean resultado;

        invokegameInitMethod();

        resultado = (board.getAliens() != null)
                && (board.getAliens().getFirst().getX() == Commons.ALIEN_INIT_X)
                && (board.getAliens().getFirst().getY() == Commons.ALIEN_INIT_Y);

        assertTrue(resultado, "La posición del primer alien es incorrecta.");
    }

    @Test
    void jugadorCreadoCorrectamente() throws Exception{
        boolean resultado;

        invokegameInitMethod();

        resultado = (board.getPlayer() != null) && (board.getPlayer().isVisible());

        assertTrue(resultado, "El jugador no se ha creado correctamente.");
    }

    @Test
    void disparoInicializadoCorrectamente() throws Exception{
        boolean resultado;

        invokegameInitMethod();

        resultado = (board.getShot() != null) && (!board.getShot().isVisible());

        assertTrue(resultado, "El disparo no se ha inicializado correctamente");
    }

    // --- Pruebas para update() ---

    private void invokeUpdateMethod() throws Exception{
        Method updateMethod = Board.class.getDeclaredMethod("update");
        updateMethod.setAccessible(true);
        try{
            updateMethod.invoke(board);
        }catch(InvocationTargetException e){
            throw (Exception) e.getTargetException();
        }
    }

    @Test
    void testUpdate_PartidaEnMarcha() throws Exception{
        boolean resultado;

        board.setDeaths(0);
        String mensajeInicial = "Juego en marcha";
        board.setMessage(mensajeInicial);
        board.setInGame(true);

        invokeUpdateMethod();

        resultado = (board.getMessage().equals(mensajeInicial)) && (board.isInGame());

        assertTrue(resultado, "Las acciones de que el juego continue no se han realizado correctamente.");
    }

    @Test
    void testUpdate_PartidaGanada() throws Exception {
        boolean resultado;

        board .setDeaths(Commons.NUMBER_OF_ALIENS_TO_DESTROY);
        board.setInGame(true);

        invokeUpdateMethod();

        resultado = (board.getMessage().equals("Game won!")) && (!board.isInGame()) && (!board.getTimer().isRunning());

        assertTrue(resultado, "Las acciones tras conseguir la victoria no se han realizado correctamente.");
    }
}