package com.example.wordical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ToggleButton;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Bundle bundle = getIntent().getExtras();//gets any previous settings
        CheckBox check = (CheckBox) findViewById(R.id.Hints);
        if(bundle != null)//applies any previous settings to the settings screen
        {
            if (bundle.containsKey("Hints"))
                if (bundle.getBoolean("Hints"))
                    check.setChecked(true);
            check = (CheckBox) findViewById(R.id.Dictionary);
            if (bundle.containsKey("Dictionary"))
                if (bundle.getBoolean("Dictionary"))
                    check.setChecked(true);
        }
    }

    public void home(View view)
    {//collects all the information from the settings screen and sends it back to the startscreen as extras
        Intent intent = new Intent(Settings.this, StartScreen.class);
        Bundle bundle = new Bundle();
        CheckBox check = (CheckBox) findViewById(R.id.Hints);
        boolean checked = check.isChecked();
        bundle.putBoolean("Hints", checked);
        check = (CheckBox) findViewById(R.id.Dictionary);
        checked = check.isChecked();
        bundle.putBoolean("Dictionary", checked);
        ToggleButton toggle = (ToggleButton) findViewById(R.id.length);
        char Gamelength = toggle.getText().toString().charAt(0);
        bundle.putChar("GameLength", Gamelength);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}