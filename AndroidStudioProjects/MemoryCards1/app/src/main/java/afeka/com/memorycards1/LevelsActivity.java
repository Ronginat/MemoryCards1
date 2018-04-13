package afeka.com.memorycards1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LevelsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //region VARIABLES
    final String TAG = "Levels";
    int itemSelectedCheck = 0;
    String name = "", age = "";
    TextView textName, textAge, textResult;
    Button button;
    Spinner spinnerLevels;
    ArrayAdapter adapter;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null)
        {
            Log.e(TAG,"trying to get name&age");
            name =(String) b.get(MainActivity.EXTRA_MESSAGE_NAME);
            age =(String) b.get(MainActivity.EXTRA_MESSAGE_AGE);
        }
        //name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_NAME);
        //age = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_AGE);
        bindUI();
    }


    private void bindUI() {
        Log.e(TAG,"in bindUI");
        textName = findViewById(R.id.levels_textView_name);
        textAge = findViewById(R.id.levels_textView_age);
        textResult = findViewById(R.id.levels_textView_result);

        textName.setText("name: " + name);
        textAge.setText("age: " + age);
        textResult.setVisibility(View.INVISIBLE);
        spinnerLevels = findViewById(R.id.levels_spinner_levels);
        spinnerLevels.setOnItemSelectedListener(this);

        adapter = ArrayAdapter.createFromResource(this, R.array.spinner_options, android.R.layout.simple_spinner_item);
        Log.i(TAG,"new dataAdapter");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.i(TAG,"setDropDown");
        spinnerLevels.setAdapter(adapter);

        Log.e(TAG,"end bindUI");
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.e(TAG,"in ItemSelected");
        if(false){
            Toast.makeText(getBaseContext(),adapterView.getItemAtPosition(i) + " is selected", Toast.LENGTH_LONG).show();
            itemSelectedCheck++;
        }
        else {
            int level = Constants.level_none;
            String selection = adapterView.getItemAtPosition(i).toString();
            Intent intent = null;
            switch (selection) {
                case "Easy 2x2":
                    level = Constants.level_easy;
                    Log.e(TAG, "Easy");
                    intent = new Intent(this, GameActivity.class);
                    break;
                case "Medium 4x4":
                    level = Constants.level_medium;
                    Log.e(TAG, "Medium");
                    intent = new Intent(this, GameActivity.class);
                    break;
                case "Hard 5x5":
                    level = Constants.level_hard;
                    Log.e(TAG, "Hard");
                    intent = new Intent(this, GameActivity.class);
                    break;
                case "Choose a level":
                    break;
                default:
                    Toast.makeText(getApplicationContext(), Constants.wrong_input_levels_activity, Toast.LENGTH_SHORT).show();

            }

            if (intent != null) {
                Log.e(TAG, "calling intent");
                intent.putExtra(MainActivity.EXTRA_MESSAGE_NAME, name);
                intent.putExtra(MainActivity.EXTRA_MESSAGE_LEVEL, level);
                startActivityForResult(intent, 1);
            }
        }
        Log.e(TAG,"end ItemSelected");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        spinnerLevels.setSelection(0);
        textResult.setVisibility(View.VISIBLE);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                textResult.setText("Well done!");
            }
            else if (resultCode == Activity.RESULT_FIRST_USER) {
                textResult.setText("Maybe next time");
            }
            else if(resultCode == Activity.RESULT_CANCELED)
                textResult.setText("Oops, an error occurred");
        }
    }//onActivityResult
}
