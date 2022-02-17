package com.ivn.bs;

import com.badlogic.gdx.Game;
import com.ivn.bs.screens.GameScreen;

public class Aplication extends Game {

	@Override
	public void create () {
		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}
