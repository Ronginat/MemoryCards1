package afeka.com.memorycards1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
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
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    //region VARIABLES
    final String TAG = "TheGame";
    ImageButton[] cubes;
    String username = "default name", timerLevel = "";
    int matchCounter = 0, level = Constants.level_none, timerLevelNum;
    int numCubes, cubes_per_RowCol;
    View clickedCube;
    TextView textName, timer;
    TableLayout table;
    List<Integer> allImages, myShuffledImages;

    CountDownTimer downTimer = null;
    MyCountDownTimer myTimer = null;
    int clickedCubePos = -1;
    //endregion

    //region ACTIVITY_OVERRIDES
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG,"e-onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null) {
            username = (String) b.get(MainActivity.EXTRA_MESSAGE_NAME);
            level = (int) b.get(MainActivity.EXTRA_MESSAGE_LEVEL);
        }

        getMyLevelParameters();
        createAllImagesList();

        cubes = new ImageButton[numCubes];

        //name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_NAME);
        bindUI();
        populateTable();
        Log.e(TAG, "after populateTable()");
        createMyShuffledImages(); // and set the cubes tags

        myTimer = new MyCountDownTimer(timerLevelNum, 500);
    }

    @Override
    protected void onStart() { // or onResume
        super.onStart();
        Log.e(TAG,"e-onStart");

        clickedCube = null;
        matchCounter = 0;
        timer.setText(timerLevel);
        myTimer.resume();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "e-onStop");
        super.onStop();
        // stop timer...
        myTimer.pause();
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
                setResult(Activity.RESULT_CANCELED, new Intent());
        }
    }
    //endregion

    //region UI_METHODS
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
                // check if this is an odd number of cubes and this is the last cube
                // if so, hide that cube, because it's not part of the game
                if(row == cubes_per_RowCol - 1 && col == cubes_per_RowCol - 1 && cubes_per_RowCol % 2 == 1) {
                    ImageButton temp = new ImageButton(this);
                    temp.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1.0f
                    ));
                    setCubeProperties(temp);
                    temp.setEnabled(false);
                    temp.setVisibility(View.INVISIBLE);
                    tableRow.addView(temp);
                    break;
                }

                int position = row * cubes_per_RowCol + col;
                final int FINAL_POSITION = position;
                cubes[position] = new ImageButton(this);
                cubes[position].setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f
                ));
                Log.e(TAG,"init cube["+position+"]");
                setCubeProperties(cubes[position]);

                cubes[position].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG,"cube["+FINAL_POSITION+"] clicked");
                        checkClickedView(cubes[FINAL_POSITION], FINAL_POSITION);
                    }
                });
                tableRow.addView(cubes[position]);
            }
        }
    }

    private void setCubeProperties(ImageButton cube) {
        ViewGroup.LayoutParams params = cube.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;

        cube.setEnabled(true);
        cube.setBackgroundColor(getTitleColor());
        cube.setScaleType(ImageView.ScaleType.FIT_XY);
        cube.setImageResource(R.mipmap.ic_launcher);
        cube.setAdjustViewBounds(true);
        cube.setPadding(0,0,0,0);

        cube.setMinimumWidth(params.width);
        cube.setMaxWidth(params.width);
        cube.setMinimumHeight(params.height);
        cube.setMaxHeight(params.height);
    }

    private void bindUI() {
        Log.e(TAG,"in bindUI");
        table = findViewById(R.id.game_buttons_table);
        table.setStretchAllColumns(false);
        table.setShrinkAllColumns(false);
        textName = findViewById(R.id.game_textView_name);
        timer = findViewById(R.id.game_textView_timer);
        textName.setText(String.format("%s: %s","name: " ,username));
    }
    //endregion

    //region GAME_METHODS
    public void checkClickedView(View view, int position){

        MyAsyncTaskDelay asyncTaskDelay;
        if(clickedCube == null) // there isn't a first image revealed from current pair
        {
            Log.e(TAG,"first cube of the pair");
            revealCube(view);
            clickedCube = view;
            clickedCubePos = position;
        }

        else if(clickedCubePos == position){ // same button clicked twice
            Log.e(TAG,"same cube twice");
            ((ImageButton)view).setImageResource(R.mipmap.ic_launcher);
            clickedCube = null;
            clickedCubePos = -1;
        }
        else if(clickedCube.getTag().equals(view.getTag())) // found a match
        {
            Log.e(TAG,"same cubes - match");
            revealCube(view);
            setAllEnabledOrDisabled(false);
            starCubes(clickedCube, view);
            clickedCube = null;
            clickedCubePos = -1;
            setAllEnabledOrDisabled(true);
            FoundAMatch();
        }
        else if(!clickedCube.getTag().equals(view.getTag())) // the two cubes are not a match
        {
            Log.e(TAG,"different cubes - not match");
            revealCube(view);
            asyncTaskDelay = new MyAsyncTaskDelay(clickedCube, view);
            asyncTaskDelay.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            clickedCube = null;
            clickedCubePos = -1;
        }
        else
        {
            Log.e(TAG,"or else");
            //do nothing
        }
    }

    private void revealCube(View cube){
        ((ImageButton)cube).setImageResource(Integer.parseInt(cube.getTag().toString()));
    }

    private void hideCubes(View alreadyClickedCube, View currentClickedCube){
        ((ImageButton)alreadyClickedCube).setImageResource(R.mipmap.ic_launcher);
        ((ImageButton)currentClickedCube).setImageResource(R.mipmap.ic_launcher);
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
            myTimer.cancel();
            Toast.makeText(getApplicationContext(), Constants.game_finish, Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK, new Intent());
            Handler hand = new Handler();
            hand.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
            //onStart();
        }
    }

    private void createMyShuffledImages() {
        Log.e(TAG, "in createMyShuffledImages");
        myShuffledImages = new ArrayList<>();
        for(int i = 0; i < numCubes/2; i++){
            myShuffledImages.add(allImages.get(i));
            myShuffledImages.add(allImages.get(i));
        }
        // shuffle the current list of images
        Collections.shuffle(myShuffledImages);
        //set the Tags of the cubes
        for(int i = 0; i < numCubes; i++){
            cubes[i].setTag(myShuffledImages.get(i));
        }
    }

    private void createAllImagesList() {
        Log.e(TAG, "in createAllImagesList");

        allImages = new ArrayList<>();

        allImages.add(R.drawable.harry_potter_all_guilds);
        allImages.add(R.drawable.cnema_punisher);
        allImages.add(R.drawable.comic_black_panther);
        allImages.add(R.drawable.comic_flash);
        allImages.add(R.drawable.comic_spiderman);
        allImages.add(R.drawable.comics_batman);
        allImages.add(R.drawable.comics_captain_america);
        allImages.add(R.drawable.comics_mask_deadpool);
        allImages.add(R.drawable.comics_superman);
        allImages.add(R.drawable.comics_thor);
        allImages.add(R.drawable.game_of_thrones_hodor);
        allImages.add(R.drawable.game_of_thrones_jon_snow);
        allImages.add(R.drawable.guardians_of_the_galaxy_baby_groot);
        allImages.add(R.drawable.guardians_of_the_galaxy_rocket);
        allImages.add(R.drawable.star_wars_master_joda);
        allImages.add(R.drawable.morty);
        allImages.add(R.drawable.rick_angry);
        allImages.add(R.drawable.pickle_rick);
        allImages.add(R.drawable.star_wars_chewbacca);
        allImages.add(R.drawable.star_wars_jarjar_binks);
        allImages.add(R.drawable.star_wars_java_the_hutt);
        allImages.add(R.drawable.star_wars_r2d2);
        allImages.add(R.drawable.star_wars_c3po);
        allImages.add(R.drawable.star_wars_luke_skywalker);
        allImages.add(R.drawable.harry_potter_all_guilds);
        allImages.add(R.drawable.harry_potter_quidditch);
        allImages.add(R.drawable.harry_potter_dumbledore);
        allImages.add(R.drawable.harry_potter_dobby_house_elf);
        allImages.add(R.drawable.harry_potter_flying_broom);
        allImages.add(R.drawable.harry_potter_magic_hat);

        Collections.shuffle(allImages);
    }

    private void setAllEnabledOrDisabled(boolean bool){
        Log.e(TAG,"in setAllEnabled");
        for (ImageButton cube: cubes) {
            if(bool && !cube.getTag().equals(Constants.starTag)){
                cube.setEnabled(bool);
            }
            else
                cube.setEnabled(false);
        }
        Log.e(TAG,"end setAllEnabled");
    }
    //endregion

    //region TIMER&DELAY
    /**
     * sub-class of AsyncTask
     */
    protected class MyAsyncTaskDelay extends AsyncTask<Void, Void, String> {
        final String Tag = "AsyncDelay";
        View view1, view2;

        public MyAsyncTaskDelay(View alreadyClicked, View currentClicked){
            view1 = alreadyClicked;
            view2 = currentClicked;
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.i(TAG, "doInBackground()");
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                Log.i(Tag, e.getMessage());
                Log.e(TAG, "doInBackground EXCEPTION");
                return null;
            }

            return "COMPLETE!";
        }

        // -- gets called just before thread begins
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "onPreExecute()");
            for(ImageButton cube : cubes){
                cube.setEnabled(false);
            }
            Log.e(TAG, "finish onPreExecute()");
        }

        // -- called from the publish progress
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.i(TAG, "onProgressUpdate(): " + String.valueOf(values[0]));
        }

        // -- called as soon as doInBackground method completes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e(TAG, "onPostExecute(): " + result);
            if(!result.equals(null)) {
                for (ImageButton cube : cubes) {
                    if (!cube.getTag().equals(Constants.starTag))
                        cube.setEnabled(true);
                }

                hideCubes(view1, view2);
            }
        }
    }

    protected class MyCountDownTimer{

        long countDownInterval = 0;
        long millisRemaining =  0;

        private CountDownTimer myTimer = null;
        boolean isPaused = true;

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            this.countDownInterval = countDownInterval;
            this.millisRemaining = millisInFuture;
        }

        public void start(){
            isPaused = false;
            myTimer = new CountDownTimer(millisRemaining, countDownInterval) {
                // timerLevelNum + 1000 -> because i want to show the timer reaches 0 seconds left
                @Override
                public void onTick(long l) {
                    millisRemaining = l;
                    Log.i(TAG, "onTick(): " + String.valueOf(l/1000));
                    long timeRemaining = l / 1000;
                    String timeRemainStr = "" + timeRemaining;
                    if(timeRemaining < 10)
                        timeRemainStr = "0".concat(timeRemainStr);

                    timer.setText(timeRemainStr);
                }

                @Override
                public void onFinish() {
                    Log.i(TAG, "onFinish()");
                    Toast.makeText(getApplicationContext(), Constants.game_failed, Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_FIRST_USER, new Intent());
                    finish();
                }
            }.start();
        }

        public void cancel(){
            if(myTimer!=null){
                myTimer.cancel();
            }
            this.millisRemaining = 0;
        }

        public void resume(){
            if(isPaused)
                this.start();
            isPaused = false;
        }

        public void pause(){
            if(!isPaused)
                myTimer.cancel();
            isPaused = true;
        }
    }
    //endregion
}
