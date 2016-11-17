package com.Gedi.Devteam.Sfun;

/**
 * Created by Andres on 13/06/2016.
 */
public class Cobro {
    private String idCobro;
    private String nombrecliente;
    private String direccioncliente;
    private String telefonocliente;
    private String cuotacliente;
    private String ciudad;
    private String barrio;
    private String documento;
    private String estado;
    private String email;
    private String fecha;
    private String movil;



    public Cobro(){

    }
    public Cobro(String idCobro,String nombre,String documento,String direccion,String telefono,String movil,String ciudad,String valor,String estado,String email,String fecha ){
        this.setIdCobro(idCobro);
        this.nombrecliente=nombre;
        this.setDocumento(documento);
        this.direccioncliente=direccion;
        this.telefonocliente=telefono;
        this.ciudad=ciudad;
        this.cuotacliente=valor;
        this.setEstado(estado);
        this.setEmail(email);
        this.setFecha(fecha);
        this.setMovil(movil);
    }




    public String getNombrecliente() {
        return nombrecliente;
    }

    public void setNombrecliente(String nombrecliente) {
        this.nombrecliente = nombrecliente;
    }

    public String getDireccioncliente() {
        return direccioncliente;
    }

    public void setDireccioncliente(String direccioncliente) {
        this.direccioncliente = direccioncliente;
    }

    public String getTelefonocliente() {
        return telefonocliente;
    }

    public void setTelefonocliente(String telefonocliente) {
        this.telefonocliente = telefonocliente;
    }

    public String getCuotacliente() {
        return cuotacliente;
    }

    public void setCuotacliente(String cuotacliente) {
        this.cuotacliente = cuotacliente;
    }

    @Override
    public String toString(){return nombrecliente+","+direccioncliente;}

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getIdCobro() {
        return idCobro;
    }

    public void setIdCobro(String idCobro) {
        this.idCobro = idCobro;
    }
}
