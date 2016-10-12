package com.slidepuzzlegame.slidepuzzlegame;

import java.util.List;

import com.slidepuzzlegame.framework.Game;
import com.slidepuzzlegame.framework.Graphics;
import com.slidepuzzlegame.framework.Screen;
import com.slidepuzzlegame.framework.Input.TouchEvent;

import android.graphics.Paint;
import android.graphics.Color;

public class OptionScreen extends Screen {
    public OptionScreen(Game game) {
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

                if (inBounds(event, 100, 250, 200, 100)) {
                    if (Slide.soundson) {
                        Assets.click.play(0.5f);
                    }
                    if (Slide.soundson) {
                        Slide.soundson = false;
                        Assets.theme.pause();

                    }
                    else {
                        Slide.soundson = true;
                        Assets.theme.play();
                    }

                }
                if (inBounds(event, 100, 500, 200, 100)) {
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
        paint.setAntiAlias(true);
        paint.setTextSize(50);
        paint.setColor(Color.rgb(12,63,106));
        g.drawImage(Assets.backgroundmenu, 0, 0);
        g.drawString("slide", 100, 150, paint);

        paint.setTextSize(27);
        g.drawString("sound", 100, 250, paint);
        g.drawString("back", 100, 550, paint);

        paint.setColor(Color.GRAY);

        if (Slide.soundson) {
            g.drawString("on", 100, 300, paint);
        }
        else {
            g.drawString("off", 100, 300, paint);
        }
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