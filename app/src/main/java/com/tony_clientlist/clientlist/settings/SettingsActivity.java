package com.tony_clientlist.clientlist.settings;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private ActionBar actionBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null){
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content,new MySettingsFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }

        return true;
    }
}
