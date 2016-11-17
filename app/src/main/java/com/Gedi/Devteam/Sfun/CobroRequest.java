package com.Gedi.Devteam.Sfun;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andres on 23/06/2016.
 */
public class CobroRequest extends StringRequest {
    private static final String COBRO_REQUEST_URL="http://softwarefunerario.com/movil/actualizarpago.json";
    private Map<String,String> params;

    public CobroRequest(String id_cobro, Response.Listener<String> listener){
        super(Method.POST, COBRO_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put("idCuenta",id_cobro);


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
