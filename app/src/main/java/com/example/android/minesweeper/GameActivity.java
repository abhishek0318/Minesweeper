package com.example.android.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static android.R.attr.data;
import static android.R.attr.x;
import static android.R.attr.y;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.example.android.minesweeper.R.id.restart_button;
import static com.example.android.minesweeper.R.id.root;

public class GameActivity extends AppCompatActivity {


    boolean gameOver;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        int numberOfRows = intent.getIntExtra("numberOfRows", 8);
        int numberOfColumns = intent.getIntExtra("numberOfColumns", 8);
        textSize = intent.getIntExtra("textSize", 12);

        gameOver = false;
        final Minefield minefield = new Minefield(numberOfRows, numberOfColumns, 0.16f, this);
        Button restartButton = (Button) findViewById(R.id.restart_button);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minefield.restartGame();
            }
        });
    }
}