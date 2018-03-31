package afeka.com.memorycards1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LevelsActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener{

    public static final String EXTRA_MESSAGE = "afeka.com.memorycards1.MESSAGE";
    TextView textName, textAge;
    Button button;
    Spinner spinnerLevels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        bindUI();
    }



    private void bindUI()
    {
        textName = (TextView)findViewById(R.id.textViewName);
        textAge = (TextView)findViewById(R.id.textViewAge);
        button = (Button)findViewById(R.id.buttonPlay);
        spinnerLevels = (Spinner)findViewById(R.id.spinnerLevels);
        spinnerLevels.setOnItemSelectedListener(this);
        spinnerLevels.setSelected(false);

        List<String> spinnerList = new ArrayList<String>();
        spinnerList.add("Easy");
        spinnerList.add("Medium");
        spinnerList.add("Hard");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevels.setAdapter(dataAdapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selection = adapterView.getItemAtPosition(i).toString();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_MESSAGE, selection);
        startActivity(intent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
