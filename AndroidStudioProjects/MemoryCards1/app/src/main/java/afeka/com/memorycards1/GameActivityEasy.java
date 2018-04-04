package afeka.com.memorycards1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameActivityEasy extends AppCompatActivity implements View.OnClickListener{

    final String TAG = "GameEasy";
    final int numCubes = 4;
    int matchCounter = 0, clickedCubeId = -1;
    Integer[] intCubes = {1,1,2,2};
    List<Integer> shuffledCubesIds;// = new ArrayList<Integer>(Arrays.<Integer>asList(intCubes));
    ImageButton[] cubes;// = new ImageButton[numCubes];
    Map<ImageButton, Integer> cubeAndImage;
    View clickedCube;
    TextView textName, timer;
    String username = "default name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG,"e-onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_easy);
        cubes = new ImageButton[numCubes];
        shuffledCubesIds = new ArrayList<Integer>(Arrays.<Integer>asList(intCubes));

        cubeAndImage = new HashMap<>();


        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null)
            username =(String) b.get(MainActivity.EXTRA_MESSAGE_NAME);

        //name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_NAME);
        bindUI();
    }

    @Override
    protected void onStart() { // or onResume
        super.onStart();
        Log.e(TAG,"e-onStart");

        Collections.shuffle(shuffledCubesIds);
        clickedCube = null;
        clickedCubeId = -1;
        matchCounter = 0;

        // start timer...
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"e-onStop");

        // stop timer...
    }

    private void bindUI() {
        textName = findViewById(R.id.gameEasy_textView_name);
        timer = findViewById(R.id.gameEasy_textView_timer);
        textName.setText("name: " + username);

        cubes[0] = findViewById(R.id.gameEasy_imageButton1);
        cubes[1] = findViewById(R.id.gameEasy_imageButton2);
        cubes[2] = findViewById(R.id.gameEasy_imageButton3);
        cubes[3] = findViewById(R.id.gameEasy_imageButton4);

        /*
         * Tags:
         * 0 - ic_launcher
         * 1 - nougat
         * 2 - oreo
         * 3 - marshmallow
         *
         * 9 - star
          */
        for (ImageButton cube: cubes){
            cube.setTag(0);
            cube.setOnClickListener(this);
            cube.setBackgroundColor(getTitleColor());
        }
    }


    public void checkClickedView(View view, int viewId)
    {
        if(clickedCube == null) // there isn't a first image revealed from current pair
        {
            revealCube(view, viewId);
            //view.setBackgroundColor(Color.YELLOW);
            clickedCube = view;
            clickedCubeId = viewId;
        }
        else if(clickedCube.getId() == view.getId()) // same button clicked twice
        {
            //clickedCube.setBackgroundColor(Color.WHITE);
            revealCube(view, viewId);
            setAllEnabledOrDisabled(false);
            delay();
            ((ImageButton)view).setImageResource(R.mipmap.ic_launcher);
            setAllEnabledOrDisabled(true);
            clickedCube = null;
            clickedCubeId = -1;
        }
        else if(clickedCubeId == viewId) // found a match
        {
            revealCube(view, viewId);
            setAllEnabledOrDisabled(false);
            delay();
            starCubes(clickedCube, view);
            clickedCube = null;
            clickedCubeId = -1;
            setAllEnabledOrDisabled(true);
            FoundAMatch();
        }
        else if(clickedCube.getId() != viewId) // the two cubes are not a match
        {
            revealCube(view, viewId);
            setAllEnabledOrDisabled(false);
            delay();
            hideCubes(clickedCube, view);
            setAllEnabledOrDisabled(true);
            clickedCube = null;
        }
        else
        {
            //do nothing

            //clickedCube = view;
        }
    }

    private void revealCube(View cube, int cubeId){
        switch (cubeId){
            case 1:
                ((ImageButton)cube).setImageResource(R.drawable.android_nougat);
                break;
            case 2:
                ((ImageButton)cube).setImageResource(R.drawable.android_oreo);
                break;
        }
        cube.setTag(cubeId);
        //maybe
        //cube.setBackgroundColor(Color.YELLOW);
    }

    private void hideCubes(View alreadyClickedCube, View currentClickedCube){
        ((ImageButton)alreadyClickedCube).setImageResource(R.mipmap.ic_launcher);
        ((ImageButton)currentClickedCube).setImageResource(R.mipmap.ic_launcher);
        alreadyClickedCube.setTag(0);
        currentClickedCube.setTag(0);
    }

    private void starCubes(View alreadyClickedCube, View currentClickedCube){
        ((ImageButton)alreadyClickedCube).setImageResource(R.drawable.star);
        ((ImageButton)currentClickedCube).setImageResource(R.drawable.star);
        alreadyClickedCube.setTag(Strings.starTag);
        currentClickedCube.setTag(Strings.starTag);
        //alreadyClickedCube.setEnabled(false);
        //currentClickedCube.setEnabled(false);
    }

    private void pairCubeToImage(){
        for(int i = 0; i < numCubes; i++){
            cubeAndImage.put(cubes[i], shuffledCubesIds.get(i));
        }
    }


    private void FoundAMatch()
    {
        Log.e(TAG,"e-count+=2");
        matchCounter += 2;
        if(matchCounter == numCubes)
        {
            Toast.makeText(getApplicationContext(),"Game Finished!",Toast.LENGTH_SHORT).show();
            Handler hand = new Handler();
            hand.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //reset.performClick();
                }
            }, 1000);
        }
    }

    private void delay(){
        //setAllEnabledOrDisabled(false);
        //delay
        Handler hand = new Handler();
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                //reset.performClick();
            }
        }, 1000);
        //setAllEnabledOrDisabled(true);
    }

    private void setAllEnabledOrDisabled(boolean bool){
        Log.e(TAG,"in setAllEnabled");
        for (ImageButton cube: cubes) {
            if(bool == true)
                if(cube.getTag().equals(Strings.starTag))
                    Log.e(TAG,"a star");
                if(!cube.getTag().equals(Strings.starTag)){
                    Log.e(TAG,"not a star");
                    cube.setEnabled(bool);
                }

            else
                cube.setEnabled(bool);
        }
        Log.e(TAG,"end setAllEnabled");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gameEasy_imageButton1: {
                checkClickedView(view, shuffledCubesIds.get(0));
                Log.e(TAG,"e-card_gameEasy_imageButton1-onClick");
                break;
            }
            case R.id.gameEasy_imageButton2: {
                checkClickedView(view, shuffledCubesIds.get(1));
                Log.e(TAG,"e-card_gameEasy_imageButton2-onClick");
                break;
            }
            case R.id.gameEasy_imageButton3: {
                checkClickedView(view, shuffledCubesIds.get(2));
                Log.e(TAG,"e-card_gameEasy_imageButton3-onClick");
                break;
            }
            case R.id.gameEasy_imageButton4: {
                checkClickedView(view, shuffledCubesIds.get(3));
                Log.e(TAG,"e-card_gameEasy_imageButton4-onClick");
                break;
            }
            default: {
                break;
            }
        }
    }
}
