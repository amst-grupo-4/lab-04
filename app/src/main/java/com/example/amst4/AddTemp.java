package com.example.amst4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddTemp extends AppCompatActivity {
    EditText nuevaTempView;
    String token;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_temp);
        nuevaTempView = (EditText) findViewById(R.id.nuevaTemp);
        Intent login = getIntent();
        this.token = (String) login.getExtras().get("token");
        mQueue = Volley.newRequestQueue(this);
    }

    public void postNewTemp(View view) {
        String nuevaTemp = nuevaTempView.getText().toString();
        doPostRequest(nuevaTemp);

    }

    public void doPostRequest(String temperatura) {
        Map<String, String> params = new HashMap();
        params.put("temperatura", temperatura);
        params.put("humedad","0");
        params.put("peso", "0");
        JSONObject parametros = new JSONObject(params);
        String temp_url = "https://amst-labx.herokuapp.com/api/sensores";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, temp_url, parametros, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    mostrarDialogo("Info","Temperatura anadida exitosamente",true);
                }catch(Exception e){
                  e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              mostrarDialogo("Alerta","Error al ingresar temperatura",false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
               Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "JWT " + token);
                return params;
            }
        };
       mQueue.add(request);
    }

    private void mostrarDialogo(String titulo,String texto,boolean exito){


        AlertDialog alertDialog = new
                AlertDialog.Builder(this).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(texto);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        if(exito){
                            Intent menuPrincipal = new Intent(getBaseContext(), Menu.class);
                            menuPrincipal.putExtra("token",token);
                            startActivity(menuPrincipal);
                        }
                    }
                });
        alertDialog.show();
    }
}