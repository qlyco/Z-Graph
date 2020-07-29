/* AboutActivity.java
 * ------------------
 * Author: Danial Fitri Ghazali (dfx) from dot cat
 * Contact: danialfitrighazali@gmail.com
 * Description: Class to show the credits
 */

// Package declaration
package com.dotcat.graph;

// Imports
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Objects;

// Start of class
public class AboutActivity extends AppCompatActivity
{
    // Set layout and add a back button to the appbar

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_about);
    }
}
