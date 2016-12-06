package com.example.budnieswski.votaapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.budnieswski.votaapp.util.AppSingleton;
import com.example.budnieswski.votaapp.R;
import com.example.budnieswski.votaapp.model.Candidato;

import java.util.List;

/**
 * Created by budnieswski on 04/12/16.
 */
public class CandidatosAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Candidato> candidatos;
    ImageLoader imageLoader = AppSingleton.getInstance(activity).getImageLoader();

    public CandidatosAdapter(Activity activity, List<Candidato> candidatos) {
        this.activity = activity;
        this.candidatos = candidatos;
    }

    @Override
    public int getCount() {
        return candidatos.size();
    }

    @Override
    public Object getItem(int location) {
        return candidatos.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item, null);

        if (imageLoader == null)
            imageLoader = AppSingleton.getInstance(activity).getImageLoader();

        TextView nome = (TextView) convertView.findViewById(R.id.textView123);
        TextView partido = (TextView) convertView.findViewById(R.id.partido);
        TextView json = (TextView) convertView.findViewById(R.id.json);
        TextView type = (TextView) convertView.findViewById(R.id.type);
        NetworkImageView foto = (NetworkImageView) convertView.findViewById(R.id.foto);

        Candidato c = candidatos.get(position);

        nome.setText(c.getNome());
        partido.setText(c.getPartido());
        json.setText(c.getJson());
        type.setText(c.getType());
        foto.setImageUrl(c.getFotoURL(), imageLoader);

        return convertView;
    }
}