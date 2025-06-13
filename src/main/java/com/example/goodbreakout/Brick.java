package com.example.goodbreakout;

import javafx.geometry.HorizontalDirection;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brick extends GraphicsItem {

    static private int gridRows;
    static private int gridCols;
    private Color color;
    private boolean hasMoneySymbol;

    public enum CrushType {
        NoCrush,
        HorizontalCrush,
        VerticalCrush;
    }

    public static void setGridRows(int gridRows) {
        Brick.gridRows = gridRows;
    }

    public static void setGridCols(int gridCols) {
        Brick.gridCols = gridCols;
    }

    public Brick(int x, int y, Color color) {
        this.width = canvasWidth / gridCols;
        this.height = canvasHeight / gridRows;
        this.x = width * x;
        this.y = height * y;
        this.color = color;
        hasMoneySymbol = false;
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(color);
        graphicsContext.fillRect(x, y, width, height);
        //graphicsContext.fillOval(money.x, money.y, money.width, money.height);
    }

    private boolean contains(Point2D point) {
        return ((point.getX() >= this.x) && (point.getX() <= (this.x + this.width))
                && (point.getY() >= this.y) && (point.getY() <= (this.y + this.height)));
    }

    public CrushType crush(Point2D upper, Point2D lower, Point2D left, Point2D right) {
        if (contains(upper) || contains(lower))
            return CrushType.VerticalCrush;
        else if (contains(left) || contains(right))
            return CrushType.HorizontalCrush;
        else
            return CrushType.NoCrush;
    }

    public boolean hasMoneySymbol()
    {
        return hasMoneySymbol;
    }

    public void setHasMoneySymbol(boolean hasMoneySymbol)
    {
        this.hasMoneySymbol = hasMoneySymbol;
    }
}