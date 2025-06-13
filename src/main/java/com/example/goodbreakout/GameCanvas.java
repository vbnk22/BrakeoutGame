package com.example.goodbreakout;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GameCanvas extends Canvas {
    private GraphicsContext graphicsContext;
    private GraphicsContext graphicsContextMoney;
    private Paddle paddle;
    private Ball ball;
    private boolean gameRunning = false;
    private List<Brick> bricks = new ArrayList<>();

    public void loadLevel(){
        int cols = 10;
        int rows = 10;

        Brick.setGridCols(cols);
        Brick.setGridRows(rows);

        Color [] colors = new Color[]{Color.RED,Color.WHITE,Color.AQUAMARINE,Color.ALICEBLUE,Color.AZURE,Color.BEIGE,Color.BLUE};

        for(int i = 2;i < 7;++i)
        {
            for(int j = 0;j < cols;++j)
            {
                int index = (i * j) % colors.length;
                Brick brick = new Brick(j, i, colors[index]);
                bricks.add(brick);
            }
        }
    }

    private AnimationTimer animationTimer = new AnimationTimer() {
        private long lastUpdate;
        @Override
        public void handle(long now) {
            double diff = (now - lastUpdate)/1_000_000_000.;
            lastUpdate = now;
            ball.updatePosition(diff);
            if (shouldBallBounceHorizontally()) ball.bounceHorizontally();
            if (shouldBallBounceVertically()) ball.bounceVertically();
            if (shouldBallBounceFromPaddle()) ball.bounceVertically();
            for (Brick brick : bricks)
            {
                Point2D[] borderpoints = ball.borderPoints();
                Brick.CrushType type = brick.crush(borderpoints[2], borderpoints[3], borderpoints[0], borderpoints[1]);
                if (type == Brick.CrushType.HorizontalCrush) {
                    ball.bounceHorizontally();
                    bricks.remove(brick);
                    break;
                }
                else if (type == Brick.CrushType.VerticalCrush) {
                    ball.bounceVertically();
                    bricks.remove(brick);
                    break;
                }
            }
            if (bricks.isEmpty())
            {
                System.out.println("Wygrales gre!");
                animationTimer.stop();
                Platform.exit();
            }
            draw();
        }

        @Override
        public void start() {
            super.start();
            lastUpdate = System.nanoTime();
        }
    };

    public GameCanvas() {
        super(640, 700);

        this.setOnMouseMoved(mouseEvent -> {
            paddle.setPosition(mouseEvent.getX());
            if(!gameRunning)
                ball.setPosition(new Point2D(mouseEvent.getX(), paddle.getY() - ball.getWidth() / 2));
            draw();
        });

        this.setOnMouseClicked(mouseEvent -> {
            gameRunning = true;
            graphicsContext = this.getGraphicsContext2D();
            animationTimer.start();
        });
    }

    public void initialize() {
        graphicsContext = this.getGraphicsContext2D();
        GraphicsItem.setCanvasSize(getWidth(), getHeight());
        paddle = new Paddle();
        ball = new Ball();
        this.loadLevel();
    }

    public void draw() {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, getWidth(), getHeight());

        paddle.draw(graphicsContext);
        ball.draw(graphicsContext);

        for(int i = 0; i < bricks.size(); ++i)
        {
            for (Brick brick : bricks) {

                brick.draw(graphicsContext);
            }
        }
    }

    public boolean shouldBallBounceHorizontally(){
        return ((ball.getLastPosition().getX() > 0 && ball.getX() < 0)
                || (ball.getLastPosition().getX()+ball.getWidth()/2 < this.getWidth()-1 && ball.getX()+ball.getWidth()/2 > this.getWidth()-1));
    }

    public boolean shouldBallBounceVertically(){
        return (ball.getLastPosition().getY() >= 0 && ball.getY() < 0);
    }

    public boolean shouldBallBounceFromPaddle(){
        return (ball.getLastPosition().getY() < paddle.getY() - paddle.getHeight()/2 && ball.getY() >= paddle.getY() - paddle.getHeight()/2 && ball.getX() >= paddle.getX() && ball.getX() <= paddle.getX() + paddle.getWidth());
    }
}