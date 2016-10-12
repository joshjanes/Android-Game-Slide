package com.slidepuzzlegame.slidepuzzlegame;

import android.graphics.Color;
import android.graphics.Paint;

import com.slidepuzzlegame.framework.Game;
import com.slidepuzzlegame.framework.Graphics;
import com.slidepuzzlegame.framework.Graphics.ImageFormat;
import com.slidepuzzlegame.framework.Screen;

public class LoadingScreen extends Screen {
	public LoadingScreen(Game game) {
		
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();

		Assets.background = g.newImage("background.png", ImageFormat.RGB565);
		Assets.backgroundmid = g.newImage("backgroundmid.png", ImageFormat.RGB565);
		Assets.backgroundmenu = g.newImage("backgroundmenu.png", ImageFormat.RGB565);
		//Assets.character = g.newImage("main.png", ImageFormat.ARGB4444);
		Assets.home = g.newImage("home.png", ImageFormat.RGB565);
		Assets.reset = g.newImage("return.png", ImageFormat.ARGB4444);
		Assets.background = g.newImage("background.png", ImageFormat.ARGB4444);
		Assets.decrease = g.newImage("decrease.png", ImageFormat.RGB565);
		Assets.increase = g.newImage("increase.png", ImageFormat.RGB565);

		Assets.character = g.newImage("main.png", ImageFormat.ARGB4444);
		Assets.stopblock = g.newImage("block.png", ImageFormat.ARGB4444);
		Assets.checkpoint = g.newImage("checkpoint.png", ImageFormat.ARGB4444);
		Assets.checkpointon = g.newImage("checkpointon.png", ImageFormat.ARGB4444);
		Assets.up = g.newImage("up.png", ImageFormat.ARGB4444);
		Assets.down = g.newImage("down.png", ImageFormat.ARGB4444);
		Assets.left = g.newImage("left.png", ImageFormat.ARGB4444);
		Assets.right = g.newImage("right.png", ImageFormat.ARGB4444);
		Assets.red = g.newImage("red.png", ImageFormat.ARGB4444);
		Assets.green = g.newImage("green.png", ImageFormat.ARGB4444);
		Assets.no = g.newImage("no.png", ImageFormat.ARGB4444);
		Assets.ice = g.newImage("ice.png", ImageFormat.ARGB4444);

		Assets.charactermid = g.newImage("mainmid.png", ImageFormat.ARGB4444);
		Assets.stopblockmid = g.newImage("blockmid.png", ImageFormat.ARGB4444);
		Assets.checkpointmid = g.newImage("checkpointmid.png", ImageFormat.ARGB4444);
		Assets.checkpointmidon = g.newImage("checkpointmidon.png", ImageFormat.ARGB4444);
		Assets.upmid = g.newImage("upmid.png", ImageFormat.ARGB4444);
		Assets.downmid = g.newImage("downmid.png", ImageFormat.ARGB4444);
		Assets.leftmid = g.newImage("leftmid.png", ImageFormat.ARGB4444);
		Assets.rightmid = g.newImage("rightmid.png", ImageFormat.ARGB4444);
		Assets.redmid = g.newImage("redmid.png", ImageFormat.ARGB4444);
		Assets.greenmid = g.newImage("greenmid.png", ImageFormat.ARGB4444);
		Assets.nomid = g.newImage("nomid.png", ImageFormat.ARGB4444);
		Assets.icemid = g.newImage("icemid.png", ImageFormat.ARGB4444);

		Assets.button = g.newImage("button.jpg", ImageFormat.RGB565);

		//sounds
		Assets.buttonpress = game.getAudio().createSound("button.wav");
		Assets.click = game.getAudio().createSound("click.wav");
		Assets.clack = game.getAudio().createSound("clack.wav");
		Assets.error = game.getAudio().createSound("error.wav");
		Assets.poweroff = game.getAudio().createSound("poweroff.wav");
		Assets.poweron = game.getAudio().createSound("poweron.wav");
		Assets.ramp = game.getAudio().createSound("ramp.wav");
		Assets.slide = game.getAudio().createSound("slide.wav");
		Assets.thump = game.getAudio().createSound("thump.wav");
		Assets.win = game.getAudio().createSound("win.wav");

		game.setScreen(new MainMenuScreen(game));

	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.backgroundmenu, 0, 0);

		Paint paint = new Paint();
		paint.setTextSize(50);
		paint.setAntiAlias(true);
		paint.setColor(Color.rgb(12,63,106));
		g.drawString("loading", 100, 150, paint);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {

	}
}