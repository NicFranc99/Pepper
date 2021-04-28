package com.example.pepperapp28aprile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_main_persone);
        //setContentView(R.layout.activity_profile);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            System.out.println("una volta");
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        ImageView btn = findViewById(R.id.imageButton);

       fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSettings(view);
            }
        });

    }

    public void startSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}