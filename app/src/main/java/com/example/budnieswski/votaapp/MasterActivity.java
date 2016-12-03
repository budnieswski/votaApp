package com.example.budnieswski.votaapp;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by budnieswski on 03/12/16.
 */
public class MasterActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[][] candidatos = Datastore.getCandidatos();

        // Montando um array de String com o candidato <outra linha> partido
        List<String> itens = new ArrayList<String>();
        for (int i = 0; i < candidatos.length; i++)
            itens.add(candidatos[i][0] + "\n" + candidatos[i][1]);

        ArrayAdapter<String> array = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itens);

        setListAdapter(array);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent it = new Intent(this, DetailsActivity .class);
        it.putExtra("id", position);
        startActivity(it);
    }
}
