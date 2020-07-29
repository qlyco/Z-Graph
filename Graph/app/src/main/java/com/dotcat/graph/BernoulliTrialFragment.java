/* BernoulliTrialFragment.java
 * ------------------
 * Author: Danial Fitri Ghazali (dfx) from dot cat
 * Contact: danialfitrighazali@gmail.com
 * Description: Perform calculations for binomial
 * distribution.
 */

// Package declaration
package com.dotcat.graph;

// Imports
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.math.BigDecimal;
import java.util.Locale;

// Start of class
public class BernoulliTrialFragment extends Fragment {
    // Declare some property that allows us to modify the display to show results
    // on the screen.
    private View view;
    private TextView result;
    private EditText kEdit;
    private EditText nEdit;
    private EditText pEdit;

    // When the fragment is created, initialize all ui element using the View created
    // and perform a calculation w/ default value to remove the placeholder val.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bernoulli_trial, container, false);

        init();
        calc();
        return view;
    }

    // Method to initialize all the required ui elements
    // NOTE: We can change it to get a View parameter so we don't need to declare
    // an additional View property.
    private void init() {
        result = view.findViewById(R.id.result_poisson);
        kEdit = view.findViewById(R.id.kEdit);
        nEdit = view.findViewById(R.id.nEdit);
        pEdit = view.findViewById(R.id.pEdit);

        TextView formula = view.findViewById(R.id.formula_poisson);

        // Set the formula for Bernoulli trials
        // TODO: format the formula properly
        String expression = "P(k) = (nCk) (p ^ k) (q ^ (n - k))";
        formula.setText(expression);

        Button button = view.findViewById(R.id.calc_poisson);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calc();
            }
        }); // Calc on button press
    }

    // Perform the bernoulli trial calculation
    private void calc() {
        // Validate the inputs
        if (kEdit.getText().toString().equals("") || nEdit.getText().toString().equals("") ||
                pEdit.getText().toString().equals("")) {
            return;
        }

        int k = Integer.parseInt(kEdit.getText().toString());
        int n = Integer.parseInt(nEdit.getText().toString());
        double p = Double.parseDouble(pEdit.getText().toString());
        BigDecimal answer;

        // Guard the input based on bernoulli trials requirements
        if (p <= 1 && k <= n) {
            // We store most of the results as BigDecimal. This allows us to calculate
            // really big values. I probably don't have to do this since most students just
            // calculate simple values.
            // But it doesn't hurt to future proof some stuff.
            BigDecimal a = comb(n, k);
            BigDecimal b = new BigDecimal(p);
            b = b.pow(k);
            BigDecimal c = new BigDecimal(1 - p);
            c = c.pow(n - k);
            answer = a.multiply(b).multiply(c);
        } else {
            return; // If input isn't valid, stop calculation
        }

        // Format the result to 4 decimal places to follow standards in schools
        String ans = String.format(Locale.ENGLISH, "= %.4f", answer);
        result.setText(ans);
    }

    // TODO: Move these 3 methods to a separate utils class since I probably need it a lot later.

    // Calculate the (n, k) combination. Uses BigDecimal since it can returns extremely big
    // values
    private BigDecimal comb(int n, int k) {
        return fact(n).divide(fact(k).multiply(fact(n - k)), BigDecimal.ROUND_CEILING);
    }

    // Calculate factorials of x, calls another method fact(x, 1) that performs the calc
    // using recursion. Also uses BigDecimal since factorials WILL return big values
    private BigDecimal fact(int x) {
        return fact(x, BigDecimal.valueOf(1));
    }

    // Recursive method fact(x, tot). Uses tail recursion to avoid stack overflow.
    private BigDecimal fact(int x, BigDecimal tot) {
        if (x == 0) return tot;
        return fact(x - 1, tot.multiply(BigDecimal.valueOf(x)));
    }
}
