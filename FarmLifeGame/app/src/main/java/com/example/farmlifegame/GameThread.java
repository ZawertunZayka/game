package com.example.farmlifegame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    private static final int TARGET_FPS = 60;
    private static final long OPTIMAL_TIME = 1000 / TARGET_FPS;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        this.running = false;
    }

    public void setRunning(boolean isRunning) {
        this.running = isRunning;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        long lastLoopTime = System.nanoTime();
        final long NANOS_PER_SECOND = 1000000000;

        while (running) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)NANOS_PER_SECOND);

            // Update game state
            this.gameView.update();

            // Render the game
            Canvas canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    if (canvas != null) {
                        this.gameView.draw(canvas);
                    }
                }
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // Sleep to maintain target FPS
            try {
                long gameTime = (System.nanoTime() - lastLoopTime) / 1000000;
                long sleepTime = OPTIMAL_TIME - gameTime;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                // Thread interrupted, likely shutting down
                running = false;
            }
        }
    }
}

