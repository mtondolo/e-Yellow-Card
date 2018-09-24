package com.android.example.e_yellowcard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mYCListTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */
        mYCListTextView = (TextView) findViewById(R.id.tv_yc);

        /*
         * This String array contains yellow card numbers.
         *
         */
        String[] ycNumber = YellowCard.getYellowCardNumber();

        /*
         * Iterate through the array and append the Strings to the TextView. The purpose of
         * the "\n\n\n" after the String is to give visual separation between each String in the
         * TextView.
         */
        for (String yellowCardNumber : ycNumber) {
            mYCListTextView.append(yellowCardNumber + "\n\n\n");
        }
    }
}

