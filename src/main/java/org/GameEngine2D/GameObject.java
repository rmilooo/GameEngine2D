package org.GameEngine2D;

import java.awt.*;

public abstract class GameObject {
    protected int x, y;wd
    protected int width, height;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update(double delta); // Accept delta time
    public abstract void render(Graphics2D g2d);

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}