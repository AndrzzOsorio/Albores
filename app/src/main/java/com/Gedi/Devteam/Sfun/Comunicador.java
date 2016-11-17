package com.Gedi.Devteam.Sfun;

/**
 * Created by Andres on 23/06/2016.
 */
public class Comunicador {
    private static Object objeto = null;

    public static void setObjeto(Object newObjeto) {
        objeto = newObjeto;
    }

    public static Object getObjeto() {
        return objeto;
    }
}
