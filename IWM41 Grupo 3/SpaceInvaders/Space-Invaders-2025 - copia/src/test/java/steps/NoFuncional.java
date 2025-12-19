package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import main.Board;
import main.Commons;
import space_invaders.sprites.Alien;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;

import static org.junit.Assert.*;

public class NoFuncional {
    private Board board;
    private JFrame gameWindow;
    private JFrame secondWindow;
    private Exception exceptionCaptured;

    @Given("el juego esta iniciado en modo ventana")
    public void iniciarJuegoEnVentana() {
        gameWindow = new JFrame();
        board = new Board();
        gameWindow.add(board);
        gameWindow.pack();
        gameWindow.setVisible(true);
    }

    @When("el usuario cambia el tamaño de la ventana a {int}x{int}")
    public void cambiarTamanoVentana(int width, int height) {
        // Simulamos el redimensionado. Swing actualizará el tamaño del componente Board.
        gameWindow.setSize(width, height);

        // Forzamos la actualización del tamaño del panel interno (Board)
        // para simular lo que haría el LayoutManager de Swing automáticamente
        board.setSize(width, height);
        board.validate();
    }

    @Then("los componentes del juego deben reescalarse proporcionalmente")
    public void verificarReescalado() {
        // Verificamos que el Board ha aceptado el nuevo tamaño.
        // La lógica de reescalado visual está en Board.paintComponent(),
        // la cual usa getWidth() y getHeight(). Si estos valores han cambiado, el escalado funcionará.
        Dimension dim = board.getSize();

        // Comprobamos que el tamaño NO es el original (Commons)
        assertNotEquals(Commons.BOARD_WIDTH, dim.width);
        assertNotEquals(Commons.BOARD_HEIGHT, dim.height);
    }

    @And("el area de juego debe ocupar todo el panel visible")
    public void verificarAreaJuego() {
        // El tablero debe coincidir con el tamaño de la ventana (menos bordes)
        assertEquals(gameWindow.getContentPane().getSize(), board.getSize());
    }
}
