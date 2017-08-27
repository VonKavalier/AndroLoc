package fr.kavalier.von.androloc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bouton_quitter = (Button)findViewById(R.id.bouton_quitter);
        bouton_quitter.setOnClickListener(bQuitterListener);

        Button bouton_villes = (Button)findViewById(R.id.bouton_villes);
        bouton_villes.setOnClickListener(bVillesListener);
    }

    private View.OnClickListener bQuitterListener = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener bVillesListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent villes_intent = new Intent(getApplicationContext(), VillesActivity.class);
            startActivity(villes_intent);
        }
    };
}
