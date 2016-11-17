package com.Gedi.Devteam.Sfun;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andres on 10/06/2016.
 */
public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL="http://softwarefunerario.com/movil/login.json";
    private Map<String,String> params;

    public LoginRequest(String cedula, String clave, Response.Listener<String> listener){
        super(Method.POST, LOGIN_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put("user",cedula);
        params.put("pass",clave);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
