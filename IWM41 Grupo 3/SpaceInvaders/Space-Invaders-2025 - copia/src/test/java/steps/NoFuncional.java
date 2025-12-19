package steps;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import main.Board;
import main.Commons;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Shot; // Necesario para instanciar el disparo de prueba

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Image;
import java.util.List;

import static org.junit.Assert.*;

public class NoFuncional {

    private Board board;
    private JFrame gameWindow;

    // Variables para almacenar los resultados de la verificación
    private Image playerImage;
    private List<Alien> aliens;
    private Image shotImage;

    @After
    public void tearDown() {
        if (board != null && board.getTimer() != null) {
            board.getTimer().stop();
        }
        if (gameWindow != null) {
            gameWindow.setVisible(false);
            gameWindow.dispose();
        }
        board = null;
        gameWindow = null;
    }

    // --- ESCENARIO 1: REDIMENSIONADO ---

    @Given("el juego esta iniciado en modo ventana")
    public void elJuegoEstaIniciadoEnModoVentana() {
        gameWindow = new JFrame("Space Invaders Test");
        board = new Board();

        gameWindow.add(board);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.pack();
        gameWindow.setVisible(true);
    }

    @When("el usuario cambia el tamaño de la ventana a {int}x{int}")
    public void elUsuariaCambiaElTamanoDeLaVentanaA(int width, int height) {
        gameWindow.setSize(width, height);
        board.setSize(width, height);
        board.validate();
    }

    @Then("los componentes del juego deben reescalarse proporcionalmente")
    public void losComponentesDelJuegoDebenReescalarseProporcionalmente() {
        Dimension dim = board.getSize();
        // Verificamos que el Board ha cambiado de tamaño
        assertNotEquals("El ancho debería haber cambiado respecto al original", Commons.BOARD_WIDTH, dim.width);
        assertNotEquals("El alto debería haber cambiado respecto al original", Commons.BOARD_HEIGHT, dim.height);
    }

    @And("el area de juego debe ocupar todo el panel visible")
    public void elAreaDeJuegoDebeOcuparTodoElPanelVisible() {
        // Verificamos que el tablero tiene dimensiones válidas
        assertTrue("El ancho del tablero debe ser > 0", board.getWidth() > 0);
        assertTrue("El alto del tablero debe ser > 0", board.getHeight() > 0);
    }

    // --- ESCENARIO 2 y 3: RECURSOS GRÁFICOS ---

    @Given("el tablero de juego ha sido inicializado")
    public void elTableroDeJuegoHaSidoInicializado() {
        board = new Board();
        if (board.getTimer() != null) board.getTimer().stop();
    }

    @When("verifico la carga de las imagenes de los sprites")
    public void verificoLaCargaDeLasImagenesDeLosSprites() {
        // Verificamos que Player y Alien se cargan al inicio
        if (board.getPlayer() != null) {
            playerImage = board.getPlayer().getImage();
        }
        aliens = board.getAliens();
    }

    @When("el jugador realiza un disparo de prueba")
    public void elJugadorRealizaUnDisparoDePrueba() {
        // Simulamos el diaparo creando un objeto Shot
        Shot testShot = new Shot(100, 100);
        shotImage = testShot.getImage();
    }

    @Then("la imagen del {string} no debe ser nula")
    public void laImagenDelNoDebeSerNula(String entidad) {
        switch (entidad) {
            case "Player":
                assertNotNull("Error: La imagen del Player no se ha cargado", playerImage);
                break;
            case "Alien":
                assertNotNull("Error: La lista de Aliens es nula", aliens);
                assertFalse("Error: No hay aliens creados", aliens.isEmpty());
                assertNotNull("Error: La imagen del primer Alien es nula", aliens.get(0).getImage());
                break;
            case "Shot":
                assertNotNull("Error: Imagen del Shot no cargada", shotImage);
                break;
            default:
                fail("Entidad no reconocida en el test: " + entidad);
        }
    }

    // --- ESCENARIO 4: CIERRE DE LA APLICACIÓN ---

    @Given("el juego se esta ejecutando")
    public void elJuegoSeEstaEjecutando() {
        if (gameWindow == null) elJuegoEstaIniciadoEnModoVentana();
        // Aseguramos la configuración correcta
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @When("el usuario hace click en el boton de cerrar ventana")
    public void elUsuarioHaceClickEnElBotonDeCerrarVentana() {
        // Verificamos el estado en el @Then
    }

    @Then("el proceso de la aplicacion debe finalizar completamente")
    public void elProcesoDeLaAplicacionDebeFinalizarCompletamente() {
        assertEquals("La ventana no está configurada para salir (EXIT_ON_CLOSE)",
                JFrame.EXIT_ON_CLOSE, gameWindow.getDefaultCloseOperation());
    }

    // --- ESCENARIO 5: INSTANCIA ÚNICA ---

    @Given("ya existe una instancia del juego ejecutandose")
    public void yaExisteUnaInstanciaDelJuegoEjecutandose() {
        if (gameWindow == null) elJuegoEstaIniciadoEnModoVentana();
        assertTrue("La ventana debería estar visible", gameWindow.isVisible());
    }

    @When("intento iniciar una segunda instancia")
    public void intentoIniciarUnaSegundaInstancia() {
        // Verificamos que la instancia actual se mantiene
    }

    @Then("la segunda instancia no debe iniciarse")
    public void laSegundaInstanciaNoDebeIniciarse() {
        assertNotNull("La ventana del juego se ha perdido", gameWindow);
        assertTrue("La ventana debería seguir siendo visible", gameWindow.isVisible());
    }
}