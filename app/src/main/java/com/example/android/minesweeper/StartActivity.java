package com.example.android.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    }

    public void launchGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        if (view.getId() == R.id.five) {
            intent.putExtra("numberOfRows", 5);
            intent.putExtra("numberOfColumns", 5);
            intent.putExtra("textSize", 32);
        }
        else if (view.getId() == R.id.eight) {
            intent.putExtra("numberOfRows", 8);
            intent.putExtra("numberOfColumns", 8);
            intent.putExtra("textSize", 24);
        }
        else if (view.getId() == R.id.twelve) {
            intent.putExtra("numberOfRows", 12);
            intent.putExtra("numberOfColumns", 12);
            intent.putExtra("textSize", 18);
        }
        else if (view.getId() == R.id.sixteen) {
            intent.putExtra("numberOfRows", 16);
            intent.putExtra("numberOfColumns", 16);
            intent.putExtra("textSize", 12);
        }
        startActivity(intent);
    }

    public void launchAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

}
