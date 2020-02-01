package com.metacodersbd.dailybook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.arlib.floatingsearchview.FloatingSearchView;

public class searchJava extends AppCompatActivity {


    FloatingSearchView searchView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_java);

        searchView = findViewById(R.id.floating_search_view) ;


    }
}
