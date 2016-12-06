package com.example.budnieswski.votaapp.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.budnieswski.votaapp.R;
import com.example.budnieswski.votaapp.model.Candidato;
import com.example.budnieswski.votaapp.util.AppSingleton;
import com.example.budnieswski.votaapp.util.VoteOperations;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView nome = (TextView) findViewById(R.id.nome);
        TextView partido = (TextView) findViewById(R.id.partido);
        TextView json = (TextView) findViewById(R.id.json);
        TextView typeTx = (TextView) findViewById(R.id.type);
        NetworkImageView foto = (NetworkImageView) findViewById(R.id.foto);

        Intent it = getIntent();
        if (it != null) {
            Bundle params = it.getExtras();
            if (params != null) {
                try {
                    JSONObject data = new JSONObject( params.getString("json") );
                    ImageLoader imageLoader = AppSingleton.getInstance(this).getImageLoader();

                    setTitle(data.getString("nome"));

                    typeTx.setText(params.getString("type"));
                    json.setText(params.getString("json"));

                    nome.setText(data.getString("nome"));
                    partido.setText(data.getString("partido"));
                    foto.setImageUrl(data.getString("foto"), imageLoader);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        ((Button) findViewById(R.id.vote)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView json = (TextView) findViewById(R.id.json);
                TextView type = (TextView) findViewById(R.id.type);
                VoteOperations conn = new VoteOperations(DetailActivity.this);
                conn.open();

                switch (type.getText().toString()) {
                    case "prefeito":
                        conn.setPrefeito(WelcomeActivity.userID, json.getText().toString());
                        break;
                    case "vereado":
                        conn.setVereador(WelcomeActivity.userID, json.getText().toString());
                        break;
                }
            }
        });
    }
}
