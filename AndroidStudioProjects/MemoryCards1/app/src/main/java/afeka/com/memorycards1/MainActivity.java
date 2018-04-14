package afeka.com.memorycards1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //region FINALS
    //final String TAG = "Main";
    public static final String EXTRA_MESSAGE_NAME = "afeka.com.memorycards1.NAME";
    public static final String EXTRA_MESSAGE_AGE = "afeka.com.memorycards1.AGE";
    public static final String EXTRA_MESSAGE_LEVEL = "afeka.com.memorycards1.LEVEL";
    //endregion

    //region VARIABLES
    EditText textName, textAge;
    Button button;
    String name = "", age = "";
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindUI();
    }

    private void bindUI() {
        textName = findViewById(R.id.main_editText_name);
        textAge = findViewById(R.id.main_editText_age);
        button = findViewById(R.id.main_button_continue);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(textName.getText().toString().trim().length() > 0 && textAge.getText().toString().trim().length() > 0) {
            name = textName.getText().toString();
            age = textAge.getText().toString();
            int ageInt = Integer.parseInt(age);
            if (ageInt <= 0 || ageInt > 120){
                errorFromOnClick(0);
                return;
            }
            Intent intent = new Intent(this, LevelsActivity.class);
            intent.putExtra(MainActivity.EXTRA_MESSAGE_NAME, name);
            intent.putExtra(MainActivity.EXTRA_MESSAGE_AGE, age);
            startActivity(intent);
        }
        else
        {
            errorFromOnClick(1);
        }

    }

    private void errorFromOnClick(int error){
        switch(error){
            case 0:
                textAge.setError(Constants.main_activity_invalid_age);
                break;
            case 1:
                if(textName.getText().toString().trim().length() == 0 ) {
                    textName.requestFocus();
                    textName.setError(Constants.main_activity_empty_name);
                }
                if(textAge.getText().toString().trim().length() == 0 ) {
                    textAge.requestFocus();
                    textAge.setError(Constants.main_activity_empty_age);
                }
                break;
        }
    }
}
