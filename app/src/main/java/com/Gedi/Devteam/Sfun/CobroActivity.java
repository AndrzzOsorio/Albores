
package com.Gedi.Devteam.Sfun;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.*;
import com.itextpdf.tool.xml.XMLWorkerHelper;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class CobroActivity extends AppCompatActivity {
    private static final String CARPETA_APP="SfunFacturas";
    String cliente;
    String direccion;
    String telefono;
    String monto;
    String documento;
    String fecha;
    String ciudad;
    String estado;
    String  id;
    String completepath="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cobro);
        final Button cobro = (Button) findViewById(R.id.btncobro);
        final Button factura = (Button) findViewById(R.id.btnFactura);
        final TextView nombrecliente = (TextView) findViewById(R.id.tvCliente);
        final TextView direccioncliente = (TextView) findViewById(R.id.tvDireccion);
        final TextView telefonocliente = (TextView) findViewById(R.id.tvTelefono);
        final TextView montocliente = (TextView) findViewById(R.id.tvMonto);
        final TextView doc = (TextView) findViewById(R.id.tvDocumento);
        final TextView ciu = (TextView) findViewById(R.id.tvCiudad);
        final TextView fec = (TextView) findViewById(R.id.tvFecha);
        factura.setEnabled(false);
        Intent intent = getIntent();
         cliente = intent.getStringExtra("cliente");
         direccion = intent.getStringExtra("direccion");
         telefono = intent.getStringExtra("telefono");
         monto = intent.getStringExtra("monto");
         documento = intent.getStringExtra("documento");
         fecha = intent.getStringExtra("fecha");
         ciudad = intent.getStringExtra("ciudad");
         id = intent.getStringExtra("id");
         estado = intent.getStringExtra("estado");


        nombrecliente.setText(cliente);
        direccioncliente.setText(direccion);
        telefonocliente.setText(telefono);
        montocliente.setText(monto);
        doc.setText(documento);
        ciu.setText(ciudad);
        fec.setText(fecha);
        if(estado.equals("1")){
            cobro.setEnabled(false);
            factura.setEnabled(true);
            Toast.makeText(this,"Este cobro ya ha sido registrado",Toast.LENGTH_LONG).show();
        }

        cobro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> respoStringListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject JsonResponse = new JSONObject(response);
                            boolean success = JsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder=new AlertDialog.Builder(CobroActivity.this);
                                builder.setMessage("Se ha registrado el cobro correctamente").setNegativeButton("Continuar",null).create().show();
                                cobro.setEnabled(false);
                                factura.setEnabled(true);
                                estado = "1";
                                toPDF();


                            }
                            else {
                                AlertDialog.Builder builder=new AlertDialog.Builder(CobroActivity.this);
                                builder.setMessage("Ocurrio un error al registrar el cobro").setNegativeButton("Continuar",null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                };

                CobroRequest cobroRequest = new CobroRequest(id,respoStringListener);
                RequestQueue queue = Volley.newRequestQueue(CobroActivity.this);
                queue.add(cobroRequest);
            }
        });
        factura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarFactura();
            }
        });

    }

    public void toPDF() {
        Document document = new Document(PageSize.NOTE);
        String NOMBRE_ARCHIVO = "Factura_" + documento + ".pdf";
        String SD = Environment.getExternalStorageDirectory().toString();
        File file = new File(SD + File.separator + CARPETA_APP);
        if (!file.exists()) {
            file.mkdir();
        }
        File filesubdir = new File(file.getPath() + File.separator + fecha);
        if (!filesubdir.exists()) {
            filesubdir.mkdir();
        }
        String nombrecompleto = Environment.getExternalStorageDirectory() + File.separator + CARPETA_APP + File.separator + fecha + File.separator + NOMBRE_ARCHIVO;

        File outputfile = new File(nombrecompleto);
        if (outputfile.exists()) {
            outputfile.delete();
        }

        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(nombrecompleto));
            document.open();
            document.addCreator("SfunMov");
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            String HTMLtoPDF = "<html>" +
                    "<head></head>" +
                    "<body>" +
                    "<h1> Factura de venta numero: " + id + "</h1>"+
                    "<p>"+
                    "<table>" +
                    "<tr>" +
                    "<td>Cliente: </td>" +
                    "<td>" + cliente + "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>Documento: </td>" +
                    "<td>" + documento + "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>Direccion: </td>" +
                    "<td>" + direccion + "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>Telefono: </td>" +
                    "<td>" + telefono + "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>Ciudad: </td>" +
                    "<td>" + ciudad + "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>Fecha: </td>" +
                    "<td>" + fecha + "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>Valor cuota: </td>" +
                    "<td>" + monto + "</td>" +
                    "</tr>" +
                    "</table></p>" +
                    "</body>" +
                    "</html>";
            try {
                worker.parseXHtml(pdfWriter, document, new StringReader(HTMLtoPDF));
                document.close();

            } catch (IOException e) {

                e.printStackTrace();
            }


        } catch (DocumentException e) {

            e.printStackTrace();

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
    }
        public void MostrarFactura(){

            Toast.makeText(this,"Leyendo archivo",Toast.LENGTH_LONG).show();
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + CARPETA_APP + File.separator + fecha + File.separator + "Factura_"+documento+".pdf");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),"application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try{
                this.startActivity(intent);
            }catch(ActivityNotFoundException e){
                 Toast.makeText(this,"No posee una app para este tipo de datos",Toast.LENGTH_LONG).show();

            }

    }








}

