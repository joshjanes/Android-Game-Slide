package com.slidepuzzlegame.slidepuzzlegame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

import com.slidepuzzlegame.framework.Screen;
import com.slidepuzzlegame.framework.implementation.AndroidGame;

public class Slide extends AndroidGame {

	public static String levelstring;
	public static boolean soundson = true;
	boolean firstTimeCreate = true;
	public static int level = 1;
	public static int maxlevel = 40;

	@Override
	public Screen getInitScreen() {

		if (firstTimeCreate) {
			Assets.load(this);
			firstTimeCreate = false;
		}

		return new SplashLoadingScreen(this);

	}

	@Override
	public void onBackPressed() {
		getCurrentScreen().backButton();
	}

	private static String convertStreamToString(InputStream is, int level) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append((line + "\n"));
			}
		} catch (IOException e) {
			Log.w("LOG", e.getMessage());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.w("LOG", e.getMessage());
			}
		}
		return sb.toString();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Slide.soundson) {
			Assets.theme.play();
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		Assets.theme.pause();

	}

}