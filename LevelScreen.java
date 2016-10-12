package com.slidepuzzlegame.slidepuzzlegame;

import java.util.List;

import com.slidepuzzlegame.framework.Game;
import com.slidepuzzlegame.framework.Graphics;
import com.slidepuzzlegame.framework.Screen;
import com.slidepuzzlegame.framework.Input.TouchEvent;
import android.graphics.Paint;
import android.graphics.Color;

public class LevelScreen extends Screen {
    public LevelScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {


                //decrease button
                if (inBounds(event, 25, 225, 100, 140)) {
                    if (Slide.soundson) {
                        Assets.click.play(0.5f);
                    }
                    //decrease button
                    if (Slide.level > 1) {
                        Slide.level--;

                        //resets variables for next level
                        //resetLevel();
                    } else {
                        Slide.level = Slide.maxlevel;

                    }
                }
                //increase button
                if (inBounds(event, 150, 225, 100, 140)) {
                    if (Slide.soundson) {
                        Assets.click.play(0.5f);
                    }
                    if (Slide.level < Slide.maxlevel) {
                        Slide.level++;

                        //resets variables for next level
                        //resetLevel();
                    } else {
                        Slide.level = 1;

                        //reset level
                        //resetLevel();
                    }
                }

                //back button
                if (inBounds(event, 100, 525, 200, 75)) {
                    if (Slide.soundson) {
                        Assets.click.play(0.5f);
                    }
                    game.setScreen(new MainMenuScreen(game));
                }

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
        Graphics g = game.getGraphics();

        Paint paint = new Paint();
        paint.setTextSize(50);
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(12,63,106));
        g.drawImage(Assets.backgroundmenu, 0, 0);
        g.drawString("slide", 100, 150, paint);

        paint.setTextSize(27);
        g.drawString("level", 134, 250, paint);
        g.drawImage(Assets.decrease, 90, 275);
        g.drawImage(Assets.increase, 200, 275);
        g.drawString(String.valueOf(Slide.level), 145, 308, paint);
        g.drawString("back", 100, 550, paint);

        paint.setTextSize(17);
        paint.setColor(Color.rgb(243,240,205));
        g.drawString("copyright 2016", 100, 675, paint);
        g.drawString("joshua janes", 100, 700, paint);
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
        android.os.Process.killProcess(android.os.Process.myPid());

    }
}