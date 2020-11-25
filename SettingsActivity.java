package com.example.ninemensmorris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ninemensmorris.View.ViewModel;

import java.util.Optional;

public class SettingsActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    private ViewModel vm;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.color_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        vm = ViewModelProviders.of(this).get(ViewModel.class);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        vm.setBackgroundColor(parent.getItemAtPosition(position).toString());
        Log.d("tag2", vm.getBackgroundColor());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}