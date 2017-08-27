package fr.kavalier.von.androloc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class VillesActivity extends AppCompatActivity {

    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villes);
        Intent intent = getIntent();

        final ListView listView = (ListView) findViewById(R.id.list_villes);
        String[] villes = new String[] { "Seattle", "Portland", "San Francisco", "San Jose", "Los Angeles", "San Diego",
                                        "Phoenix", "Denver", "El Paso", "San Antonio", "Austin", "Fort Worth", "Oklahoma City",
                                        "Dallas", "Houston", "Jackson", "Memphis", "Nashville", "Charlotte", "Indianapolis", "Chicago",
                                        "Colombus", "Milwaukee", "Detroit", "Washington", "Baltimore", "Philadelphie", "New York", "Boston"};

        Arrays.sort(villes);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, villes);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();

            }

        });
    }
}
