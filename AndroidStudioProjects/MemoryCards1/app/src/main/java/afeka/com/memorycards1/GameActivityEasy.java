package afeka.com.memorycards1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.List;

public class GameActivityEasy extends AppCompatActivity implements View.OnClickListener{

    final String TAG = "GameEasy";
    final int numCubes = 4;
    int matchCounter = 0, clickedCubeId = -1;
    Integer[] intCubes = {1,1,2,2};
    List<Integer> shuffledCubesIds;// = new ArrayList<Integer>(Arrays.<Integer>asList(intCubes));
    ImageButton[] cubes;// = new ImageButton[numCubes];

    View clickedCube;
    TextView textName, timer;
    String username = "default name";
    MyAsyncTask asyncTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG,"e-onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_easy);
        cubes = new ImageButton[numCubes];
        shuffledCubesIds = new ArrayList<>(Arrays.<Integer>asList(intCubes));

        asyncTask = new MyAsyncTask();

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
        timer.setText("30:00");

        for (ImageButton cube: cubes){
            cube.setTag(0);
            cube.setEnabled(true);
            cube.setImageResource(R.mipmap.ic_launcher);
        }

        asyncTask.execute(Constants.EasyTime); // start timer
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "e-onStop");
        super.onStop();
        // stop timer...
        if(!asyncTask.isCancelled())
            asyncTask.cancel(true);
    }

    private void bindUI() {
        textName = findViewById(R.id.gameEasy_textView_name);
        timer = findViewById(R.id.gameEasy_textView_timer);
        textName.setText(String.format("%s: %s","name: " ,username));

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
            ((ImageButton)view).setImageResource(R.mipmap.ic_launcher);
            view.setTag(0);
            clickedCube = null;
            clickedCubeId = -1;
        }
        else if(clickedCubeId == viewId) // found a match
        {
            revealCube(view, viewId);
            setAllEnabledOrDisabled(false);
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
            delay(Constants.delay_for_hide_cubes, clickedCube, view);
            //hideCubes(clickedCube, view);
            setAllEnabledOrDisabled(true);
            clickedCube = null;
            clickedCubeId = -1;
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
        //((ImageButton)alreadyClickedCube).setImageResource(R.drawable.star);
        //((ImageButton)currentClickedCube).setImageResource(R.drawable.star);
        alreadyClickedCube.setTag(Constants.starTag);
        currentClickedCube.setTag(Constants.starTag);
    }

    private void FoundAMatch()
    {
        Log.e(TAG,"e-count+=2");
        matchCounter += 2;
        if(matchCounter == numCubes)
        {
            Log.e(TAG,"found match finish");
            asyncTask.cancel(true);
            Toast.makeText(getApplicationContext(), Constants.game_error, Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK, new Intent());
            Handler hand = new Handler();
            hand.postDelayed(new Runnable() {
                @Override
                public void run() {finish();}
            }, 1000);
            //onStart();
        }
    }

    private void delay(int i, final View view1, final View view2){
        Handler hand = new Handler();
        switch (i){
            case Constants.delay_for_hide_cubes:
                hand.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideCubes(view1, view2);
                    }
                }, 600);
                break;
            case Constants.delay_for_star_cubes:
                hand.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        starCubes(view1, view2);
                    }
                }, 600);
                break;
        }
        //setAllEnabledOrDisabled(false);
        //delay

        //setAllEnabledOrDisabled(true);
    }

    private void setAllEnabledOrDisabled(boolean bool){
        Log.e(TAG,"in setAllEnabled");
        for (ImageButton cube: cubes) {
            if(bool == true)
                if(cube.getTag().equals(Constants.starTag))
                    Log.e(TAG,"a star");
                if(!cube.getTag().equals(Constants.starTag)){
                    Log.e(TAG,"not a star");
                    cube.setEnabled(bool);
                }

            else
                cube.setEnabled(false);
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

    /**
     * sub-class of AsyncTask
     */
    protected class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
        final String Tag = "AsyncTimer";

        @Override
        protected String doInBackground(Integer... params) {
            Log.i(TAG, "doInBackground()");
            // -- on every iteration
            // -- runs a while loop that causes the thread to sleep for 1000 milliseconds
            // -- publishes the progress - calls the onProgressUpdate handler defined below
            // -- and increments the counter variable i by one
            int i = 0;

            while (i < params[0]) {
                if(isCancelled()) {
                    Log.i(Tag, "doInBackground cancelled");
                    return null;
                }
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    Log.i(Tag, e.getMessage());
                }

                if(isCancelled()) {
                    Log.i(Tag, "doInBackground cancelled");
                    return "Cancelled";
                }
                i++;
                publishProgress(i, params[0]);
            }
            try {
                Thread.sleep(1000);
            }

            catch (Exception e) {
                Log.i(Tag, e.getMessage());
            }
            return "COMPLETE!";
        }

        // -- gets called just before thread begins
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute()");
            super.onPreExecute();
        }

        // -- called from the publish progress
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.i(TAG, "onProgressUpdate(): " + String.valueOf(values[0]));
            int timeElapsed = values[0];
            int totalTime = values[1];
            int timeRemaining = totalTime - timeElapsed;
            String timeRemains = timeRemaining + ":00";
            if(timeRemaining < 10)
                timeRemains = "0".concat(timeRemains);

            timer.setText(timeRemains);
        }

        @Override
        protected void onCancelled(){
            Log.e(Tag, "onCancelled");
            super.onCancelled();
            //onDestroy();
            //Toast.makeText(getApplicationContext(), Constants.game_finish, Toast.LENGTH_SHORT).show();
        }
        // -- called as soon as doInBackground method completes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "onPostExecute(): " + result);
            //timer.setText(result);
            Toast.makeText(getApplicationContext(), Constants.game_failed, Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_CANCELED, new Intent());
            finish();
        }
    }
}
