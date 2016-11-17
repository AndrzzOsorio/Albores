package com.Gedi.Devteam.Sfun;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    String id;
    FloatingActionButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        final TextView contador = (TextView) findViewById(R.id.tvcontador);

        refresh = (FloatingActionButton) findViewById(R.id.btnRefrescar);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Cobro> cobros =  new ArrayList<>();

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray JsonResponse = new JSONArray(response);
                            for (int i =0;i<JsonResponse.length();i++){
                                JSONObject row = JsonResponse.getJSONObject(i);

                                    cobros.add(new Cobro(row.getString("id"), row.getString("nombre"), row.getString("documento"), row.getString("direccion"), row.getString("telefono"), row.getString("celular"), row.getString("municipio"), row.getString("valor"), row.getString("estado"), row.getString("email"), row.getString("fechaCobro")));


                            }
                            if (!cobros.isEmpty()){
                                contador.setText("Cobros actuales");
                            setadapter(cobros);}
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder(ListActivity.this);
                                builder.setMessage("Todos sus cobros han sido realizados").setNegativeButton("Continuar",null).create().show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ListRequest listRequest = new ListRequest(id,listener);
                RequestQueue queue = Volley.newRequestQueue(ListActivity.this);
                queue.add(listRequest);
            }
        });
        refresh.performClick();

  }
    private void setadapter(final ArrayList<Cobro> cobros){
         ListView lista =  (ListView)findViewById(R.id.lista);
        Adaptadorcobro<Cobro> adaptador = new Adaptadorcobro<Cobro>(this, cobros);
         lista.setAdapter(adaptador);
         lista.setOnItemClickListener(  new AdapterView.OnItemClickListener() {
         @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Intent intentocobro = new Intent(ListActivity.this, CobroActivity.class);
                 intentocobro.putExtra("id",cobros.get(position).getIdCobro());
                 intentocobro.putExtra("cliente",cobros.get(position).getNombrecliente());
                 intentocobro.putExtra("direccion",cobros.get(position).getDireccioncliente());
                 intentocobro.putExtra("telefono",cobros.get(position).getTelefonocliente());
                 intentocobro.putExtra("monto",cobros.get(position).getCuotacliente());
                 intentocobro.putExtra("ciudad",cobros.get(position).getCiudad());
                 intentocobro.putExtra("documento",cobros.get(position).getDocumento());
                 intentocobro.putExtra("fecha",cobros.get(position).getFecha());
                 intentocobro.putExtra("estado",cobros.get(position).getEstado());
                 ListActivity.this.startActivity(intentocobro);
             }
    });
}
    @Override
    protected void onRestart() {
        super.onRestart();
        refresh.performClick();
    }



}