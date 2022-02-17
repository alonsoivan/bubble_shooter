package com.ivn.bs.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class StaticBall extends Ball{
    public StaticBall(Color texture, Vector2 position, int tam) {
        super(texture, position,tam);

    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

    }
}
