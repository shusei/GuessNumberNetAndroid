package com.heavener.guessnumbernet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class PlayingActivity extends AppCompatActivity {

    private AdView mAdView;

    private Button buttonOK, buttonX;
    private Button button0, button1, button2, button3, button4,
            button5, button6, button7, button8, button9;
    private TextView textViewPlayer1, textViewPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        MobileAds.initialize(this, "ca-app-pub-4286420050191609~2435862473");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        buttonOK = (Button) findViewById(R.id.buttonOK);
        buttonX = (Button) findViewById(R.id.buttonX);
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);

        textViewPlayer1 = (TextView) findViewById(R.id.textViewPlayer1);
        textViewPlayer2 = (TextView) findViewById(R.id.textViewPlayer2);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonOK:

                break;
            case R.id.buttonX:
                break;
            case R.id.button0:
                break;
            case R.id.button1:
                break;
            case R.id.button2:
                break;
            case R.id.button3:
                break;
            case R.id.button4:
                break;
            case R.id.button5:
                break;
            case R.id.button6:
                break;
            case R.id.button7:
                break;
            case R.id.button8:
                break;
            case R.id.button9:
                break;
            default:
                break;
        }
    }
}
