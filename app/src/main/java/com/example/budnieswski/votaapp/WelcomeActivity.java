package com.example.budnieswski.votaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuPrefeito:
                Toast.makeText(this, "Prefeito", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuVereador:
                Toast.makeText(this, "Vereador", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuConfirmar:
                Toast.makeText(this, "Confirmar", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuSair:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
