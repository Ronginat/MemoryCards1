package afeka.com.memorycards1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameActivityHard extends AppCompatActivity {

    final String TAG = "TheGame";
    ImageButton[] cubes;
    String username = "default name", timerLevel = "";
    int matchCounter = 0, clickedCubeId = -1, level = Constants.level_none, timerLevelNum;
    int numCubes, cubes_per_RowCol;
    View clickedCube;
    TextView textName, timer;
    TableLayout table;
    Integer[] intCubes;
    List<Integer> shuffledCubesIds;

    MyAsyncTask asyncTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG,"e-onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hard);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null) {
            username = (String) b.get(MainActivity.EXTRA_MESSAGE_NAME);
            level = (int) b.get(MainActivity.EXTRA_MESSAGE_LEVEL);
        }

        getMyLevelParameters();
        asyncTask = new MyAsyncTask();

        intCubes = new Integer[numCubes];
        int counter = 1;
        Log.e(TAG,"set intCubes");
        for(int i = 0; i < numCubes - 1; i+=2){
            intCubes[i] = counter;
            intCubes[i+1] = counter;
            counter++;
        }
        Log.e(TAG,"set intCubes last element");
        if(numCubes % 2 == 1)
            intCubes[numCubes - 1] = 0;

        shuffledCubesIds = new ArrayList<>(Arrays.<Integer>asList(intCubes));

        cubes = new ImageButton[numCubes];

        //name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_NAME);
        bindUI();

        Collections.shuffle(shuffledCubesIds);
        populateTable();
    }

    @Override
    protected void onStart() { // or onResume
        super.onStart();
        Log.e(TAG,"e-onStart");

        clickedCube = null;
        clickedCubeId = -1;
        matchCounter = 0;
        timer.setText(timerLevel);

        //for (ImageButton cube: cubes){
            //cube.setTag(0);
            //cube.setOnClickListener(this);
            //cube.setImageResource(R.mipmap.ic_launcher);
            //cube.setEnabled(true);
            //cube.setBackgroundColor(getTitleColor());
        //}
        asyncTask.execute(timerLevelNum); // start timer
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "e-onStop");
        super.onStop();
        // stop timer...
        if(!asyncTask.isCancelled())
            asyncTask.cancel(true);
    }

    private void getMyLevelParameters() {
        switch (level){
            case Constants.level_easy:
                numCubes = Constants.numcubesEasy;
                cubes_per_RowCol = Constants.row_column_Easy;
                timerLevel = Constants.TimeEasyString;
                timerLevelNum = Constants.EasyTime;
                break;
            case Constants.level_medium:
                numCubes = Constants.numcubesMedium;
                cubes_per_RowCol = Constants.row_column_Medium;
                timerLevel = Constants.TImeMediumString;
                timerLevelNum = Constants.MediumTime;
                break;
            case Constants.level_hard:
                numCubes = Constants.numcubesHard;
                cubes_per_RowCol = Constants.row_column_Hard;
                timerLevel = Constants.TimeHardString;
                timerLevelNum = Constants.HardTime;
                break;
            default:
                Toast.makeText(getApplicationContext(), Constants.game_error, Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_FIRST_USER, new Intent());
        }
    }



    private void populateTable() {
        Log.e(TAG,"in populateTable");
        for(int row = 0; row < cubes_per_RowCol; row++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            table.addView(tableRow);

            for(int col = 0; col < cubes_per_RowCol; col++){
                final int position = row * cubes_per_RowCol + col;
                final int FINAL_POSITION = position;
                cubes[position] = new ImageButton(this);
                cubes[position].setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f
                ));
                Log.e(TAG,"init cube["+position+"]");
                Log.e(TAG,"id is " + shuffledCubesIds.get(position));
                setCubeProperties(cubes[position]);

                cubes[position].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG,"cube["+position+"] clicked");
                        checkClickedView(cubes[position], position, shuffledCubesIds.get(position));
                    }
                });
                tableRow.addView(cubes[position]);

            }
        }

        for (ImageButton cube: cubes){
            //cube.setOnClickListener(this);
        }
    }

    private void setCubeProperties(ImageButton cube) {
        cube.setTag(0);
        ViewGroup.LayoutParams params = cube.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;

        cube.setEnabled(true);
        cube.setBackgroundColor(getTitleColor());
        cube.setScaleType(ImageView.ScaleType.FIT_XY);
        cube.setImageResource(R.mipmap.ic_launcher);
        cube.setAdjustViewBounds(true);
        cube.setPadding(0,0,0,0);

        cube.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        cube.setMaxWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        cube.setMinimumHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        cube.setMaxHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        //cube.setMinimumWidth(cube.getMaxWidth());
        //cube.setMaxWidth(cube.getMaxWidth());
        //cube.setMinimumHeight(cube.getMaxHeight());
        //cube.setMaxHeight(cube.getMaxHeight());
    }

    private void bindUI() {
        Log.e(TAG,"in bindUI");
        table = findViewById(R.id.gameHard_buttons_table);
        table.setStretchAllColumns(false);
        table.setShrinkAllColumns(false);
        textName = findViewById(R.id.gameHard_textView_name);
        timer = findViewById(R.id.gameHard_textView_timer);
        textName.setText(String.format("%s: %s","name: " ,username));

        /*
         * Tags:
         * 0 - ic_launcher
         * 1 - nougat
         * 2 - oreo
         * 3 - marshmallow
         *
         * 9 - star
          */

    }


    public void checkClickedView(View view, int position, int viewId)
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
            Log.e(TAG,"same cube twice");
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
            delayBeforeHideCubes(clickedCube, view);
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
            case 0:
                ((ImageButton)cube).setImageResource(R.drawable.app_help_index);
                break;
            case 1:
                ((ImageButton)cube).setImageResource(R.drawable.android_nougat);
                break;
            case 2:
                ((ImageButton)cube).setImageResource(R.drawable.android_oreo);
                break;
            case 3:
                ((ImageButton)cube).setImageResource(R.drawable.androidmarshmallow);
                break;
            case 4:
                ((ImageButton)cube).setImageResource(R.drawable.app_amor);
                break;
            case 5:
                ((ImageButton)cube).setImageResource(R.drawable.app_kcmprocessor);
                break;
            case 6:
                ((ImageButton)cube).setImageResource(R.drawable.app_kcmmidi);
                break;
            case 7:
                ((ImageButton)cube).setImageResource(R.drawable.app_gadu);
                break;
            case 8:
                ((ImageButton)cube).setImageResource(R.drawable.app_web);
                break;
            case 9:
                ((ImageButton)cube).setImageResource(R.drawable.app_pysol);
                break;
            case 10:
                ((ImageButton)cube).setImageResource(R.drawable.app_babelfish);
                break;
            case 11:
                ((ImageButton)cube).setImageResource(R.drawable.app_khelpcenter);
                break;
            case 12:
                ((ImageButton)cube).setImageResource(R.drawable.app_klaptopdaemon);
                break;
        }
        cube.setTag(cubeId);
    }

    private void hideCubes(View alreadyClickedCube, View currentClickedCube){
        ((ImageButton)alreadyClickedCube).setImageResource(R.mipmap.ic_launcher);
        ((ImageButton)currentClickedCube).setImageResource(R.mipmap.ic_launcher);
        alreadyClickedCube.setTag(0);
        currentClickedCube.setTag(0);
    }


    private void starCubes(View alreadyClickedCube, View currentClickedCube){
        alreadyClickedCube.setTag(Constants.starTag);
        currentClickedCube.setTag(Constants.starTag);
    }

    private void FoundAMatch() {
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


    private void delayBeforeHideCubes(final View view1, final View view2){
        Handler hand = new Handler();
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideCubes(view1, view2);
            }
        }, 600);
        //setAllEnabledOrDisabled(false);
        //delay

        //setAllEnabledOrDisabled(true);
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
