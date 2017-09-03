package fr.kavalier.von.androloc;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class VillesActivity extends AppCompatActivity {

    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villes);
        Intent intent = getIntent();

        this.setTitle("Villes principales");

        final ListView listView = (ListView) findViewById(R.id.list_villes);

        Ville seattle = new Ville("Seattle", 47.606209,-122.332071);
        Ville portland = new Ville("Portland", 45.523062,-122.676482);
        Ville san_francisco = new Ville("San Fransisco", 37.774929,-122.419416);
        Ville san_jose = new Ville("San Jose", 37.3394444, -121.8938889);
        Ville los_angeles = new Ville("Los Angeles", 34.05223, -118.24368);
        Ville san_diego = new Ville("San Diego", 32.71533, -117.15726);
        Ville phoenix = new Ville("Phoenix", 33.44838, -112.07404);
        Ville denver = new Ville("Denver", 39.73915, -104.9847);
        Ville el_paso = new Ville("El Paso", 31.75872, -106.48693);
        Ville san_antonio = new Ville("San Antonio", 29.42412, -98.49363);
        Ville austin = new Ville("Austin", 30.26715, -97.74306);
        Ville fort_worth = new Ville("Fort Worth", 32.72541, -97.32085);
        Ville oklahoma_city = new Ville("Oklahoma City", 35.467560, -97.516428);
        Ville dallas = new Ville("Dallas", 32.776664, -96.796988);
        Ville houston = new Ville("Houston", 29.760427, -95.369803);
        Ville jackson = new Ville("Jackson", 32.298757,-90.184810);
        Ville memphis = new Ville("Memphis", 35.149534,-90.048980);
        Ville nashville = new Ville("Nashville", 36.162664,-86.781602);
        Ville charlotte = new Ville("Charlotte", 35.227087,-80.843127);
        Ville indianapolis = new Ville("Indianapolis", 39.768403,-86.158068);
        Ville chicago = new Ville("Chicago", 41.878114,-87.629798);
        Ville colombus = new Ville("Colombus", 39.961176,-82.998794);
        Ville milwaukee = new Ville("Milwaukee", 43.038902,-87.906474);
        Ville detroit = new Ville("Detroit", 42.331427,-83.045754);
        Ville washington = new Ville("Washington", 38.907192,-77.036871);
        Ville baltimore = new Ville("Baltimore", 39.290385,-76.612189);
        Ville philadelphie = new Ville("Philadelphie", 39.952584,-75.165222);
        Ville new_york = new Ville("New York", 40.712784,-74.005941);
        Ville boston = new Ville("Boston", 42.360082,-71.058880);

        Ville[] list = new Ville[] { seattle, portland, san_francisco, san_jose, los_angeles, san_diego,
                                        phoenix, denver, el_paso, san_antonio, austin, fort_worth, oklahoma_city,
                                        dallas, houston, jackson, memphis, nashville, charlotte, indianapolis, chicago,
                                        colombus, milwaukee, detroit, washington, baltimore, philadelphie, new_york, boston};

        ArrayList<Ville> villes = new ArrayList<>();

        for (Ville ville: list) {
            villes.add(ville);
        }

        Collections.sort(villes, new Comparator<Ville>(){
            public int compare(Ville obj1, Ville obj2) {
                // ## Ascending order
                return obj1.getNom().compareToIgnoreCase(obj2.getNom()); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(obj2.empId); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(obj1.empId); // To compare integer values
            }
        });

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<Ville> adapter = new ArrayAdapter<>(this,
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
                String  itemValue    =  listView.getItemAtPosition(position).toString();

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

                showAlert();

            }

        });
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.alert_ville_choisie)
                .setPositiveButton(R.string.button_travaux, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent travaux_intent = new Intent(getApplicationContext(), TravauxActivity.class);

                        /*//Create the bundle
                        Bundle bundle = new Bundle();

                        bundle.putString(, );

                        //Add the bundle to the intent
                        travaux_intent.putExtras(bundle);*/

                        //Fire that second activity
                        startActivity(travaux_intent);
                    }
                })
                .setNegativeButton(R.string.button_recherche, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent recherche_intent = new Intent(getApplicationContext(), RechercheActivity.class);
                        startActivity(recherche_intent);
                    }
                });
        dialog.show();
    }
}
