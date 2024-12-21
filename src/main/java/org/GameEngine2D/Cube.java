package org.GameEngine2D;

import java.awt.*;

public class Cube extends GameObject {

    private static final int MOVE_SPEED = 200; // Movement speed in pixels per second
    private static final int SCREEN_WIDTH = 800; // Screen width for boundary wrapping

    public Cube(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update(double delta) {
        x += (int) (MOVE_SPEED * delta); // Move right at configured speed
        if (x > SCREEN_WIDTH) {
            x = -width;
            y += height;
        }
    }

    @Override
    public synchronized void render(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setColor(Color.BLUE);
        g2d.fillRect(x, y, width, height);
    }
}
