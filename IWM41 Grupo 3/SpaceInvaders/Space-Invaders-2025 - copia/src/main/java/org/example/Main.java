//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example;

import main.Board;

import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JFrame;

public class Main extends JFrame {
    public Main() {
        this.initUI();
    }

    private void initUI() {
        this.add(new Board());
        this.setTitle("Space Invaders");
        this.setSize(358, 350);
        this.setDefaultCloseOperation(3);
        this.setResizable(false);
        this.setLocationRelativeTo((Component)null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Main ex = new Main();
            ex.setVisible(true);
        });
    }
}
