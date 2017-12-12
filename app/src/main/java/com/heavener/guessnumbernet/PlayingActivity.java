package com.heavener.guessnumbernet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class PlayingActivity extends AppCompatActivity {

    private AdView mAdView;

    private Button buttonOK, buttonX;
    private TextView textViewPlayer1, textViewPlayer2;
    private String guessNumberPlayer1 = "";             // 按扭按出來的數字
    private String guessNumberBoard = "";               // Player1's Board
    private String numberPlayer1;                       // 使用者設定的數字
    private String numberPlayer2;                       // 對手設定的數字
    private int[] flag = new int[10];
    private Button[] button = new Button[10];
    private String id;
    private String TAG = "GuessNumberDebug";
    private DatabaseReference myRef;
    private String tempUuid, tempNumber, flagRoom;
    private String roomId;
    private boolean runOnce1 = true, runOnce2 = true, runOnce3 = true;
    private boolean isWaiting = false;
    private boolean afterBulidRoom = false;
    private ProgressDialog progressDialog;
    private String player = "";                           // is player1 or player2
    private String bord2ShowPlayer = "";                  // player2Bord is p1 or p2
    private ScrollView scrollView1, scrollView2;

    // Firebase's player1: queue
    // Firebase's player2: build room

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        // 廣告
        MobileAds.initialize(this, "ca-app-pub-4286420050191609~2435862473");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        buttonOK = findViewById(R.id.buttonOK);
        buttonX = findViewById(R.id.buttonX);
        button[0] = findViewById(R.id.button0);
        button[1] = findViewById(R.id.button1);
        button[2] = findViewById(R.id.button2);
        button[3] = findViewById(R.id.button3);
        button[4] = findViewById(R.id.button4);
        button[5] = findViewById(R.id.button5);
        button[6] = findViewById(R.id.button6);
        button[7] = findViewById(R.id.button7);
        button[8] = findViewById(R.id.button8);
        button[9] = findViewById(R.id.button9);

        for (int i = 0; i <= 9; i++) {
            flag[i] = 0;
        }

        textViewPlayer1 = findViewById(R.id.textViewPlayer1);
        textViewPlayer2 = findViewById(R.id.textViewPlayer2);
        textViewPlayer1.setMovementMethod(new ScrollingMovementMethod());
        textViewPlayer2.setMovementMethod(new ScrollingMovementMethod());

        scrollView1 = findViewById(R.id.scrollView1);
        scrollView2 = findViewById(R.id.scrollView2);

        Intent intent = getIntent();
        numberPlayer1 = intent.getStringExtra("guessNumberPlayer1");
        id = intent.getStringExtra("uuid");

        //numberPlayer2 = "8573";

        Log.d(TAG, "initial data");
        runOnce1 = true;
        runOnce2 = true;
        runOnce3 = true;
        isWaiting = false;
        afterBulidRoom = false;
        player = "";
        bord2ShowPlayer = "";
        roomId = "";


        // Write a message to the database
        myRef = FirebaseDatabase.getInstance().getReference();

        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                myRef.child("queue").child("number").runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        //numberPlayer2 = (String) mutableData.getValue();
                        //Log.d(TAG, "Number is: " + numberPlayer2);

                        if (runOnce2) {
                            // Enter queue
                            if (mutableData.getValue().equals("0")) {
                                Log.d(TAG, "Enter queue");
                                myRef.child("queue").child("uuid").setValue(id);
                                myRef.child("queue").child("number").setValue(numberPlayer1);
                                isWaiting = true;
                            } else {
                                isWaiting = false;
                                tempUuid = (String) dataSnapshot.child("queue").child("uuid").getValue();
                                tempNumber = (String) dataSnapshot.child("queue").child("number").getValue();
                                flagRoom = (String) dataSnapshot.child("room").child("flag").getValue();

                                myRef.child("queue").child("uuid").setValue("0");
                                myRef.child("queue").child("number").setValue("0");

                                Log.d(TAG, "tempUuid:" + tempUuid + ", tempNumber:" + tempNumber + ", flagRoom:" + flagRoom);

                                if (runOnce1) {
                                    player = "iAmPlayer2";
                                    buildRoom(tempUuid, tempNumber, guessNumberBoard, id, numberPlayer1, flagRoom);
                                }
                            }

                            runOnce2 = false;
                        }
                        return null;
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });

        Log.d(TAG, "isWaiting=" + isWaiting);

        // player stay on queue, if room build then get roomId
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (isWaiting) {

                    if (runOnce3) {
                        progressDialog = ProgressDialog.show(PlayingActivity.this, getResources().getString(R.string.progressDialogTitle), getResources().getString(R.string.progressDialogMessage));
                        runOnce3 = false;
                    }

                    myRef.child("queue").child("number").runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            if (mutableData.getValue().equals("0")) {
                                player = "iAmPlayer1";
                                isWaiting = false;
                                progressDialog.dismiss();
                                roomId = (String) dataSnapshot.child("room").child("flag").getValue();
                                //numberPlayer2 = (String) dataSnapshot.child("room").child(roomId).child("player2").child("number").getValue();
                                dataSnapshot.child("room").child(roomId).child("player2").child("number").getRef().addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            numberPlayer2 = dataSnapshot.getValue().toString();
                                            Log.d(TAG, "numberPlayer2: " + numberPlayer2);
                                            afterBulidRoom = true;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Log.d(TAG, "Exit queue enter room: " + roomId + ", numberPlayer2: " + numberPlayer2);
                            } else {
                                Log.d(TAG, "value: " + mutableData.getValue());
                            }
                            return null;
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        // if game finish then go to WinActivity
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (afterBulidRoom) {
                    Log.d(TAG, "Begin to listen 'finish:playerX'");
                    if (dataSnapshot.child("room").child(roomId).child("finish").getValue() != null) {
                        if (dataSnapshot.child("room").child(roomId).child("finish").getValue().equals("player1")) {
                            if (player.equals("iAmPlayer1")) {
                                Intent intentWin = new Intent(PlayingActivity.this, WinActivity.class);
                                startActivity(intentWin);
                                myRef.child("room").child(roomId).removeValue();
                                PlayingActivity.this.finish();
                            } else if (player.equals("iAmPlayer2")) {
                                Intent intentLose = new Intent(PlayingActivity.this, LoseActivity.class);
                                startActivity(intentLose);
                                myRef.child("room").child(roomId).removeValue();
                                PlayingActivity.this.finish();
                            }
                        } else if (dataSnapshot.child("room").child(roomId).child("finish").getValue().equals("player2")) {
                            if (player.equals("iAmPlayer2")) {
                                Intent intentWin = new Intent(PlayingActivity.this, WinActivity.class);
                                startActivity(intentWin);
                                myRef.child("room").child(roomId).removeValue();
                                PlayingActivity.this.finish();
                            } else if (player.equals("iAmPlayer1")) {
                                Intent intentLose = new Intent(PlayingActivity.this, LoseActivity.class);
                                startActivity(intentLose);
                                myRef.child("room").child(roomId).removeValue();
                                PlayingActivity.this.finish();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // 按下小鍵盤
    public void onClick(View v) {

        scrollView1.post(new Runnable() {
            @Override
            public void run() {
                scrollView1.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        scrollView2.post(new Runnable() {
            @Override
            public void run() {
                scrollView1.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        switch (v.getId()) {
            case R.id.buttonOK:
                Log.d(TAG, "numberPlayer2=" + numberPlayer2 + " guessNumberPlayer1=" + guessNumberPlayer1);
                if(numberPlayer2==null){
                    myRef.child("room").child(roomId).child("player2").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            numberPlayer2 = dataSnapshot.getValue().toString();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                if (guessNumberPlayer1.length() == 4) {
                    guessNumberBoard += handleXAXB(Integer.parseInt(numberPlayer2), Integer.parseInt(guessNumberPlayer1));

                    initialFlagNumberColor();

                    Log.d(TAG, "player=" + player);
                    if (player.equals("iAmPlayer1")) {
                        myRef.child("room").child(roomId).child("player1").child("board").setValue(guessNumberBoard);
                    } else {
                        myRef.child("room").child(roomId).child("player2").child("board").setValue(guessNumberBoard);
                    }
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

        // textViewPlayer1,2 setText
        textViewPlayer1.setText(guessNumberBoard);
        Log.d(TAG, "roomId" + roomId);
        if (player.equals("iAmPlayer1")) {
            bord2ShowPlayer = "player2";
        } else {
            bord2ShowPlayer = "player1";
        }
        myRef.child("room").child(roomId).child(bord2ShowPlayer).child("board").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textViewPlayer2.setText((String) dataSnapshot.getValue());
                //Log.d(TAG, "(String) dataSnapshot.getValue()" + (String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    // 按下數字鍵
    // 按第一次顯示數字、文字變灰色
    // 按第二次刪除數字、文字顏色復原
    private int handleTextViewID(int flag, String number, Button buttons) {
        if (++flag % 2 == 1) {
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

        if (a == 4) {
            if (player.equals("iAmPlayer1")) {
                myRef.child("room").child(roomId).child("finish").setValue("player1");
            } else {
                myRef.child("room").child(roomId).child("finish").setValue("player2");
            }
        }

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

    // Build room
    private void buildRoom(String roomIdPlayer1, String roomNumberPlayer1, String roomBoardPlayer1, String roomIdPlayer2, String roomNumberPlayer2, String flagRoom) {

        if (flagRoom == null) return;

        if (Integer.parseInt(flagRoom) > 200) {
            myRef.child("room").child("flag").setValue(0);
        } else {
            flagRoom = String.valueOf(Integer.parseInt(flagRoom) + 1);
        }

        roomId = flagRoom;
        myRef.child("room").child("flag").setValue(flagRoom);

        myRef.child("room").child(roomId).child("player1").child("number").setValue(roomNumberPlayer1);
        myRef.child("room").child(roomId).child("player1").child("board").setValue(roomBoardPlayer1);
        myRef.child("room").child(roomId).child("player1").child("uuid").setValue(roomIdPlayer1);
        myRef.child("room").child(roomId).child("player2").child("number").setValue(roomNumberPlayer2);
        myRef.child("room").child(roomId).child("player2").child("uuid").setValue(roomIdPlayer2);
        myRef.child("room").child(roomId).child("finish").setValue("null");

        runOnce2 = false;

        Log.d(TAG, "numberPlayer2=" + numberPlayer2);
        numberPlayer2 = roomNumberPlayer1;

        afterBulidRoom = true;
    }
}