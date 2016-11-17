package com.Gedi.Devteam.Sfun;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;



public class LoginActivity extends AppCompatActivity {
    ProgressDialog progress;
    protected LocationManager locationManager;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    CheckBox credenciales;
    EditText CedulaLogin;
    EditText ClaveLogin;
    Location inicial = null;
    int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(LoginActivity.LOCATION_SERVICE);
        setContentView(R.layout.activity_login);
        credenciales = (CheckBox) findViewById(R.id.cbCredenciales);
        CedulaLogin = (EditText) findViewById(R.id.tfCedulaLogin);
        CedulaLogin.setText("");
        ClaveLogin = (EditText) findViewById(R.id.tfClavelogin);
        ClaveLogin.setText("");
        Button Login = (Button) findViewById(R.id.btnLogin);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            CedulaLogin.setText(loginPreferences.getString("username", ""));
            ClaveLogin.setText(loginPreferences.getString("password", ""));
            credenciales.setChecked(true);
        }
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, Local);



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    try{
                    Double lat=inicial.getLatitude();
                    Double lon=inicial.getLongitude();
                    flag = 1;

                    }

                    catch (Exception e){
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("No se ha podido establecer conexion con el GPS, por favor dirijase a un lugar despejado y espere un momento").setNegativeButton("Continuar", null).create().show();
                        flag = 0;
                    }*/

                    //if(flag == 1){
                    final String cedula = CedulaLogin.getText().toString();
                    final String clave = ClaveLogin.getText().toString();
                    Response.Listener<String> respoStringListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject JsonResponse = new JSONObject(response);

                                boolean success = JsonResponse.getBoolean("success");
                                if (success) {
                                    guardarcredenciales();
                                    String nombre = JsonResponse.getString("nombre");
                                    String id = JsonResponse.getString("id");
                                    final Intent intent = new Intent(LoginActivity.this, MapsActivity.class);

                                    //intent.putExtra("lat",inicial.getLatitude());
                                    //intent.putExtra("long",inicial.getLongitude());
                                    intent.putExtra("nombre", nombre);
                                    intent.putExtra("id", id);
                                    progress = ProgressDialog.show(LoginActivity.this, "Autenticando", "Porfavor espere...", true);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            LoginActivity.this.startActivity(intent);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progress.dismiss();
                                                }
                                            });
                                        }
                                    }).start();

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Inicio de sesion incorrecto").setNegativeButton("Reintentar", null).create().show();
                                    CedulaLogin.setText("");
                                    ClaveLogin.setText("");

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Imposible conectar con el servidor, intente de nuevo mas tarde o pongase en contacto con el administrador").setNegativeButton("Reintentar", null).create().show();
                            }

                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(cedula, clave, respoStringListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);


                  // }
                //}
               /* else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Porfavor, encienda el GPS del equipo").setNegativeButton("Continuar", null).create().show();

                }*/

            }});

    }
    public void guardarcredenciales(){
        InputMethodManager imm = (InputMethodManager)getSystemService(LoginActivity.this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(CedulaLogin.getWindowToken(), 0);

        String cedula = CedulaLogin.getText().toString();
        String clave = ClaveLogin.getText().toString();

        if (credenciales.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", cedula);
            loginPrefsEditor.putString("password", clave);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
    }
    public void setLocation(Location loc){
        this.inicial = loc;
    }


}
