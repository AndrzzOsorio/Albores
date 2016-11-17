package com.Gedi.Devteam.Sfun;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andres on 26/06/2016.
 */
public class ListRequest extends StringRequest {
    private static final String LIST_REQUEST_URL="http://softwarefunerario.com/movil/cobros.json";
    private Map<String,String> params;


    public ListRequest(String id, Response.Listener<String> listener) {
        super(Method.POST, LIST_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id",id);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

