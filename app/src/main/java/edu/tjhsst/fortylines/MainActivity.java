package edu.tjhsst.fortylines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mMenuText;
    private Button mPlayButton;
    private Button mLeaderboardButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMenuText = (TextView) findViewById(R.id.menu_text);
        mPlayButton = (Button) findViewById(R.id.play_button);
        mLeaderboardButton = (Button) findViewById(R.id.leaderboard_button);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(MainActivity.this, GameActivity.class);
                //pass any variables in here using .putExtra(), most likely user information
                startActivity(playIntent);
            }
        });

        mLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent leaderboardIntent= new Intent(MainActivity.this, LeaderboardActivity.class);
                //pass any variables in here using .putExtra(), most likely user information + times
                //might using
                startActivity(leaderboardIntent);
            }
        });
    }
}