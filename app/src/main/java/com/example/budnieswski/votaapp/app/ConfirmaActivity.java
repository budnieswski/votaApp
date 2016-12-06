package com.example.budnieswski.votaapp.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.budnieswski.votaapp.R;
import com.example.budnieswski.votaapp.util.AppSingleton;
import com.example.budnieswski.votaapp.util.VoteOperations;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirma);

        setTitle("Confirmar Votos");
        Button confirmar = (Button) findViewById(R.id.confirmar);

        VoteOperations conn = new VoteOperations(this);
        conn.open();

        String prefeitoJSON = conn.getPrefeito(WelcomeActivity.userID);
        String vereadorJSON = conn.getVereador(WelcomeActivity.userID);

        if (!prefeitoJSON.isEmpty())
            showPrefeito(prefeitoJSON);

        if (!vereadorJSON.isEmpty())
            showVereador(vereadorJSON);

        // Evitando votar sem ter os 2 candidatos
        if (!prefeitoJSON.isEmpty() && !vereadorJSON.isEmpty())
            confirmar.setEnabled(true);
    }

    private void showPrefeito(String prefeitoJson) {
        try {
            JSONObject data = new JSONObject(prefeitoJson);
            ImageLoader imageLoader = AppSingleton.getInstance(this).getImageLoader();

            TextView nome = (TextView) findViewById(R.id.prefeitoNome);
            TextView partido = (TextView) findViewById(R.id.prefeitoPartido);
            NetworkImageView foto = (NetworkImageView) findViewById(R.id.prefeitoFoto);

            nome.setText(data.getString("nome"));
            partido.setText(data.getString("partido"));
            foto.setImageUrl(data.getString("foto"), imageLoader);

            RelativeLayout layout = (RelativeLayout) findViewById(R.id.wrapPrefeito);
            layout.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showVereador(String vereadorJson) {
        try {
            JSONObject data = new JSONObject(vereadorJson);
            ImageLoader imageLoader = AppSingleton.getInstance(this).getImageLoader();

            TextView nome = (TextView) findViewById(R.id.vereadorNome);
            TextView partido = (TextView) findViewById(R.id.vereadorPartido);
            NetworkImageView foto = (NetworkImageView) findViewById(R.id.vereadorFoto);

            nome.setText(data.getString("nome"));
            partido.setText(data.getString("partido"));
            foto.setImageUrl(data.getString("foto"), imageLoader);

            RelativeLayout layout = (RelativeLayout) findViewById(R.id.wrapVereador);
            layout.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
