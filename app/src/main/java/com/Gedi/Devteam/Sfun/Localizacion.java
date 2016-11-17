package com.Gedi.Devteam.Sfun;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


public class Localizacion implements LocationListener {
    LoginActivity mainActivity;

    public LoginActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(LoginActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onLocationChanged(Location loc) {
        // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
        // debido a la deteccion de un cambio de ubicacion

        loc.getLatitude();
        loc.getLongitude();

        this.mainActivity.setLocation(loc);
    }



    @Override
    public void onProviderDisabled(String provider) {


    }

    @Override
    public void onProviderEnabled(String provider) {
        // Este metodo se ejecuta cuando el GPS es activado

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Este metodo se ejecuta cada vez que se detecta un cambio en el
        // status del proveedor de localizacion (GPS)
        // Los diferentes Status son:
        // OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
        // TEMPORARILY_UNAVAILABLE -> Temporalmente no disponible pero se
        // espera que este disponible en breve
        // AVAILABLE -> Disponible
    }

}/* Fin de la clase localizacion */


