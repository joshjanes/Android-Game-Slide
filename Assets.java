package com.slidepuzzlegame.slidepuzzlegame;

import com.slidepuzzlegame.framework.Image;
import com.slidepuzzlegame.framework.Music;
import com.slidepuzzlegame.framework.Sound;

public class Assets {

	//Image setups
	public static Image background, backgroundmid, backgroundmenu, decrease, increase, home, reset;
	//Large sprites:
	public static Image character, stopblock, checkpoint, checkpointon, up, down, left, right, red, green, no, ice;
	//Medium sprites:
	public static Image charactermid, stopblockmid, checkpointmid, checkpointmidon, upmid, downmid, leftmid, rightmid, redmid, greenmid, nomid, icemid;
	public static Image buffer;

	public static Sound buttonpress, click, clack, error, poweroff, poweron, ramp, slide, thump, win;

	public static Music theme;

	public static Image menu, splash;
	public static Image button;
	
	public static void load(Slide slide) {
		// TODO Auto-generated method stub
		theme = slide.getAudio().createMusic("theme.mp3");
		theme.setLooping(true);
		theme.setVolume(1);
		theme.play();
		if (Slide.soundson == false) {
			theme.pause();
		}
	}
	
}
