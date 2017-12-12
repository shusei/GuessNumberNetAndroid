package com.heavener.guessnumbernet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WinActivity extends AppCompatActivity {

    private Button buttonAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        buttonAgain = findViewById(R.id.buttonAgain);
    }

    public void buttonAgain(View view){
        Intent intent = new Intent(WinActivity.this, MainActivity.class);
        startActivity(intent);
        WinActivity.this.finish();
    }
}
