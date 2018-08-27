package com.example.marti.amimedicos;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.marti.amimedicos.ui.LogInUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainAppThemeWithNoBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addDynamicFragment();

    }



    private void addDynamicFragment() {
        // TODO Auto-generated method stub
        // creating instance of the HelloWorldFragment.
        Fragment fg = LogInUI.newInstance();
        //Fragment fg = LogInFragment.newInstance();
        // adding fragment to relative layout by using layout id
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fg).commit();
    }
}
