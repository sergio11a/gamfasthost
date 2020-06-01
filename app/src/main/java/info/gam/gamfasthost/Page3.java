package info.gam.gamfasthost;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static info.gam.gamfasthost.MainActivity.user_id;


public class Page3 extends Activity {
    String myPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.page3 );


        myPhoneNumber = readCellNo();
        TextView cellulare = findViewById( R.id.cellulare );
        cellulare.setText( myPhoneNumber );
        String URL = String.format( "http://80.211.35.77/AJAX/SERV_personale.php?action=read4app&id=" + user_id );
        RequestQueue queue = Volley.newRequestQueue( this );
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                ( Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            TextView nome = findViewById( R.id.nome );
                            nome.setText( response.getString( "nome" ) );
                            TextView cognome = findViewById( R.id.cognome );
                            cognome.setText( response.getString( "cognome" ) );
                            TextView cellulare = findViewById( R.id.cellulare );
                            cellulare.setText( response.getString( "cellulare" ) );

                            TextView email = findViewById( R.id.email );
                            email.setText( response.getString( "email" ) );

                            Log.d( "function : ", response.getString( "nome" ) );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                        new Response.ErrorListener() {
                            String body;

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d( "Error : ", error.getMessage() );
                                if (error.networkResponse == null) {
                                    return;
                                }
                                final String statusCode = String.valueOf( error.networkResponse.statusCode );
                                try {
                                    body = new String( error.networkResponse.data, "UTF-8" );
                                } catch (UnsupportedEncodingException e) {
                                }

                            }
                        } );
        queue.add( jsonObjectRequest );


        View.OnClickListener gestore = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    case R.id.button_conferma:
                        EditText nome = (EditText) findViewById( R.id.nome );
                        String nn = nome.getText().toString();
                        boolean perc_ok = true;

                        if (nn.equals( "" )) {
                            Toast.makeText( Page3.this, "Nome obbligatorio", Toast.LENGTH_LONG ).show();
                            perc_ok = false;
                        }

                        EditText cognome = (EditText) findViewById( R.id.cognome );
                        nn = cognome.getText().toString();

                        if (nn.equals( "" )) {
                            Toast.makeText( Page3.this, "Cognome obbligatorio", Toast.LENGTH_LONG ).show();
                            perc_ok = false;
                        }

                        EditText cellulare = (EditText) findViewById( R.id.cellulare );
                        nn = cellulare.getText().toString();

                        if (nn.equals( "" )) {
                            Toast.makeText( Page3.this, "Numero cellulare obbligatorio", Toast.LENGTH_LONG ).show();
                            perc_ok = false;
                        }
                        EditText email = (EditText) findViewById( R.id.email );
                        nn = email.getText().toString();

                        if (nn.equals( "" )) {
                            Toast.makeText( Page3.this, "Indirizzo email obbligatorio", Toast.LENGTH_LONG ).show();
                            perc_ok = false;
                        }

                        if (perc_ok) {
                            invia_dati();
                            Toast.makeText( Page3.this, "Dati trasmessi", Toast.LENGTH_LONG ).show();
                            finish();
                        }
                        break;
                    case R.id.button_conferma1:
                        perc_ok = true;
                        EditText npw = (EditText) findViewById( R.id.new_password );
                        String new_password = npw.getText().toString();
                        npw = findViewById( R.id.repeat_password );
                        String repeat_password = npw.getText().toString();
                        if (new_password.equals("")||repeat_password.equals("")) {
                            Toast.makeText( Page3.this, "Prego compilare entrambi i campi", Toast.LENGTH_LONG ).show();
                            perc_ok = false;

                        }
                        if (!new_password.equals(repeat_password)) {
                            Toast.makeText( Page3.this, "Le due password sono diverse", Toast.LENGTH_LONG ).show();
                            perc_ok = false;
                        }
                        if (perc_ok) {
                            cambia_password();
                            Toast.makeText( Page3.this, "Dati trasmessi", Toast.LENGTH_LONG ).show();
                            finish();
                        }
                        break;
                    default:
                        finish();
                }
            }
        };
        Button button_conferma = (Button) findViewById( R.id.button_conferma );
        button_conferma.setOnClickListener( gestore );
        Button button_conferma1 = (Button) findViewById( R.id.button_conferma1 );
        button_conferma1.setOnClickListener( gestore );

    }


    private void invia_dati() {
        EditText nome = findViewById( R.id.nome );
        String visitor_nome = nome.getText().toString();
        EditText cognome =  findViewById( R.id.cognome );
        String visitor_cognome = cognome.getText().toString();
        EditText cellulare =  findViewById( R.id.cellulare );
        String visitor_cellulare = cellulare.getText().toString();
        writeCellNo( visitor_cellulare );
        EditText email = findViewById( R.id.email );
        String visitor_email = email.getText().toString();

        JSONObject postdata = new JSONObject();

        try {
            postdata.put("nome", visitor_nome);
            postdata.put("cognome", visitor_cognome);
            postdata.put("cellulare", visitor_cellulare);
            postdata.put("email", visitor_email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String URL = String.format("http://80.211.35.77/AJAX/SERV_personale.php?action=saveFromApp");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest( Request.Method.POST, URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("OK", response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    String body;

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error : ", error.getMessage());
                        if (error.networkResponse == null) {
                            return;
                        }
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                        }

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                EditText nome = (EditText) findViewById( R.id.nome );
                String visitor_nome = nome.getText().toString();
                EditText cognome = (EditText) findViewById( R.id.cognome );
                String visitor_cognome = cognome.getText().toString();
                EditText cellulare = (EditText) findViewById( R.id.cellulare );
                String visitor_cellulare = cellulare.getText().toString();
                writeCellNo( visitor_cellulare );
                EditText email = (EditText) findViewById( R.id.email );
                String visitor_email = email.getText().toString();

                params.put("nome", visitor_nome);
                params.put("cognome", visitor_cognome);
                params.put("cellulare", visitor_cellulare);
                params.put("email", visitor_email);

                return params;
            }
        };
        queue.add(stringRequest);

    }
    private void cambia_password() {
        EditText npw = findViewById( R.id.new_password );
        String new_password = npw.getText().toString();
        RequestQueue queue = Volley.newRequestQueue( this );
        String URL = String.format( "http://80.211.35.77/AJAX/SERV_personale.php?action=changePwFromApp&id=" + user_id + "&newpw="+new_password);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                ( Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText( Page3.this, "Modifica eseguita", Toast.LENGTH_SHORT ).show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                } );
        queue.add(jsonObjectRequest);
    }
    private void writeCellNo(String cellNo)   {

        SharedPreferences.Editor editor = getSharedPreferences("cellNo", MODE_PRIVATE).edit();
        editor.putString("cellNo", cellNo);
        editor.apply();
    }
    private String readCellNo() {
        SharedPreferences numeroCellulare = getSharedPreferences( "cellNo", MODE_PRIVATE );
        String cellNo = numeroCellulare.getString( "cellNo", null );
        return cellNo;
    }

}

