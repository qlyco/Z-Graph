/* PoissonDistributionFragment.java
 * ------------------
 * Author: Danial Fitri Ghazali (dfx) from dot cat
 * Contact: danialfitrighazali@gmail.com
 * Description: Perform calculations for poisson
 * distribution.
 */

// Package declaration
package com.dotcat.graph;

// Imports
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.math.BigDecimal;
import java.util.Locale;

// Start of class
public class PoissonDistributionFragment extends Fragment {
    // Declare the required UI elements as properties
    private View view;
    private TextView result;
    private EditText lambda;
    private EditText xVal;

    // Initialize all UI elements and perform a calc with default val to replace placeholder

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_poisson_distribution, container, false);

        init();
        calc();

        return view;
    }

    // Instantiate all of the UI elements
    private void init() {
        TextView formula = view.findViewById(R.id.formula_poisson);
        result = view.findViewById(R.id.result_poisson);
        lambda = view.findViewById(R.id.lambdaEdit);
        xVal = view.findViewById(R.id.xEdit);
        Button calculate = view.findViewById(R.id.calc_poisson);

        calculate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                calc();
            }
        });

        // TODO: Format the formula properly
        String expression = "P(X = x) = (e ^ (-λ))(λ ^ x) / x!";
        formula.setText(expression);
    }

    // Perform the calculations. Used BigDecimal to ensure it can handle really big values.
    // Due to this, it can calculate a wider range of values, but it might get the result
    // slowly.
    private void calc() {
        if (lambda.getText().toString().equals("") || xVal.getText().toString().equals("")) {
            return;
        }

        double mean = Double.parseDouble(lambda.getText().toString());
        int success = Integer.parseInt(xVal.getText().toString());
        BigDecimal a = new BigDecimal(Math.pow(Math.E, -1 * mean));
        BigDecimal b = new BigDecimal(mean);
        b = b.pow(success);
        BigDecimal c = fact(success);
        BigDecimal answer = a.multiply(b).divide(c, BigDecimal.ROUND_CEILING);

        // Format the result with 4 decimal places based on school's standard
        String ans = String.format(Locale.ENGLISH, "= %.4f", answer);
        result.setText(ans);
    }

    // TODO: Move these methods to a separate utils class.

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
