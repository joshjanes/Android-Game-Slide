package com.slidepuzzlegame.framework;

import com.slidepuzzlegame.framework.Graphics.ImageFormat;

public interface Image {
    public int getWidth();
    public int getHeight();
    public ImageFormat getFormat();
    public void dispose();
}
