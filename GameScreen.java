package com.slidepuzzlegame.slidepuzzlegame;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.slidepuzzlegame.framework.Game;
import com.slidepuzzlegame.framework.Graphics;
import com.slidepuzzlegame.framework.Input.TouchEvent;
import com.slidepuzzlegame.framework.Screen;
import com.slidepuzzlegame.framework.implementation.AndroidGame;

public class GameScreen extends Screen {

	private int endcountdown = 50;
	public boolean levelfinish = false;
	public static Block block;
	private boolean velocity = false, soundon = true, resetlevel = false;
	public static int direction;
	private int startX, startY, endX, endY, counter = 0, counter2 = 0, maxlevel = 40, score = 0;
	//this will contain the tilemap for the levels:
	private char[] array[] = new char[16][32];
	//this boolean sets the size of the level:
	private char size;
	private int isize, jsize, xincrement, yincrement;
	//this is the state the game starts in
	//this is the level the game starts on
	private static int level = 1;
	private int switchcounter = 0;
	public boolean errorsound = true;
	private static int currentlevel = 0;
	public boolean dragged = false;

	//these floats are for registering directional swipes
	public float x1 = -1, x2 = -1, y1 = -1, y2 = -1;
	//for now the (arbitrary) maximum number of iceblocks in a level is 20
	public static Block iceblockarray[] = new Block[20];
	private int iceblockcounter = 0;

	public static Paint paint;
	public static Graphics g;


	//games states
	public enum States {
		Running,
		Paused
	};
	private States state = States.Running;

	public GameScreen(Game game) {
		super(game);

		// Defining a paint object
		paint = new Paint();
		paint.setTextSize(30);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);

		// Initialize game objects here
		block = new Block();
		//initializes the iceblocks
		for (int i = 0; i<20; i++) {
			iceblockarray[i] = new Block();
		}

		loadMap();
	}

	//this method will load a new level for the game
	private void loadMap() {

			// opens the level files
			// each file contains (on seperate lines):
			// -a letter (a for small level, b for big)
			// -for numbers for the starting x,y coordiantes
			// and the finish x,y coordinates, each on seperate
			// lines.
			// -an array of chars as a tile map:
			//      '1' means block
			//      '0' means nothing
			//      'u'/'d'/'l'/'r' means a ramp
			//		'b' means iceblock
			InputStream ins = null;

			try {
				// new input stream created
				ins = AndroidGame.fileIO.readAsset("level" + String.valueOf(Slide.level) + ".txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(ins));
				//Read File Line By Line
				String strLine = br.readLine();

				if (strLine.charAt(0) == 'a') {
					size = 'a';
				}
				else if (strLine.charAt(0) == 'b') {
					size = 'b';
				}
				else if (strLine.charAt(0) == 'c') {
					size = 'c';
				}

				//start of level x and y
				strLine = br.readLine();
				startX = Integer.valueOf(strLine);

				this.block.setCenterX(startX);

				strLine = br.readLine();
				startY = Integer.valueOf(strLine);
				this.block.setCenterY(startY);

				//end of level x and y
				strLine = br.readLine();
				endX = Integer.valueOf(strLine);

				strLine = br.readLine();
				endY = Integer.valueOf(strLine);

				if (size == 'a') {
					isize = 8;
					jsize = 32;
					xincrement = 60;
					yincrement = 50;
				}
				else if (size == 'b') {
					isize = 12;
					jsize = 50;
					xincrement = 40;
					yincrement = 32;
				}
				else if (size == 'c') {
					isize = 16;
					jsize = 64;
					xincrement = 30;
					yincrement = 25;
				}

				for (int i = 0; i<isize; i++) {
					strLine = br.readLine();
					for (int j = 0; j<jsize; j+=2){
						//game tiles:
						//no block
						if (strLine.charAt(j) == '0') {
							array[i][counter] = '0';
						}
						//block
						else if (strLine.charAt(j) == '1') {
							array[i][counter] = '1';
						}
						//up ramp
						else if (strLine.charAt(j) == 'u') {
							array[i][counter] = 'u';
						}
						//down ramp
						else if (strLine.charAt(j) == 'd') {
							array[i][counter] = 'd';
						}
						//left ramp
						else if (strLine.charAt(j) == 'l') {
							array[i][counter] = 'l';
						}
						//right ramp
						else if (strLine.charAt(j) == 'r') {
							array[i][counter] = 'r';
						}
						//on switch
						else if (strLine.charAt(j) == 'g') {
							array[i][counter] = 'g';
						}
						//off switch
						else if (strLine.charAt(j) == 'o') {
							array[i][counter] = 'o';
							//since these switches are off, we add one to
							//switchcounter. the exit to the level will only
							//be active if all the switches are on (when
							//switchcounter = 0
							switchcounter++;
						}
						//ice block
						else if (strLine.charAt(j) == 'b') {
							iceblockarray[iceblockcounter].setCenterX(i*xincrement);
							iceblockarray[iceblockcounter].setCenterY(counter*yincrement);
							iceblockcounter++;
						}
						counter++;
					}


					counter = 0;
					//sets the number of switches that need to be turned on
					block.setSwitches(switchcounter);
				}

				//i set the rest of the blocks out of bounds so we know not to draw them
				for (int i = iceblockcounter; i<20; i++) {
					iceblockarray[i].setCenterX(1000);
					iceblockarray[i].setCenterY(1000);
					iceblockarray[i].setSize(size, isize, jsize, xincrement, yincrement);
				}

				iceblockcounter = 0;

				//sends the completed level to the block class
				//(for collision detection purposes)
				block.setArray(array);
				block.setSize(size, isize, jsize, xincrement, yincrement);

				//Close the input stream
				ins.close();

			}
			catch (IOException e) {
				System.out.println("no");
			}

		currentlevel = level;
		//increase currentlevel to indicate we are in a new level

		//we change the reset variable back to false
		resetlevel = false;
		score = 0;
		switchcounter = 0;
	}

	@Override
	public void update(float deltaTime) {

		//updates main block position
		if (velocity && Block.iceblockmoving == false){
			if (direction == 1 || direction == 2) {
				if (size == 'a') {
					block.update((float) (deltaTime * 1.25));
				}
				else {
					block.update(deltaTime);
				}
			}
			else {
				if (size == 'a') {
					block.update(deltaTime);
				}
				else {
					block.update((float) (deltaTime * 1.25));
				}
			}
			//continues moving in one direction until velocity returns false,
			//which indicates that the block has hit an obstacle and thus stops
			velocity = block.moveBlock(direction);
		}

		//updates iceblock if iceblock is moving
		if (Block.iceblockmoving) {
			if (direction == 1 || direction == 2) {
				if (size == 'a') {
					iceblockarray[Block.iceblocknumber].update((float) (deltaTime * 1.25));
				}
				else {
					iceblockarray[Block.iceblocknumber].update(deltaTime);
				}
			}
			else {
				if (size == 'a') {
					iceblockarray[Block.iceblocknumber].update(deltaTime);
				}
				else {
					iceblockarray[Block.iceblocknumber].update((float) (deltaTime*1.25));
				}
			}

			velocity = iceblockarray[Block.iceblocknumber].moveBlock(direction);
			if (!velocity) {
				Block.iceblockmoving = false;
			}
		}

		//win condition for each level: correct x,y, and block not moving
		if (block.getCenterX() == endX && block.getCenterY() == endY && velocity == false && block.getSwitches() == 0) {

			if (endcountdown == 0) {
				if (Slide.soundson) {
					Assets.win.play(0.1f);
				}

				//advances to the next level
				Slide.level++;
				if (Slide.level == 41) {
					Slide.level = 1;
				}

				//resets variables for next level
				counter = 0;
				score = 0;
				for (int i = 0; i < 16; i++) {
					for (int j = 0; j < 32; j++) {
						array[i][j] = '0';
					}
				}

				//wait until user clicks the screen
				game.setScreen(new GameScreen(game));
			}
			else {
				endcountdown--;
			}
		}

		else if (block.getCenterX() == endX && block.getCenterY() == endY && velocity == false && block.getSwitches() != 0) {
			if (Slide.soundson && errorsound) {
				Assets.error.play(0.1f);
				errorsound = false;
			}
		}

		Graphics g = game.getGraphics();
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);

			if (levelfinish) {
				levelfinish = false;
				continue;
			}

			if (event.type == TouchEvent.TOUCH_UP) {
				//user pressed the reset button
				if (inBounds(event, 0, 0, 60, 50)) {
					if (Slide.soundson) {
						Assets.click.play(0.5f);
					}

					game.setScreen(new GameScreen(game));
					//resetLevel();
				}
				//user pressed the home button
				if (inBounds(event, 420, 0, 60, 50)) {
					if (Slide.soundson) {
						Assets.click.play(0.5f);
					}
					game.setScreen(new MainMenuScreen(game));
				}
			}
			if (dragged == false && event.type == TouchEvent.TOUCH_DRAGGED && event.y < 750) {
				x1 = event.x;
				y1 = event.y;
				dragged = true;
			}
			if (event.type == TouchEvent.TOUCH_UP && dragged == true) {
				x2 = event.x;
				y2 = event.y;

				//up swipe
				if (getDirection(x1, y1, x2, y2) == Direction.up) {
					if (velocity != true && block.getSpeedX() == 0 && block.getSpeedY() == 0) {
						score++;
						if (Slide.soundson) {
							//Assets.ramp.play(5);
						}

						direction = 1;
						velocity = true;
						errorsound = true;
					}
				}
				//down swipe
				if (getDirection(x1, y1, x2, y2) == Direction.down) {
					if (velocity != true && block.getSpeedX() == 0 && block.getSpeedY() == 0) {
						score++;
						if (Slide.soundson) {
							//Assets.ramp.play(5);
						}

						direction = 2;
						velocity = true;
						errorsound = true;
					}
				}
				//left swipe
				if (getDirection(x1, y1, x2, y2) == Direction.left) {
					if (velocity != true && block.getSpeedX() == 0 && block.getSpeedY() == 0) {
						score++;
						if (Slide.soundson) {
							//Assets.ramp.play(5);
						}

						direction = 3;
						velocity = true;
						errorsound = true;
					}
				}
				//right swipe
				if (getDirection(x1, y1, x2, y2) == Direction.right) {
					if (velocity != true && block.getSpeedX() == 0 && block.getSpeedY() == 0) {
						score++;
						if (Slide.soundson) {
							//Assets.ramp.play(5);
						}

						direction = 4;
						velocity = true;
						errorsound = true;
					}
				}
				x1 = -1;
				x2 = -1;
				y1 = -1;
				y2 = -1;
				dragged = false;
			}
		}

	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	@Override
	public void paint(float deltaTime) {

		g = game.getGraphics();
		counter = 0;

		if (size == 'a') {
			g.drawImage(Assets.background, 0, 0);
		}
		else if (size == 'b') {
			g.drawImage(Assets.backgroundmid, 0, 0);
		}

		//this draws the sprites
		for (int i = 0; i<480; i += xincrement) {
			for (int j = 0; j<800; j += yincrement) {
				//large sprites
				if (size == 'a') {
					if (array[counter][counter2] == '1') {
						g.drawImage(Assets.stopblock, i, j);
					}
					else if (array[counter][counter2] == 'u') {
						g.drawImage(Assets.up, i, j);
					}
					else if (array[counter][counter2] == 'd') {
						g.drawImage(Assets.down, i, j);
					}
					else if (array[counter][counter2] == 'l') {
						g.drawImage(Assets.left, i, j);
					}
					else if (array[counter][counter2] == 'r') {
						g.drawImage(Assets.right, i, j);
					}
					else if (array[counter][counter2] == 'g') {
						g.drawImage(Assets.green, i, j);
					}
					else if (array[counter][counter2] == 'o') {
						g.drawImage(Assets.red, i, j);
					}
				}
				//medium sprites
				if (size == 'b') {
					if (array[counter][counter2] == '1') {
						g.drawImage(Assets.stopblockmid, i, j);
					}
					else if (array[counter][counter2] == 'u') {
						g.drawImage(Assets.upmid, i, j);
					}
					else if (array[counter][counter2] == 'd') {
						g.drawImage(Assets.downmid, i, j);
					}
					else if (array[counter][counter2] == 'l') {
						g.drawImage(Assets.leftmid, i, j);
					}
					else if (array[counter][counter2] == 'r') {
						g.drawImage(Assets.rightmid, i, j);
					}
					else if (array[counter][counter2] == 'g') {
						g.drawImage(Assets.greenmid, i, j);
					}
					else if (array[counter][counter2] == 'o') {
						g.drawImage(Assets.redmid, i, j);
					}
				}
				counter2++;
			}
			counter2 = 0;
			counter++;
		}

		counter2 = 0;

		//draws avatar and finish
		if (size == 'a') {
			//checkpoint turns yellow if the block is on top of it
			if (Math.abs(block.getCenterX() - endX) <= 59 && Math.abs(block.getCenterY() - endY) <= 49 && block.getSwitches() == 0) {
				g.drawImage(Assets.checkpointon, endX, endY);
			}
			else {
				g.drawImage(Assets.checkpoint, endX, endY);
			}

			g.drawImage(Assets.character, block.getCenterX(), block.getCenterY());

			//draws the iceblocks
			for (int i = 0; i<20; i++) {

				if (iceblockarray[i].getCenterX() < 1000) {
					g.drawImage(Assets.ice, iceblockarray[i].getCenterX(), iceblockarray[i].getCenterY());
				}
			}
		}
		else if (size == 'b') {
			if (Math.abs(block.getCenterX() - endX) <= 39 && Math.abs(block.getCenterY() - endY) <= 31 && block.getSwitches() == 0) {
				g.drawImage(Assets.checkpointmidon, endX, endY);
			}
			else{
				g.drawImage(Assets.checkpointmid, endX, endY);
			}

			g.drawImage(Assets.charactermid, block.getCenterX(), block.getCenterY());
			for (int i = 0; i<20; i++) {
				if (iceblockarray[i].getCenterX() < 1000) {
					g.drawImage(Assets.icemid, iceblockarray[i].getCenterX(), iceblockarray[i].getCenterY());
				}
			}
		}

		//draws score tracker: omitted for now
		if (score > 10000) {
			//g.drawString("maybe it's time to take a break", 25, 775, paint);
			score = 0;
		}
		else {
			//g.drawString(String.valueOf(block.getSwitches()), 35, 785, paint);
		}

		//draws reset button
		g.drawImage(Assets.reset, 0, 0);
		//draws menu button
		g.drawImage(Assets.home, 420, 0);
	}

	private void nullify() {
		System.gc();

	}

	@Override
	public void pause() {
		if (state == States.Running)
			state = States.Paused;

	}

	@Override
	public void resume() {
		if (state == States.Paused)
			state = States.Running;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {
		goToMenu();
	}

	private void goToMenu() {
		// TODO Auto-generated method stub
		game.setScreen(new MainMenuScreen(game));

	}

	public static Block getBlock() {
		// TODO Auto-generated method stub
		return block;
	}

	//erases the level's tilemap and prompts the program to draw a fresh level
	private void resetLevel() {
		counter = 0;
		for (int i = 0; i<16; i++) {
			for (int j = 0; j<32; j++){
				array[i][j] = '0';
			}
		}
		switchcounter = 0;
		block.setSwitches(0);

		//resets the iceblocks
		for (int i = 0; i<20; i++) {
			iceblockarray[i].setCenterX(1000);
			iceblockarray[i].setCenterY(1000);
		}
		iceblockcounter = 0;
		loadMap();
	}

	public Direction getDirection(float x1, float y1, float x2, float y2){
		double angle = getAngle(x1, y1, x2, y2);
		return Direction.get(angle);
	}

	//finds the angle between two points
	public double getAngle(float x1, float y1, float x2, float y2) {

		double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
		return (rad*180/Math.PI + 180)%360;
	}


	public enum Direction{
		up,
		down,
		left,
		right;

		//returns a direction given an angle
		public static Direction get(double angle){
			if(inRange(angle, 45, 135)){
				return Direction.up;
			}
			else if(inRange(angle, 0,45) || inRange(angle, 315, 360)){
				return Direction.right;
			}
			else if(inRange(angle, 225, 315)){
				return Direction.down;
			}
			else{
				return Direction.left;
			}

		}

		// returns true if the given angle is in the interval
		private static boolean inRange(double angle, float init, float end){
			return (angle >= init) && (angle < end);
		}
	}
}