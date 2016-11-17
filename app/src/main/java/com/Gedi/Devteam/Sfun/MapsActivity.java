package com.Gedi.Devteam.Sfun;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private GoogleMap mMap;
    String id;
    ArrayList<Cobro> flag;
    CameraUpdate center;
    FloatingActionButton consultar;
    GoogleApiClient googleApiClient = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        final TextView textView = (TextView) findViewById(R.id.tvBienvenido);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleApiClient =new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

        getSystemService(MapsActivity.LOCATION_SERVICE);

        flag = new ArrayList<>();
        final Intent intent = getIntent();
        id = intent.getStringExtra("id");
        final String nombre = intent.getStringExtra("nombre");
        consultar = (FloatingActionButton) findViewById(R.id.btnRefrescar);
        //int id = intent.getIntExtra("id_cobrador",0);
        String mensaje = "Bienvenido " + nombre;
        textView.setText(mensaje);
        onStart();


        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ArrayList<Cobro> cobros = new ArrayList<>();

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray JsonResponse = new JSONArray(response);
                            for (int i = 0; i < JsonResponse.length(); i++) {
                                JSONObject row = JsonResponse.getJSONObject(i);
                                cobros.add(new Cobro(row.getString("id"), row.getString("nombre"), row.getString("documento"), row.getString("direccion"), row.getString("telefono"), row.getString("celular"), row.getString("municipio"), row.getString("valor"), row.getString("estado"), row.getString("email"), row.getString("fechaCobro")));
                                // Cobro(String nombre,String documento,String direccion,String telefono,String movil,String ciudad,String valor,int estado,String email,String fecha )
                            }
                            if (!cobros.isEmpty()) {

                                onSearch(cobros);
                                flag = cobros;


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                                builder.setMessage("Señor " + nombre + " usted no posee cobros asignados para el dia de hoy").setNegativeButton("Continuar", null).create().show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                MapRequest mapRequest = new MapRequest(id, listener);
                RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
                queue.add(mapRequest);

            }
        });



        //center = CameraUpdateFactory.newLatLng(new LatLng(intent.getDoubleExtra("lat",0), intent.getDoubleExtra("long",0)));





    }

    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();


    }

    public void onSearch(final ArrayList<Cobro> cobros) {
        for (Cobro c1 : cobros) {
            String localizacion = c1.getCiudad() + "," + c1.getDireccioncliente();
            List<Address> address = null;
            Geocoder geocoder = new Geocoder(this);
            try {
                address = geocoder.getFromLocationName(localizacion, 1);
                while (address == null) {
                    address = geocoder.getFromLocationName(localizacion, 1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address ad = address.get(0);
            LatLng lt = new LatLng(ad.getLatitude(), ad.getLongitude());
            if (c1.getEstado().equals("1")) {
                setMarkerGreen(lt, c1.getNombrecliente(), c1.getDireccioncliente());
            } else {
                setMarkerRed(lt, c1.getNombrecliente(), c1.getDireccioncliente());
            }
        }
    }


    public void onExecute(View view) {

        if (!flag.isEmpty()) {

            Intent intent = new Intent(MapsActivity.this, ListActivity.class);
            intent.putExtra("id", id);
            MapsActivity.this.startActivity(intent);
            onStop();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            builder.setMessage("Debe consultar los cobros antes de efectuar").setNegativeButton("Continuar", null).create().show();
        }
    }


    private void setMarkerGreen(LatLng position, String titulo, String info) {

        Marker myMaker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(titulo)
                .snippet(info)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    private void setMarkerRed(LatLng position, String titulo, String info) {

        Marker myMaker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(titulo)
                .snippet(info)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       /* mMap.moveCamera(center);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
        mMap.animateCamera(zoom);
        consultar.performClick();*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        onStart();
        consultar.performClick();

    }

    /*@Override
    protected void onRestart() {
        super.onRestart();
        consultar.performClick();
    }*/

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        mMap.setMyLocationEnabled(true);
        Location LastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (LastLocation != null) {

            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(LastLocation.getLatitude(), LastLocation.getLongitude()));
            mMap.moveCamera(center);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
            mMap.animateCamera(zoom);


        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
