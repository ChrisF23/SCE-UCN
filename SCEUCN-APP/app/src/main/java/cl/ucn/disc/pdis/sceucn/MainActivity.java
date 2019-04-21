package cl.ucn.disc.pdis.sceucn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import cl.ucn.disc.pdis.sceucn.model.Persona;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Persona p = new Persona("1969180K", "Christian Farias");

        Toast.makeText(this, p.toString(), Toast.LENGTH_LONG).show();
    }
}
