package afeka.com.memorycards1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivityEasy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_easy);

        Intent intent = getIntent();
        String message = intent.getStringExtra(LevelsActivity.EXTRA_MESSAGE);
    }
}
