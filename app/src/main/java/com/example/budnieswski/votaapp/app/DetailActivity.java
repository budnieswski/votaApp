package com.example.budnieswski.votaapp.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.budnieswski.votaapp.R;
import com.example.budnieswski.votaapp.model.Candidato;
import com.example.budnieswski.votaapp.util.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView nome = (TextView) findViewById(R.id.nome);
        TextView partido = (TextView) findViewById(R.id.partido);
        NetworkImageView foto = (NetworkImageView) findViewById(R.id.foto);

        Intent it = getIntent();
        if (it != null) {
            Bundle params = it.getExtras();
            if (params != null) {
                try {
                    JSONObject data = new JSONObject( params.getString("json") );
                    ImageLoader imageLoader = AppSingleton.getInstance(this).getImageLoader();

                    setTitle(data.getString("nome"));

                    nome.setText(data.getString("nome"));
                    partido.setText(data.getString("partido"));
                    foto.setImageUrl(data.getString("foto"), imageLoader);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
