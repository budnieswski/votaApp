package com.example.budnieswski.votaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    private Button btnVote;
    private TextView txtNome;
    private TextView txtNumero;
    private TextView txtPartido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        btnVote = (Button) findViewById(R.id.btnVote);
        txtNome = (TextView) findViewById(R.id.txtNome);
        txtNumero = (TextView) findViewById(R.id.txtNumero);
        txtPartido = (TextView) findViewById(R.id.txtPartido);

        this.checkVoteStatus();

        Intent it = getIntent();
        if (it != null) {
            Bundle params = it.getExtras();
            if (params != null) {
                int id = params.getInt("id");

                String[] details = Datastore.getCandidato(id);

                txtNome.setText(details[0]);
                txtNumero.setText(details[1]);
                txtPartido.setText(details[2]);
            }
        }
    }

    public void makeVote(View view) {
        Datastore.setVoteStatus(true);
        Toast.makeText(this, "Voto confirmado com sucesso.", Toast.LENGTH_LONG).show();

        this.checkVoteStatus();
    }

    private void checkVoteStatus() {
        if (Datastore.getVoteStatus()) {
            btnVote.setEnabled(false);
        }
    }
}
