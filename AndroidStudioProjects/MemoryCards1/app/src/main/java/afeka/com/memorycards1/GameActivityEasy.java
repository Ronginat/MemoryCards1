package afeka.com.memorycards1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collections;
import java.util.List;

public class GameActivityEasy extends AppCompatActivity {

    final int numCubes = 4;
    Integer[] intCubes = {0,0,1,1};
    List<Integer> shuffledCubes = new ArrayList<Integer>(Arrays.<Integer>asList(intCubes));
    ImageButton[] cubes = new ImageButton[numCubes];
    TextView textName, timer;
    String name = "default name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_easy);

        Collections.shuffle(shuffledCubes);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null)
            name =(String) b.get(MainActivity.EXTRA_MESSAGE_NAME);

        //name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_NAME);
        bindUI();
    }

    private void bindUI() {
        textName = findViewById(R.id.gameEasy_textView_name);
        timer = findViewById(R.id.gameEasy_textView_timer);
        textName.setText("name: " + name);

        cubes[0] = findViewById(R.id.gameEasy_imageButton1);
        cubes[1] = findViewById(R.id.gameEasy_imageButton2);
        cubes[2] = findViewById(R.id.gameEasy_imageButton3);
        cubes[3] = findViewById(R.id.gameEasy_imageButton4);

    }
}
