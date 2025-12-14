package main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space_invaders.sprites.Alien;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class BoardIntegrationTest {

    private static final Logger logger = Logger.getLogger(BoardIntegrationTest.class.getName());
    private Board board;

    @Mock
    private space_invaders.sprites.Alien alienMock;

    @Mock
    private Alien alienMock2;

    @Mock
    private Alien.Bomb bombMock;

    @Mock
    private space_invaders.sprites.Shot shotMock;

    @Mock
    private space_invaders.sprites.Player playerMock;

    @BeforeEach
    void setUp() {

        logger.info("-------------------------------------");
        logger.info("Objetos necesarios para los tests de integración de Board");

        board = new Board();

        lenient().when(alienMock.getBomb()).thenReturn(bombMock);
        lenient().when(alienMock2.getBomb()).thenReturn(bombMock);

        List<Alien> alienList = new ArrayList<>();
        alienList.add(alienMock);
        alienList.add(alienMock2);
        board.setAliens(alienList);

        board.setPlayer(playerMock);

        board.setShot(shotMock);
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
}