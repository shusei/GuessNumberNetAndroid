package com.heavener.guessnumbernet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private TextView textViewID;
    private Button buttonOK, buttonX;
    private Button button0, button1, button2, button3, button4,
            button5, button6, button7, button8, button9;
    private String guessNumberPlayer1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        textViewID = (TextView) findViewById(R.id.textViewID);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonOK:
                Intent intent = new Intent(MainActivity.this, PlayingActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonX:
                guessNumberPlayer1 = guessNumberPlayer1.substring(guessNumberPlayer1.length()-1);
                break;
            case R.id.button0:
                if(button0.){
                    guessNumberPlayer1+="0";
                    button0.setEnabled(false);
                }
                else {
                    button0.setEnabled(true);
                    guessNumberPlayer1.replace("0", "");
                }
                break;
            case R.id.button1:
                if(button1.isEnabled()){
                    guessNumberPlayer1+="1";
                    button1.setEnabled(false);
                }
                else {
                    button1.setEnabled(true);
                    guessNumberPlayer1.replace("1", "");
                }
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

        textViewID.setText(guessNumberPlayer1);
    }

}
