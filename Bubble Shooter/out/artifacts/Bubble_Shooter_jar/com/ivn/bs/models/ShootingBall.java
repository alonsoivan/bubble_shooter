package com.ivn.bs.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ShootingBall extends Ball{
    private boolean isShot = false;
    private int tam = 0;

    private Sprite trazado;

    private Vector2 direction;
    private boolean gotRotation = false;

    public ShootingBall(Color color, Vector2 position,int tam) {
        super(color, position,tam);
        this.tam = tam;
        trazado = new Sprite(new Texture("trazado.png"));
        trazado.setSize(trazado.getWidth()/4,trazado.getHeight()/4);
        trazado.setOrigin(trazado.getWidth()/2,0);
        trazado.setPosition(position.x + tam/2 - trazado.getWidth()/2,position.y + tam*1.1f);

    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

        if (!isShot)
            if(gotRotation)
                trazado.draw(batch);
    }

    public void setRotationTrazado(float rotation){
        gotRotation = true;
        rotation = rotation - 90;
        trazado.setRotation(rotation);
    }

    // TODO deltatime
    public void shot(Vector2 target){
        target = new Vector2(target.x, Gdx.graphics.getHeight() - target.y );
        direction =target.cpy().sub(new Vector2(super.getX()+ tam/2,super.getY() + tam/2));
        direction.nor();

        direction.scl(20);
        isShot = true;
    }

    public void move(){
        if(isShot){
            System.out.println(direction);
            Vector2 pos = new Vector2(super.getX(),super.getY()).add(direction);
            super.setPosition(pos.x,pos.y);
        }
    }

    public void rebotar(){
        if(direction.x <=0)
            direction.x = Math.abs(direction.x);
        else
            direction.x = 0 - direction.x;

    }

    public Vector2 getTrazadoCenterPos(){
        return new Vector2(trazado.getX() + (tam/5)/2 , trazado.getY());
    }

}
