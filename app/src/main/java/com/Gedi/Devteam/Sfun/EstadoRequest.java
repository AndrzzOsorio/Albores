package com.Gedi.Devteam.Sfun;

/**
 * Created by Andres on 29/09/2016.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andres on 10/06/2016.
 */
public class EstadoRequest extends StringRequest {
    private static final String ESTADO_REQUEST_URL="http://192.168.1.10:8080/AlboresWeb/ServiciosURL/consultarEstatoCobroAsignado/";
    private Map<String,String> params;

    public EstadoRequest(String id, Response.Listener<String> listener){
        super(Method.POST, ESTADO_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put("idCobroAsignado",id);


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
