package com.example.amst4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;

import java.util.Map;

public class AddTemp extends AppCompatActivity {
    EditText nuevaTempView;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_temp);
        nuevaTempView = (EditText) findViewById(R.id.nuevaTemp);
        Intent login = getIntent();
        this.token = (String) login.getExtras().get("token");
    }

    public void postNewTemp(View view) {
        String nuevaTemp = nuevaTempView.getText().toString();
        doPostRequest(nuevaTemp);

    }

    public void doPostRequest(String temperatura) {
        Map<String, String> params = new HashMap();
        params.put("temperatura", temperatura);
        JSONObject parametros = new JSONObject(params);
        String temp_url = "https://amst-labx.herokuapp.com/api/sensores/3";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, temp_url, parametros, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Intent menuPrincipal = new Intent(getBaseContext(), MainActivity.class);
                    menuPrincipal.putExtra("token", token);
                    startActivity(menuPrincipal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(AddTemp.class).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Error al insertar temperatura");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "JWT " + token);
                return params;
            }
        };
    }
}