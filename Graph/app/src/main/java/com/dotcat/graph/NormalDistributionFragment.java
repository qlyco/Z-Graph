/* NormalDistributionFragment.java
 * ------------------
 * Author: Danial Fitri Ghazali (dfx) from dot cat
 * Contact: danialfitrighazali@gmail.com
 * Description: Class to calculate the normal distribution
 * and show the associated graph
 */

// Package declaration
package com.dotcat.graph;

// Imports
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import java.util.Locale;

// Start of class
public class NormalDistributionFragment extends Fragment {

    // Define the current selected mode of calc
    enum Mode
    {
        LESS,
        MID,
        MORE
    }

    // Properties

    // UI elements
    private View view;
    private TextView zText;
    private TextView probText;
    private GraphView gw;
    private SeekBar zBar;
    private ImageButton ltBtn;
    private ImageButton gtBtn;
    private ImageButton markBtn;

    // Used for calculations/result display
    private int zVal = 409;
    private int zVal2 = 0;
    private String sign;
    private double probVal = 0.5;
    private float[] prob;
    private Mode mode = Mode.LESS;

    // Used to check secret game access
    private int secret;

    // When the fragment starts, init the required ui elements

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_normal_distribution, container, false);
        init();
        return view;
    }

    // Instantiate all the required UI elements
    private void init()
    {
        zText = view.findViewById(R.id.text);
        probText = view.findViewById(R.id.prob);

        ImageView graph = view.findViewById(R.id.graph);
        gw = new GraphView(0, 0.5);
        graph.setImageDrawable(gw);

        zBar = view.findViewById(R.id.zbar);

        ltBtn = view.findViewById(R.id.ltBtn);
        gtBtn = view.findViewById(R.id.gtBtn);
        markBtn = view.findViewById(R.id.markBtn);
        ImageButton incBtn = view.findViewById(R.id.incBtn);
        ImageButton decBtn = view.findViewById(R.id.decBtn);

        secret = 0;

        // Add listener to the seekbar
        zBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            // Calc the new value everytime the seekbar was moved

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                zVal = progress;
                update();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        // Set all the evt listener to the buttons
        ltBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ltMode();
            }
        });
        gtBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gtMode();
            }
        });
        markBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                midMode();
            }
        });
        incBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                incZ();
            }
        });
        decBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                decZ();
            }
        });
        ltBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ltMode();
            }
        });

        // Get the Z vals
        loadData();
    }

    // Get the Z vals from the provided bundle
    private void loadData()
    {
        assert getArguments() != null;
        prob = getArguments().getFloatArray("val");
    }

    // Used to update the display to reflect new values
    private void update()
    {
        String str;
        calc(); // Perform the required calculations

        switch (mode)
        {
            case MID:
            case LESS:
                sign = "< ";
                break;
            case MORE:
                sign = "> ";
                break;
        }

        if (mode != Mode.MID)
        {
            sign += (zVal >= 409) ? "+" : "";
            str = "P (Z "
                    + String.format(Locale.ENGLISH, "%s%.2f", sign, (zVal - 409) / 100.0)
                    + ")";
            zText.setText(str);
        }
        else zText.setText(mid(zVal, zVal2));

        str = "= " + String.format(Locale.ENGLISH, "%.5f", probVal);
        probText.setText(str);
    }

    // Mid requires special formatting unlike Less than or Greater than mode
    private String mid(int val1, int val2)
    {
        int max;
        int min;

        if (val1 == val2)
        {
            max = val1;
            min = val2;
        }
        else
        {
            max = Math.max(val1, val2);
            min = Math.min(val1, val2);
        }

        String polar = (min >= 409) ? "+" : "";
        String str = "P ("
                + String.format(Locale.ENGLISH, "%s%.2f", polar, (min - 409) / 100.0);
        polar = (max >= 409) ? "+" : "";
        str += " " + sign + "Z " + sign
                + String.format(Locale.ENGLISH, "%s%.2f", polar, (max - 409) / 100.0)
                + ")";

        return str;
    }

    // Calculate the value from the current Z value and mode
    private void calc()
    {
        switch(mode)
        {
            case LESS:
                calcLess();
                break;
            case MORE:
                calcMore();
                break;
            case MID:
                calcMid();
                break;
        }
    }

    // Calculate the prob if the mode is less than
    private void calcLess()
    {
        if (zVal >= 409)
            probVal = 1 - prob[zVal - 409];
        else probVal = prob[409 - zVal];

        gw.setBounds(0, zVal / 810.0);
    }

    // Calculate the prob if the mode is more than
    private void calcMore()
    {
        if (zVal >= 409)
            probVal = prob[zVal - 409];
        else probVal = 1 - prob[409 - zVal];

        gw.setBounds(zVal / 810.0, 1.0);
    }

    // Calculate the prob if the mode is mid
    private void calcMid()
    {
        int val1, val2;
        double res1, res2;

        if (zVal != zVal2)
        {
            val1 = Math.max(zVal, zVal2);
            val2 = Math.min(zVal, zVal2);

            if (val1 >= 409)
                res1 = 1 - prob[val1 - 409];
            else res1 = prob[409 - val1];

            if (val2 >= 409)
                res2 = 1 - prob[val2 - 409];
            else res2 = prob[409 - val2];

            probVal = res1 - res2;

            gw.setBounds(val1 / 810.0, val2 / 810.0);
        }
        else
        {
            probVal = 0;
            gw.setBounds(zVal / 810.0, zVal2 / 810.0);
        }
    }

    // Set current mode to less than. Also checks secret game access
    private void ltMode()
    {
        if (secret == 4 || secret == 6) secret++;
        else secret = 0;

        mode = Mode.LESS;
        ltBtn.setEnabled(false);
        gtBtn.setEnabled(true);
        gtBtn.setImageTintList(ColorStateList.valueOf(0xffffffff));
        ltBtn.setImageTintList(ColorStateList.valueOf(0xff8181ff));
        markBtn.setImageTintList(ColorStateList.valueOf(0xffffffff));
        zBar.setSecondaryProgress(0);
        update();
    }

    // Set current mode to mid. Also checks secret game access. Since the sequence ends here,
    // it will launch the game if the user entered the sequence correctly.
    private void midMode()
    {
        if (secret == 8 || secret == 9) secret++;
        else secret = 0;

        if (secret == 10) {
            secret = 0;
            startGame();
        }

        mode = Mode.MID;
        zVal2 = zBar.getProgress();
        zBar.setSecondaryProgress(zVal2);
        ltBtn.setEnabled(true);
        gtBtn.setEnabled(true);
        gtBtn.setImageTintList(ColorStateList.valueOf(0xffffffff));
        ltBtn.setImageTintList(ColorStateList.valueOf(0xffffffff));
        markBtn.setImageTintList(ColorStateList.valueOf(0xff8181ff));
        update();
    }

    // Set current mode to greater than. Also checks secret game access
    private void gtMode()
    {
        if (secret == 5 || secret == 7) secret++;
        else secret = 0;

        mode = Mode.MORE;
        gtBtn.setEnabled(false);
        ltBtn.setEnabled(true);
        gtBtn.setImageTintList(ColorStateList.valueOf(0xff8181ff));
        ltBtn.setImageTintList(ColorStateList.valueOf(0xffffffff));
        markBtn.setImageTintList(ColorStateList.valueOf(0xffffffff));
        zBar.setSecondaryProgress(0);
        update();
    }

    // Increment the seekbar progress by 1. Useful for small screens. Also tracks secret game
    // access.
    private void incZ()
    {
        if (secret == 0 || secret == 1) secret++;
        else secret = 0;

        zBar.setProgress(zBar.getProgress() + 1);
    }

    // Decrement the seekbar progress by 1. Useful for small screens. Also tracks secret game
    // access.
    private void decZ()
    {
        if (secret == 2 || secret == 3) secret++;
        else secret = 0;

        zBar.setProgress(zBar.getProgress() - 1);
    }

    // If the user gain access to the game, start the game.
    private void startGame() {
        Intent intent = new Intent(this.getContext(), GameActivity.class);
        startActivity(intent);
    }
}
