package com.example.chasejacobs.eventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    private String categories[] = new String[9];
    private ArrayAdapter<String> adapter;
    private ListView mainList;
    private String categorySelected = new String();
    String[] android_versions = {"Cupcake", "Donut", "Froyo", "Gingerbread", "Honeycomb", "Ice_Cream_Sandwich", "Jelly_Bean", "KitKat", "Lollipop"};

    String[] test = {"Hello",
            "Next"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        spinner = (Spinner) findViewById(R.id.spinnerSearch);
        categories[0] = "Select Category";
        categories[1]= "Sports";
        categories[2] = "Outdoors";
        categories[3] = "Board Games";
        categories[4] = "Parties";
        categories[5] = "Food";
        categories[6] = "Business";
        categories[7] = "Charity";
        categories[8] = "Other";

        mainList = (ListView)findViewById(R.id.listResults);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myText = (TextView) view;
        categorySelected = myText.getText().toString();
        if(!categorySelected.equals("Select Category")) {
            Toast.makeText(this, "You selected " + myText.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onButtonClickHome(View a){
        if (a.getId() == R.id.homeButton){
            Intent i = new Intent (this, MainActivity.class);
            startActivity(i);
        }
    }

    public void unitTestLoadResults(View A){
        mainList = (ListView) findViewById(R.id.listResults);
    }
}
