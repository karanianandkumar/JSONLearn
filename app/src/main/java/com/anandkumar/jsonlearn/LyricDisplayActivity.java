package com.anandkumar.jsonlearn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class LyricDisplayActivity extends AppCompatActivity {

    private TextView lyricDisplyTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric_display);

        String lyric=getIntent().getStringExtra("Lyric");
        lyricDisplyTV=(TextView)findViewById(R.id.lyricTV);
        lyricDisplyTV.setText(lyric);
    }
}
