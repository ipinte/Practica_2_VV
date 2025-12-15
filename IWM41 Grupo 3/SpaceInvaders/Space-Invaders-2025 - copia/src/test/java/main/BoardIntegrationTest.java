package main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Player;
import space_invaders.sprites.Shot;

import main.Commons; //Nuevo

import javax.swing.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardIntegrationTest {

    private static final Logger logger = Logger.getLogger(BoardIntegrationTest.class.getName());
    private Board board;

    @Mock
    private space_invaders.sprites.Alien alienMock;

    @Mock
    private space_invaders.sprites.Alien alienMock2;

    @Mock
    private Alien.Bomb bombMock;

    @Mock
    private space_invaders.sprites.Shot shotMock;

    @Mock
    private space_invaders.sprites.Player playerMock;

    @Mock
    private Timer timerMock;

    @BeforeEach
    void setUp() {

        logger.info("-------------------------------------");
        logger.info("Objetos necesarios para los tests de integración de Board");

        board = new Board();

        lenient().when(alienMock.getBomb()).thenReturn(bombMock);
        lenient().when(alienMock2.getBomb()).thenReturn(bombMock);

        lenient().when(alienMock.isVisible()).thenReturn(true);
        lenient().when(alienMock2.isVisible()).thenReturn(true);

        List<Alien> alienList = new ArrayList<>();
        alienList.add(alienMock);
        alienList.add(alienMock2);
        board.setAliens(alienList);

        board.setPlayer(playerMock);
        board.setShot(shotMock);

        board.setTimer(timerMock);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testUpdateAliens_aliensBajan() {
        logger.info("TEST: update_aliens Caso en el que un alien toca el borde derecho y todos tienen que bajar");

        when(alienMock.getX()).thenReturn(Commons.BOARD_WIDTH - Commons.BORDER_RIGHT + 10);
        when(alienMock.isVisible()).thenReturn(true);

        when(alienMock2.getX()).thenReturn(10);
        when(alienMock2.isVisible()).thenReturn(true);

        logger.info("Ejecutando board.update_aliens()");
        board.update_aliens();


        verify(alienMock).setY(anyInt());
        verify(alienMock2).setY(anyInt());

        logger.info("OK: Se verificó que alienMock y alienMock2 bajaron.");

        verify(alienMock, atLeastOnce()).act(anyInt());
        verify(alienMock2, atLeastOnce()).act(anyInt());
    }

    @Test
    public void testUpdateAliens_CasoBordeIzquierdo(){
        logger.info("TEST: update_aliens Caso en el que un alien toca el borde izquierdo y todos tienen que bajar");

        board.setDirection(-1);

        logger.info("Dirección forzada a -1.");

        when(alienMock.getX()).thenReturn(0);
        when(alienMock.isVisible()).thenReturn(true);

        when(alienMock2.getX()).thenReturn(50);
        when(alienMock2.isVisible()).thenReturn(true);

        logger.info("Ejecutando board.update_aliens()");
        board.update_aliens();

        verify(alienMock, atLeastOnce()).setY(anyInt());
        verify(alienMock2, atLeastOnce()).setY(anyInt());

        logger.info("OK: Los aliens bajaron al tocar la pared izquierda.");
    }

    @Test
    public void testUpdateAliens_CasoNormal() {
        logger.info("TEST: Probar que se mueven de forma normal");

        when(alienMock.getX()).thenReturn(100);
        when(alienMock.isVisible()).thenReturn(true);
        when(alienMock2.getX()).thenReturn(150);
        when(alienMock2.isVisible()).thenReturn(true);

        logger.info(" Ejecutando board.update_aliens()");
        board.update_aliens();


        verify(alienMock, never()).setY(anyInt());
        verify(alienMock2, never()).setY(anyInt());

        logger.info("OK: Ningún alien bajó de fila.");
    }

    //update()

    @Test
    public void testUpdate_MMPath1_GameWon() {
        logger.info("TEST: update() - Condición de Victoria");

        board.setDeaths(Commons.NUMBER_OF_ALIENS_TO_DESTROY);
        board.setInGame(true);
        board.update();

        verify(timerMock).stop();

        assertEquals("Game won!", board.getMessage());
        assertFalse(board.isInGame());
    }

    @Test
    public void testUpdate_MMPath2_NormalLoop() {
        logger.info("TEST: update() - Ciclo Normal");

        board.setDeaths(0);
        board.setInGame(true);

        lenient().when(timerMock.isRunning()).thenReturn(true);

        board.update();

        verify(playerMock, times(1)).act();
        verify(timerMock, never()).start();
    }

    @Test
    public void testUpdate_MMPath3_TimerRestart() {
        logger.info("TEST: update() - Reinicio del Timer");

        board.setInGame(true);
        board.setDeaths(0);

        when(timerMock.isRunning()).thenReturn(false);

        board.update();

        verify(timerMock).start();
        verify(playerMock).act();
    }


    @Test
    public void testGameInit() {
        logger.info("TEST: gameInit() - Verificación de inicialización real de objetos (Top-Down con objetos reales)");

        // 1. Ejecuto el método a probar.
        // Aqui gameInit() hace internamente new Player(), new Alien(), etc
        // Esto sobrescribe los Mocks inyectados en setUp() con objetos reales.
        board.gameInit();

        // 2. Verificamos el estado

        // Verificamos que la lista de Aliens se ha creado y llenado correctamente
        assertNotNull(board.getAliens(), "La lista de aliens no debe ser null");
        assertEquals(Commons.NUMBER_OF_ALIENS_TO_DESTROY, board.getAliens().size(),
                "El número de aliens creados debe ser " + Commons.NUMBER_OF_ALIENS_TO_DESTROY);

        // Verificamos que el Player se ha reiniciado (es un objeto nuevo y real, no null)
        assertNotNull(board.getPlayer(), "El objeto Player debe haber sido creado");

        // Verificamos que el Shot se ha reiniciado
        assertNotNull(board.getShot(), "El objeto Shot debe haber sido creado");

        // Verificamos que el juego no se considera 'ganado' ni 'perdido' al inicio
        assertTrue(board.isInGame(), "El juego debe estar en estado 'inGame = true'");

        logger.info("OK: gameInit inicializó correctamente la estructura de objetos.");
    }


}