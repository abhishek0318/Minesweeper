package com.example.android.minesweeper;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.android.minesweeper.R.drawable.flag;

class Minefield {
    private int[][] minefield;
    private boolean[][] flagMinefield;
    private int numberOfRows;
    private int numberOfColumns;
    private float percentageMines;
    private int numberOfMines;
    private int flagCounter;
    private int correctFlagCounter;
    private GameActivity gameActivity;
    private final int UNEXPLORED_SITE = -2;
    private final int MINE = -1;
    private final boolean FLAG = true;
    private final boolean NO_FLAG = false;

    Minefield(int numberOfRows, int numberOfColumns, float percentageMines, GameActivity gameActivity) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.percentageMines = percentageMines;
        this.gameActivity = gameActivity;

        minefield = new int[numberOfRows][numberOfColumns];
        flagMinefield = new boolean[numberOfRows][numberOfColumns];
        initialiseDisplay();
    }

    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                string += (String.valueOf(minefield[i][j]) + " ");
            }
            string += "\n";
        }
        return string;
    }

    private void refreshDisplay() {
        LinearLayout root = (LinearLayout) gameActivity.findViewById(R.id.root);
        for(int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                TextView textView = (TextView) gameActivity.findViewById(row * numberOfColumns + column);
                textView.setText("");
                if (flagMinefield[row][column] == FLAG) {
                    textView.setBackgroundResource(flag);
                }
                else if (minefield[row][column] == UNEXPLORED_SITE || minefield[row][column] == MINE) {
                    textView.setBackgroundResource(R.drawable.unexplored_site);
                }
                else if (minefield[row][column] == 0) {
                    textView.setBackgroundResource(R.drawable.explored_site);
                }
                else {
                    textView.setText(String.valueOf(minefield[row][column]));
                    textView.setBackgroundResource(R.drawable.explored_site);
                }
            }
        }
    }

    private void initialiseMinefiled() {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                minefield[i][j] = UNEXPLORED_SITE;
                flagMinefield[i][j] = false;
            }
        }
        flagCounter = 0;

        numberOfMines = (int) (numberOfRows * numberOfColumns * percentageMines);
        Random random = new Random();
        for (int i = 0; i < numberOfMines;) {
            int x = random.nextInt(numberOfRows), y = random.nextInt(numberOfColumns);
            if (minefield[x][y] == UNEXPLORED_SITE) {
                minefield[x][y] = MINE;
                i++;
            }
        }

    }

    private void updateFlagCounter() {
        TextView flagCounterTV = (TextView) gameActivity.findViewById(R.id.flag_counter);
        flagCounterTV.setText(String.valueOf(flagCounter) + "/" + String.valueOf(numberOfMines));
    }

    private void initialiseDisplay() {
        initialiseMinefiled();
        updateFlagCounter();

        LinearLayout root = (LinearLayout) gameActivity.findViewById(R.id.root);
        for(int row = 0; row < numberOfRows; row++) {
            LinearLayout rowLinearLayout = new LinearLayout(gameActivity);
            rowLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            rowLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int column = 0; column < numberOfColumns; column++) {
                TextView textView = new TextView(gameActivity);
                textView.setId(row * numberOfColumns + column);
                textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                textView.setBackgroundResource(R.drawable.unexplored_site);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(gameActivity.textSize);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!gameActivity.gameOver) {
                            int row = v.getId() / numberOfColumns, column = v.getId() % numberOfColumns;
                            clickedOn(row, column);
                        }
                    }
                });
                textView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (!gameActivity.gameOver) {
                            int row = v.getId() / numberOfColumns, column = v.getId() % numberOfColumns;
                            toggleFlag(row, column);
                            return true;
                        }
                        return true;
                    }
                });
                rowLinearLayout.addView(textView);
            }
            root.addView(rowLinearLayout);
        }
        Display display = gameActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        int screen_height = outMetrics.heightPixels;
        int screen_width = outMetrics.widthPixels;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) root.getLayoutParams();
        layoutParams.height = screen_width;
        layoutParams.width = screen_width;
        root.setLayoutParams(layoutParams);
    }

    private void updateStatus(String status) {
        TextView statusTv = (TextView) gameActivity.findViewById(R.id.status);
        statusTv.setText(status);
    }

    private void clickedOn(int row, int column) {
        if (flagMinefield[row][column] == NO_FLAG) {
            if (minefield[row][column] == MINE) {
                gameActivity.gameOver = true;
                showAllMines();
                TextView clickedMineTV = (TextView) gameActivity.findViewById(row * numberOfColumns + column);
                clickedMineTV.setBackgroundResource(R.drawable.red_mine);
                updateStatus("You lost!");
                Toast.makeText(gameActivity, "You lost the game!", Toast.LENGTH_SHORT).show();
            }
            else if (minefield[row][column] == UNEXPLORED_SITE) {
                openSite(row, column);
                refreshDisplay();
                updateFlagCounter();
            }
        }
    }

    private void toggleFlag(int row, int column) {
        if (flagMinefield[row][column] == FLAG) {
            if (minefield[row][column] == MINE) correctFlagCounter--;
            flagMinefield[row][column] = NO_FLAG;
            flagCounter--;
            updateFlagCounter();
        }
        else if (flagMinefield[row][column] == NO_FLAG && flagCounter < numberOfMines) {
            flagMinefield[row][column] = FLAG;
            flagCounter++;
            updateFlagCounter();
            if (minefield[row][column] == MINE) correctFlagCounter++;
            if (correctFlagCounter == numberOfMines) {
                gameActivity.gameOver = true;
                showAllMines();
                Toast.makeText(gameActivity, "You won the game!", Toast.LENGTH_SHORT).show();
                updateStatus("You won!");
                return;
            }
        }
        refreshDisplay();
    }

    private void openSite(int row, int column) {
        if (flagMinefield[row][column] == FLAG) {
            flagMinefield[row][column] = NO_FLAG;
            flagCounter--;
            updateFlagCounter();
        }
        if (numberOfMinesAdjacent(row, column) != 0) {
            minefield[row][column] = numberOfMinesAdjacent(row, column);
        }
        else {
            minefield[row][column] = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) continue;
                    if ((row + i >= 0 && row + i < numberOfRows) && (column + j >= 0 && column + j < numberOfColumns) && minefield[row + i][column + j] == UNEXPLORED_SITE) {
                        openSite(row + i, column + j);
                    }
                }
            }
        }
    }

    private int numberOfMinesAdjacent(int row, int column) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((row + i >= 0 && row + i < numberOfRows) && (column + j >= 0 && column + j < numberOfColumns) && minefield[row + i][column + j] == MINE) {
                    count++;
                }
            }
        }
        return count;
    }

    private void showAllMines() {
        LinearLayout root = (LinearLayout) gameActivity.findViewById(R.id.root);
        for(int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                TextView textView = (TextView) gameActivity.findViewById(row * numberOfColumns + column);
                if (flagMinefield[row][column] == FLAG) {
                    if (minefield[row][column] == MINE) {
                        textView.setBackgroundResource(R.drawable.correct_flag);
                    }
                    else {
                        textView.setBackgroundResource(R.drawable.incorrect_flag);
                    }
                }
                else if (flagMinefield[row][column] == NO_FLAG) {
                    if (minefield[row][column] == MINE) {
                        textView.setBackgroundResource(R.drawable.mine);
                    }
                }
            }
        }
    }

    void restartGame() {
        gameActivity.gameOver = false;
        updateStatus("");
        initialiseMinefiled();
        refreshDisplay();
    }

}
