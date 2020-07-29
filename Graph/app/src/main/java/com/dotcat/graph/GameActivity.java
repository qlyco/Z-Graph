/* GameActivity.java
 * ------------------
 * Author: Danial Fitri Ghazali (dfx) from dot cat
 * Contact: danialfitrighazali@gmail.com
 * Description: Launches a secret game hidden in the app.
 * To launch, press + + - - < > < > _ _ in that sequence inside
 * the Normal Distribution mode.
 * The buttons must not be disabled for it to count as a press.
 */

// Package declaration
package com.dotcat.graph;

// Imports
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

// Start of class
public class GameActivity extends AppCompatActivity
{
    // When the activity is created, setup the game
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        start();
    }

    // The game is run inside a WebView and coded
    // using JavaScript. So, js support is necessary.
    @SuppressLint("SetJavaScriptEnabled")
    private void start() {
        WebView game = findViewById(R.id.game);
        WebSettings setting = game.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);
        setting.setDomStorageEnabled(true);

        // Load the game file and run.
        // The game and all of its assets are created by Danial Fitri Ghazali (dfx) and
        // based on Flappy Bird.
        // The game is also available on my website: dfx81.github.io
        game.loadUrl("file:///android_asset/Flap.html");
    }
}
