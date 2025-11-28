package space_invaders.sprites;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShotWhiteboxTest {

    Shot shot;

    @BeforeEach
    void setUp() {
        shot = new Shot();
    }

    @AfterEach
    void tearDown() {
        shot = null;
    }

    //Shot.initShot() tiene V(g) = 1, por lo que tiene un test asociado

    @Test
    void initShot_PosicionModificadaYImagenNoNula() {
        int x = 100;
        int y = 100;
        shot = new Shot(x, y);
        assertEquals(x + 6, shot.getX());
        assertEquals(y - 1, shot.getY());
        assertNotNull(shot.getImage(), "La imagen debe estar asignada");
    }
}
