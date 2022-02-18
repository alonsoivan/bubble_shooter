package com.ivn.bs.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ivn.bs.screens.GameScreen;

public class Ball extends Sprite {

    public static enum Color
    {
        RED, GREEN, BLUE
    }

    private Color color;
    private Rectangle rect;
    private int tam;


    public Ball(Color color, Vector2 position, int tam){
        super(new Texture("badlogic.jpg"));
        if(color == Color.BLUE)
            super.setTexture(GameScreen.textureBlue);
        else if(color == Color.GREEN)
            super.setTexture(GameScreen.textureGreen);
        else
            super.setTexture(GameScreen.textureRed);

        this.color = color;

        super.setPosition(position.x,position.y);
        super.setSize(tam,tam);
        this.tam = tam;

        rect = new Rectangle(position.x,position.y, tam, tam);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        rect.setPosition(getX(),getY());
    }

    public Rectangle getRect(){
        return this.rect;
    }

    public Vector2 getPos(){
        return new Vector2(rect.x,rect.y);
    }

    public int getTam(){
        return tam;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        rect.setPosition(getX(),getY());
    }

    public Color getBallColor(){
        return color;
    }
}
