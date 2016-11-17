package com.Gedi.Devteam.Sfun;


import com.android.volley.Response;

import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andres on 23/06/2016.
 */
public class MapRequest extends StringRequest {
    private static final String MAPS_REQUEST_URL="http://softwarefunerario.com/movil/cobros.json";
    private Map<String,String> params;


    public MapRequest(String id, Response.Listener<String> listener) {
        super(Method.POST, MAPS_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id",id);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
