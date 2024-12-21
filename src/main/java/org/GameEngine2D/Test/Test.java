package org.GameEngine2D.Test;

import org.GameEngine2D.GameEngine;

public class Test {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine("Customizable 2D Game Engine", 800, 600, 60);
        engine.start();
        engine.addGameObject(new Cube(2, 2, 20, 20));
    }
}
