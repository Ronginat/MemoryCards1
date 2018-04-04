package afeka.com.memorycards1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LevelsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    final String TAG = "Levels";
    int itemSelectedCheck = 0;
    String name = "", age = "";
    TextView textName, textAge;
    Button button;
    Spinner spinnerLevels;
    ArrayAdapter adapter;
    private int options;

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



    private void bindUI()
    {
        Log.e(TAG,"in bindUI");
        textName = findViewById(R.id.levels_textView_name);
        textAge = findViewById(R.id.levels_textView_age);
        button = findViewById(R.id.levels_button_play);

        textName.setText("name: " + name);
        textAge.setText("age: " + age);

        spinnerLevels = findViewById(R.id.levels_spinner_levels);
        Log.e(TAG,"before setOnClick");

        spinnerLevels.setOnItemSelectedListener(this);
        Log.e(TAG,"after setOnClick");
        //spinnerLevels.setSelected(false);

        //List<String> spinnerList = new ArrayList<>();
        //spinnerList.add("");
        //spinnerList.add("Easy");
        //spinnerList.add("Medium");
        //spinnerList.add("Hard");


        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
          //      android.R.layout.simple_spinner_item, spinnerList);
        adapter = ArrayAdapter.createFromResource(this, R.array.spinner_options, android.R.layout.simple_spinner_item);
        Log.e(TAG,"new dataAdapter");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.e(TAG,"setDropDown");
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
            String selection = adapterView.getItemAtPosition(i).toString();
            Intent intent = null;
            switch (selection) {
                case "Easy 2x2":
                    Log.e(TAG, "Easy");
                    intent = new Intent(this, GameActivityEasy.class);
                    break;
                case "Medium 4x4":
                    Log.e(TAG, "Medium");
                    //intent = new Intent(this, GameActivityMedium.class);
                    break;
                case "Hard 8x8":
                    Log.e(TAG, "Hard");
                    //intent = new Intent(this, GameActivityHard.class);
                    break;
                case "Choose a level":
                    break;
                default:
                    Toast.makeText(getApplicationContext(), Strings.wrong_input_levels_activity, Toast.LENGTH_SHORT).show();

            }

            if (intent != null) {
                Log.e(TAG, "calling intent");
                intent.putExtra(MainActivity.EXTRA_MESSAGE_NAME, name);
                startActivity(intent);
            }
        }
        Log.e(TAG,"end ItemSelected");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
