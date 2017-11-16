package com.heavener.guessnumbernet;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlayingActivity extends AppCompatActivity {

    private AdView mAdView;

    private Button buttonOK, buttonX;
    //private Button button0, button1, button2, button3, button4,
    //        button5, button6, button7, button8, button9;
    private TextView textViewPlayer1, textViewPlayer2;
    private String guessNumberPlayer1 = "";             // 按扭按出來的數字
    private String guessNumberBoard = "";                    // Player1's Board
    private String numberPlayer1;                       // 使用者設定的數字
    private String numberPlayer2;                       // 對手設定的數字
    //private int flag[0] = 0, flag[1] = 0, flag[2] = 0, flag[3] = 0, flag[4] = 0,
    //        flag[5] = 0, flag[6] = 0, flag[7] = 0, flag[8] = 0, flag[9] = 0;
    private int[] flag = new int[10];
    private Button[] button = new Button[10];
    private String id;
    private String TAG = "debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        // 廣告
        MobileAds.initialize(this, "ca-app-pub-4286420050191609~2435862473");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        buttonOK = (Button) findViewById(R.id.buttonOK);
        buttonX = (Button) findViewById(R.id.buttonX);
        button[0] = (Button) findViewById(R.id.button0);
        button[1] = (Button) findViewById(R.id.button1);
        button[2] = (Button) findViewById(R.id.button2);
        button[3] = (Button) findViewById(R.id.button3);
        button[4] = (Button) findViewById(R.id.button4);
        button[5] = (Button) findViewById(R.id.button5);
        button[6] = (Button) findViewById(R.id.button6);
        button[7] = (Button) findViewById(R.id.button7);
        button[8] = (Button) findViewById(R.id.button8);
        button[9] = (Button) findViewById(R.id.button9);

        for (int i = 0; i <= 9; i++) {
            flag[i] = 0;
        }

        textViewPlayer1 = (TextView) findViewById(R.id.textViewPlayer1);
        textViewPlayer2 = (TextView) findViewById(R.id.textViewPlayer2);

        Intent intent = getIntent();
        numberPlayer1 = intent.getStringExtra("guessNumberPlayer1");
        id = intent.getStringExtra("uuid");

        numberPlayer2 = "8573";

        // Write a message to the database
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child("users").child(id).setValue(numberPlayer1);
        myRef.child("queue").child("2").child("uuid").setValue(id);
        myRef.child("queue").child("2").child("number").setValue(numberPlayer1);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = (String) dataSnapshot.child("queue").child("2").child("uuid").getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    // 按下小鍵盤
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonOK:
                if (guessNumberPlayer1.length() == 4) {
                    guessNumberBoard += handleXAXB(Integer.parseInt(numberPlayer2), Integer.parseInt(guessNumberPlayer1));

                    initialFlagNumberColor();
                } else {
                    Toast.makeText(PlayingActivity.this, "請輸入四位數字", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.buttonX:
                if (guessNumberPlayer1.length() != 0) {
                    handleDelete(Integer.parseInt(guessNumberPlayer1.substring(guessNumberPlayer1.length() - 1)));
                }
                break;
            case R.id.button0:
                flag[0] = handleTextViewID(flag[0], "0", button[0]);
                break;
            case R.id.button1:
                flag[1] = handleTextViewID(flag[1], "1", button[1]);
                break;
            case R.id.button2:
                flag[2] = handleTextViewID(flag[2], "2", button[2]);
                break;
            case R.id.button3:
                flag[3] = handleTextViewID(flag[3], "3", button[3]);
                break;
            case R.id.button4:
                flag[4] = handleTextViewID(flag[4], "4", button[4]);
                break;
            case R.id.button5:
                flag[5] = handleTextViewID(flag[5], "5", button[5]);
                break;
            case R.id.button6:
                flag[6] = handleTextViewID(flag[6], "6", button[6]);
                break;
            case R.id.button7:
                flag[7] = handleTextViewID(flag[7], "7", button[7]);
                break;
            case R.id.button8:
                flag[8] = handleTextViewID(flag[8], "8", button[8]);
                break;
            case R.id.button9:
                flag[9] = handleTextViewID(flag[9], "9", button[9]);
                break;
            default:
                break;
        }

        textViewPlayer1.setText(guessNumberBoard);

    }

    // 按下數字鍵
    // 按第一次顯示數字、文字變灰色
    // 按第二次刪除數字、文字顏色復原
    private int handleTextViewID(int flag, String number, Button buttons) {
        if ((++flag % 2 == 1) ? true : false) {
            if (guessNumberPlayer1.length() < 4) {
                guessNumberPlayer1 += number;
                guessNumberBoard += number;
                buttons.setTextColor(0xFFA9AAAA);
            } else {
                Toast.makeText(PlayingActivity.this, "輸入數字已完成，請按下OK", Toast.LENGTH_SHORT).show();
            }
        } else {
            buttons.setTextColor(Color.BLACK);
            guessNumberPlayer1 = guessNumberPlayer1.replaceFirst(number, "");
            guessNumberBoard = replaceLast(guessNumberBoard, number, "");
        }
        return flag;
    }

    // 按下刪除鍵X
    // 刪除數字、文字顏色復原
    private void handleDelete(int lastNumber) {
        switch (lastNumber) {
            case 0:
                flag[0] = handleTextViewID(flag[0], "0", button[0]);
                break;
            case 1:
                flag[1] = handleTextViewID(flag[1], "1", button[1]);
                break;
            case 2:
                flag[2] = handleTextViewID(flag[2], "2", button[2]);
                break;
            case 3:
                flag[3] = handleTextViewID(flag[3], "3", button[3]);
                break;
            case 4:
                flag[4] = handleTextViewID(flag[4], "4", button[4]);
                break;
            case 5:
                flag[5] = handleTextViewID(flag[5], "5", button[5]);
                break;
            case 6:
                flag[6] = handleTextViewID(flag[6], "6", button[6]);
                break;
            case 7:
                flag[7] = handleTextViewID(flag[7], "7", button[7]);
                break;
            case 8:
                flag[8] = handleTextViewID(flag[8], "8", button[8]);
                break;
            case 9:
                flag[9] = handleTextViewID(flag[9], "9", button[9]);
                break;
            default:
                break;
        }
    }

    // 處理 ?A?B
    private String handleXAXB(int answer, int guess) {
        String xAxB = "";
        int a = 0, b = 0;

        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 3; j++) {
                if ((answer / (int) Math.pow(10, i)) % 10 == (guess / (int) Math.pow(10, j)) % 10) {
                    if (i == j) {
                        a++;
                    } else {
                        b++;
                    }
                }
            }
        }

        xAxB += "  " + a + "A" + b + "B\n";

        return xAxB;
    }

    // initial flag and number's color

    private void initialFlagNumberColor() {
        for (int i = 0; i <= 9; i++) {
            flag[i] = 0;
            button[i].setTextColor(Color.BLACK);
        }
        guessNumberPlayer1 = "";
    }

    // ReplaceLast, code by stackoverflow
    private String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }
}