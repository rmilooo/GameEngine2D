package org.GameEngine2D;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

public class GameEngine extends Canvas implements Runnable {

    private Thread thread;
    private boolean running = false;
    private final int width;
    private final int height;
    private final int fps;
    private JFrame frame;
    private final List<GameObject> gameObjects;

    public GameEngine(String title, int width, int height, int fps) {
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.gameObjects = new ArrayList<>();

        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(this);

        InputHandler inputHandler = new InputHandler();
        this.addKeyListener(inputHandler);
        this.addMouseListener(inputHandler);
        this.addMouseMotionListener(inputHandler);

        frame.setVisible(true);
    }

    public synchronized void start() {
        if (running) return;
        this.createBufferStrategy(3); // Pre-create buffer strategy
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long lastUpdate = startTime;

        while (running) {
            long now = System.currentTimeMillis();
            double delta = (now - lastUpdate) / 1000.0; // Delta time in seconds
            lastUpdate = now;

            synchronized (this) {
                tick(delta);
                render();
            }

            // Sleep to maintain the target FPS
            long frameTime = 1000 / fps;
            long sleepTime = frameTime - (System.currentTimeMillis() - now);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized void tick(double delta) {
        List<GameObject> objectsCopy;
        synchronized (gameObjects) {
            objectsCopy = new ArrayList<>(gameObjects); // Avoid ConcurrentModificationException
        }

        for (GameObject object : objectsCopy) {
            object.update(delta);
        }
    }

    private synchronized void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) return;

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;

        // Configure rendering hints once during initialization if constant
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);

        // Local copy of the game objects for rendering
        List<GameObject> objectsToRender;
        synchronized (gameObjects) {
            objectsToRender = new ArrayList<>(gameObjects);
        }

        for (GameObject object : objectsToRender) {
            if (isVisible(object)) { // Optional: Skip objects outside the viewport
                object.render(g2d);
            }
        }

        g2d.dispose();
        bs.show();
    }

    // Visibility check for objects (optional optimization)
    private boolean isVisible(GameObject object) {
        Rectangle bounds = object.getBounds(); // Assume objects define bounds
        return bounds.intersects(0, 0, width, height);
    }

    public synchronized void addGameObject(GameObject object) {
        synchronized (gameObjects) {
            gameObjects.add(object);
        }
    }

    public static void main(String[] args) {
        GameEngine engine = new GameEngine("Customizable 2D Game Engine", 800, 600, 60);
        engine.start();
        engine.addGameObject(new Cube(2, 2, 20, 20));
    }
}
