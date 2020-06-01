package info.gam.gamfasthost;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

public class Login extends Activity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ImageView imageView = findViewById(R.id.imageView1);
        imageView.setImageResource(R.drawable.smalllogo);

        View.OnClickListener gestore = new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    case R.id.button1:
                        EditText username = findViewById(R.id.username);
                        String nickmail = username.getText().toString();
                        EditText password = findViewById(R.id.password);
                        String pw = password.getText().toString();
                        accesso(nickmail, pw);
                        break;
                    case R.id.forgotpw:
                        username = findViewById(R.id.username);
                        nickmail = username.getText().toString();
                        nuova_password(nickmail);
                        break;
                    default:
                        finish();
                }
            }
        };
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(gestore);
        TextView forgotpw = (TextView) findViewById( R.id.forgotpw );
        forgotpw.setOnClickListener( gestore );

        readLoginPref();
    }

    public void accesso(final String nickmail, String pw)  {
        String URL = "http://80.211.35.77/AJAX/SERV_personale.php?action=loginFromApp&nickmail="+nickmail+"&pw="+pw;
        RequestQueue queue = Volley.newRequestQueue( this );
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                ( Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String user_id = null;
                        String user_name = null;
                        String fcm_token = null;
                        try {
                            user_id = response.getString("id");
                            Log.d("RESPONSE AJAX", user_id);
                            if (user_id.equals("sconosciuto")) {
                                Toast.makeText(Login.this, "Email o password non riconosciute", Toast.LENGTH_LONG).show();
                                return;
                            }
                            user_name = response.getString("nome") + " " + response.getString("cognome");
                            fcm_token = response.getString( "fcm_token" );
                           } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        EditText password = findViewById(R.id.password);
                        String pw = password.getText().toString();

                        Intent openMain = new Intent();
                        openMain.putExtra("nickmail", nickmail);
                        openMain.putExtra("user_id", user_id);
                        openMain.putExtra("user_name", user_name);
                        openMain.putExtra("pw", pw);
                        openMain.putExtra("fcm_token", fcm_token);
                        setResult(Activity.RESULT_OK, openMain);
                        CheckBox rememberme = findViewById(R.id.saveoption );
                        writeLoginPref(rememberme.isChecked());
                        finish();

                    }
                },
                new Response.ErrorListener() {
                    String body;
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error : ", error.getMessage());
                        if (error.networkResponse == null) {
                            Log.d("Msg", "response = nulla "+nickmail);
                            return;
                        }
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        try
                        {
                            body = new String(error.networkResponse.data, "UTF-8");
                            Log.d("Msg", body);
                        } catch (UnsupportedEncodingException e) {
                        }

                    }
                });
        queue.add(jsonObjectRequest);

    }
    public void nuova_password(final String nickmail)  {
        if (nickmail.equals("")) {
            Toast.makeText( Login.this, "Indicare indirizzo email", Toast.LENGTH_SHORT ).show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue( this );
        String URL = "http://80.211.35.77/forgotpassword.php?action=send&email="+nickmail;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                ( Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText( Login.this, "Nuova password inviata", Toast.LENGTH_SHORT ).show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                } );
        queue.add(jsonObjectRequest);
        Toast.makeText( Login.this, "Nuova password inviata", Toast.LENGTH_SHORT ).show();
    }


      void readLoginPref()    {
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        CheckBox rememberme = findViewById(R.id.saveoption );

        SharedPreferences prefs = getSharedPreferences("loginprefs", MODE_PRIVATE);
        String nickmail = prefs.getString("username", null);

        String pw = prefs.getString("password",null);

           if (nickmail != null && pw != null)

        {

            rememberme.setChecked( true );

            username.setText( nickmail );
            password.setText( pw );


        }

        else

            rememberme.setChecked(false);

    }
    void  writeLoginPref(Boolean stato)  {

        EditText username = findViewById(R.id.username);
        String nickmail = username.getText().toString();
        EditText password = findViewById(R.id.password);
        String pw = password.getText().toString();
        CheckBox rememberme = findViewById(R.id.saveoption );

         SharedPreferences.Editor  editor = getSharedPreferences("loginprefs", MODE_PRIVATE).edit();

        if (stato) {

            if (nickmail != "" && pw != "")

            {

                editor.putString("username", nickmail);

                editor.putString("password", pw);

                editor.apply();

            }

            else {
                rememberme.setChecked(false);
            }

        }

        else

        {

            editor.remove("username");

            editor.remove("password");

            editor.apply();

        }

    }
}