package org.example;

import org.junit.jupiter.api.Test;
import space_invaders.sprites.Player;
import space_invaders.sprites.Shot;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShotTest {

    //PRUEBAS DEL MÉTODO INIT SHOT
    @Test
    void initShot_PosicionModificada() {
        Player player = new Player();
        int startX = player.getX();
        int startY = player.getY();
        Shot shot = new Shot(startX, startY);
        boolean resultado = (shot.getX() > startX) && (shot.getY() < startY) && (shot.getImage() != null);
        assertTrue(resultado);

    }
}
