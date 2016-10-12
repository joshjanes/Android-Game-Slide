package com.slidepuzzlegame.slidepuzzlegame;

public class Block {

    private char[] array[] = new char[16][32];

    private int x1, x2, y1, y2;
    public static char size;
    private int isize, jsize, xincrement, yincrement;

    private int centerX = 60;
    private int centerY = 150;
    private int speedX = 0;
    private int speedY = 0;

    public static boolean iceblockmoving = false;
    public static int iceblocknumber = -1;

    private boolean switchstop = true;
    private int switchcounter = 0;

    public void update(float deltaTime) {

        if (speedX + centerX > 480-xincrement  || speedX + centerX < 0) {
            if (Slide.soundson) {
                Assets.thump.play(0.5f);
            }
            speedX = 0;
        }
        if (speedY + centerY > 800-yincrement || speedY + centerY < 0) {
            if (Slide.soundson) {
                Assets.thump.play(0.5f);
            }
            speedY = 0;
        }


        // Updates X Position
        if (speedX != 0 && speedY == 0 ) {
            centerX += speedX*deltaTime;
            switchstop = true;
        }

        // Updates Y Position
        if (speedY != 0 && speedX == 0) {
            centerY += speedY*deltaTime;
            switchstop = true;
        }

        //this loop check for collisions with the ramps
        for (int i = 0; i<isize; i++) {
            for (int j = 0; j<jsize; j++) {

                //because the indices of the array also map
                //to x/y coordinates i use modular arithmetic
                //to translate between the two
                if (this.centerX/xincrement == i && this.centerX%xincrement == 0 &&
                        this.centerY/yincrement == j && this.centerY%yincrement == 0) {
                    if (array[i][j] == 'u') {
                        speedY = (int) -1;
                        speedX = 0;
                        GameScreen.direction = 1;

                        if (Slide.soundson) {
                            Assets.ramp.play(0.25f);
                        }
                    }
                    if (array[i][j] == 'd')  {
                        speedY = (int) 1;
                        speedX = 0;
                        GameScreen.direction = 2;

                        if (Slide.soundson) {
                            Assets.ramp.play(0.25f);
                        }
                    }
                    if (array[i][j] == 'l')  {
                        speedX = -1;
                        speedY = 0;
                        GameScreen.direction = 3;

                        if (Slide.soundson) {
                            Assets.ramp.play(0.25f);
                        }
                    }
                    if (array[i][j] == 'r')  {
                        speedX = 1;
                        speedY = 0;
                        GameScreen.direction = 4;

                        if (Slide.soundson) {
                            Assets.ramp.play(0.25f);
                        }
                    }
                    if (switchstop) {
                        //if a switch is on...
                        if (array[i][j] == 'g')  {
                            //we set it to off
                            array[i][j] = 'o';

                            if (switchcounter == 0) {
                                if (Slide.soundson) {
                                    Assets.poweroff.play(0.5f);
                                }
                            }
                            GameScreen.block.setSwitches(GameScreen.block.getSwitches()+1);

                            if (Slide.soundson) {
                                Assets.buttonpress.play(0.5f);
                            }
                            switchstop = false;
                        }
                        //if a switch is off...
                        else if (array[i][j] == 'o')  {
                            //we set it to on
                            array[i][j] = 'g';
                            GameScreen.block.setSwitches(GameScreen.block.getSwitches()-1);

                            if (Slide.soundson) {
                                Assets.buttonpress.play(0.5f);
                            }

                            if (switchcounter == 0) {
                                if (Slide.soundson) {
                                    Assets.poweron.play(0.5f);
                                }
                            }
                            switchstop = false;
                        }
                    }
                }
            }
        }
    }

    public boolean moveBlock(int blockdirection) {

        switch (blockdirection) {

            //up
            case (1):
                speedY = (int) -1;
                speedX = 0;
                break;

            //down
            case (2):
                speedY = (int) 1;
                speedX = 0;
                break;

            //left
            case (3):
                speedX = -1;
                speedY = 0;
                break;

            //right
            case (4):
                speedX = 1;
                speedY = 0;
                break;
        }

        //this finds which index of the array the top left
        //corner of the box is in
        x1 = (centerX+speedX) / xincrement;
        y1 = (centerY+speedY) / yincrement;

        //this finds which index of the array the bottom
        //right corner is in
        x2 = (centerX+speedX+(xincrement-1)) / xincrement;
        y2 = (centerY+speedY+(yincrement-1)) / yincrement;

        //prevents the box from going out of bounds
        if (centerX+speedX < 0 || centerY+speedY < 0){
            speedX = 0;
            speedY = 0;
            if (Slide.soundson) {
                Assets.thump.play(0.5f);
            }
            return false;
        }

        //checks for collisions with iceblocks
        for (int i = 0; i<20; i++) {
            if (GameScreen.iceblockarray[i].getCenterX() < 1000) {
                //top collision with iceblock
                if (blockdirection == 1 && centerX == GameScreen.iceblockarray[i].getCenterX()
                        && centerY == GameScreen.iceblockarray[i].getCenterY() + yincrement) {
                    speedY = 0;
                    GameScreen.iceblockarray[i].setSize(size, isize, jsize, xincrement, yincrement);
                    GameScreen.iceblockarray[i].setArray(array);
                    if (Slide.soundson) {
                        Assets.clack.play(0.5f);
                    }

                    iceblockmoving = true;
                    iceblocknumber = i;

                    return true;
                }
                //bottom collision
                else if (blockdirection == 2 && centerX == GameScreen.iceblockarray[i].getCenterX()
                        && centerY == GameScreen.iceblockarray[i].getCenterY() - yincrement) {
                    speedY = 0;
                    GameScreen.iceblockarray[i].setSize(size, isize, jsize, xincrement, yincrement);
                    GameScreen.iceblockarray[i].setArray(array);
                    if (Slide.soundson) {
                        Assets.clack.play(0.5f);
                    }

                    iceblockmoving = true;
                    iceblocknumber = i;

                    return true;
                }
                //left collision
                else if (blockdirection == 3 && centerX == GameScreen.iceblockarray[i].getCenterX() + xincrement
                        && centerY == GameScreen.iceblockarray[i].getCenterY()) {
                    speedX = 0;
                    GameScreen.iceblockarray[i].setSize(size, isize, jsize, xincrement, yincrement);
                    GameScreen.iceblockarray[i].setArray(array);
                    if (Slide.soundson) {
                        Assets.clack.play(0.5f);
                    }

                    iceblockmoving = true;
                    iceblocknumber = i;

                    return true;

                }
                //right collision
                else if (blockdirection == 4 && centerX == GameScreen.iceblockarray[i].getCenterX() - xincrement
                        && centerY == GameScreen.iceblockarray[i].getCenterY()) {
                    speedX = 0;
                    GameScreen.iceblockarray[i].setSize(size, isize, jsize, xincrement, yincrement);
                    GameScreen.iceblockarray[i].setArray(array);
                    if (Slide.soundson) {
                        Assets.clack.play(0.5f);
                    }

                    iceblockmoving = true;
                    iceblocknumber = i;

                    return true;
              }
            }
        }


        //checks top left corner for collisions
        if (array[x1][y1] == '1') {
            speedX = 0;
            speedY = 0;
            if (Slide.soundson) {
                Assets.thump.play(0.5f);
            }
            return false;
        }

        if (x2<isize && y2<jsize/2) {
            //top right, bottom left, and bottom right corners
            if (array[x2][y1] == '1' || array[x1][y2] == '1' || array[x2][y2] == '1') {
                speedX = 0;
                speedY = 0;
                if (Slide.soundson) {
                    Assets.thump.play(0.5f);
                }
                return false;
            }
        }
        else {
            if (Slide.soundson) {
                Assets.thump.play(0.5f);
            }
            speedX = 0;
            speedY = 0;
            return false;
        }

        //checks for collision with main block
        if (GameScreen.block.centerX != centerX || GameScreen.block.centerY != centerY) {
            if (GameScreen.direction == 1 && centerX == GameScreen.block.centerX &&
                    centerY-yincrement == GameScreen.block.centerY) {
                speedY = 0;
                while (GameScreen.block.moveBlock(GameScreen.direction)) {
                    /*try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
                return false;
            }
            if (GameScreen.direction == 2 && centerX == GameScreen.block.centerX &&
                    centerY+yincrement == GameScreen.block.centerY) {
                speedY = 0;
                while (GameScreen.block.moveBlock(GameScreen.direction)) {
                    /*try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
                return false;
            }
            if (GameScreen.direction == 3 && centerY == GameScreen.block.centerY &&
                    centerX-xincrement == GameScreen.block.centerX) {
                speedX = 0;
                while (GameScreen.block.moveBlock(GameScreen.direction)) {
                    /*try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
                return false;
            }
            if (GameScreen.direction == 4 && centerY == GameScreen.block.centerY &&
                    centerX+xincrement == GameScreen.block.centerX) {
                speedX = 0;
                while (GameScreen.block.moveBlock(GameScreen.direction)) {
                    /*try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
                return false;
            }
        }
        //passed collisions tests, now we can draw the image
        //this.update();

        return true;


    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setArray(char[] array[]) {
        this.array = array;
    }


    public void setSize(char newchar, int isize, int jsize, int xincrement, int yincrement) {
        this.size = newchar;
        this.isize = isize;
        this.jsize = jsize;
        this.xincrement = xincrement;
        this.yincrement = yincrement;
    }

    public void setSwitches(int newnumber) {
        this.switchcounter = newnumber;
    }

    public int getSwitches() {
        return switchcounter;
    }

    public int getSize() {
        return yincrement;
    }
}
