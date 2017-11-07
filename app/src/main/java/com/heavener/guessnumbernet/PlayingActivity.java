package com.heavener.guessnumbernet;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class PlayingActivity extends AppCompatActivity {

    private AdView mAdView;

    private Button buttonOK, buttonX;
    private Button button0, button1, button2, button3, button4,
            button5, button6, button7, button8, button9;
    private TextView textViewPlayer1, textViewPlayer2;
    private String guessNumberPlayer1 = "";
    private int flag0 = 0, flag1 = 0, flag2 = 0, flag3 = 0, flag4 = 0, flag5 = 0, flag6 = 0, flag7 = 0, flag8 = 0, flag9 = 0;

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
                if(guessNumberPlayer1.length() == 4) {

                }else{
                    Toast.makeText(PlayingActivity.this, "請輸入四位數字", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonX:
                if(guessNumberPlayer1.length() != 0) {
                    handleDelete(Integer.parseInt(guessNumberPlayer1.substring(guessNumberPlayer1.length() - 1)));
                }
                break;
            case R.id.button0:
                flag0 = handleTextViewID(flag0, "0", button0);
                break;
            case R.id.button1:
                flag1 = handleTextViewID(flag1, "1", button1);
                break;
            case R.id.button2:
                flag2 = handleTextViewID(flag2, "2", button2);
                break;
            case R.id.button3:
                flag3 = handleTextViewID(flag3, "3", button3);
                break;
            case R.id.button4:
                flag4 = handleTextViewID(flag4, "4", button4);
                break;
            case R.id.button5:
                flag5 = handleTextViewID(flag5, "5", button5);
                break;
            case R.id.button6:
                flag6 = handleTextViewID(flag6, "6", button6);
                break;
            case R.id.button7:
                flag7 = handleTextViewID(flag7, "7", button7);
                break;
            case R.id.button8:
                flag8 = handleTextViewID(flag8, "8", button8);
                break;
            case R.id.button9:
                flag9 = handleTextViewID(flag9, "9", button9);
                break;
            default:
                break;
        }

        textViewPlayer1.setText(guessNumberPlayer1);

    }

    private int handleTextViewID(int flag, String number, Button button) {
        if ((++flag % 2 == 1) ? true : false) {
            if (guessNumberPlayer1.length() < 4) {
                guessNumberPlayer1 += number;
                button.setTextColor(0xFFA9AAAA);
            } else {
                Toast.makeText(PlayingActivity.this, "輸入數字已完成，請按下OK", Toast.LENGTH_SHORT).show();
            }
        } else {
            button.setTextColor(Color.BLACK);
            guessNumberPlayer1 = guessNumberPlayer1.replace(number, "");
            //Log.d("ID_T", guessNumberPlayer1);
        }
        return flag;
    }

    private void handleDelete(int lastNumber) {
        switch (lastNumber) {
            case 0:
                flag0 = handleTextViewID(flag0, "0", button0);
                break;
            case 1:
                flag1 = handleTextViewID(flag1, "1", button1);
                break;
            case 2:
                flag2 = handleTextViewID(flag2, "2", button2);
                break;
            case 3:
                flag3 = handleTextViewID(flag3, "3", button3);
                break;
            case 4:
                flag4 = handleTextViewID(flag4, "4", button4);
                break;
            case 5:
                flag5 = handleTextViewID(flag5, "5", button5);
                break;
            case 6:
                flag6 = handleTextViewID(flag6, "6", button6);
                break;
            case 7:
                flag7 = handleTextViewID(flag7, "7", button7);
                break;
            case 8:
                flag8 = handleTextViewID(flag8, "8", button8);
                break;
            case 9:
                flag9 = handleTextViewID(flag9, "9", button9);
                break;
            default:
                break;
        }
    }
}