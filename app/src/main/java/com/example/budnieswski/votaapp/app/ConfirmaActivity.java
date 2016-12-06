package com.example.budnieswski.votaapp.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.budnieswski.votaapp.R;
import com.example.budnieswski.votaapp.util.VoteOperations;

public class ConfirmaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirma);

        TextView prefeito = (TextView) findViewById(R.id.prefeito);
        TextView vereador = (TextView) findViewById(R.id.vereador);
        VoteOperations conn = new VoteOperations(this);
        conn.open();

        String prefeitoJSON = conn.getPrefeito(WelcomeActivity.userID);
        String vereadorJSON = conn.getVereador(WelcomeActivity.userID);

        prefeito.setText(prefeitoJSON);
        vereador.setText(vereadorJSON);
    }
}
