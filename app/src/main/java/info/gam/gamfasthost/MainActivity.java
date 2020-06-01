package info.gam.gamfasthost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
//import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static String user_id = null;
    public static String user_name = null;
    public static String fcm_token = null;
    public static int giornipw = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        if (user_id==null) {
            Intent openLogin = new Intent(MainActivity.this, Login.class);
            startActivityForResult(openLogin, 1);
        } else {
            TextView utente = findViewById(R.id.utente);
            utente.setText(user_name);
            TextView utenteid = findViewById( R.id.utenteid );
            utenteid.setText( user_id  );

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }


        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        Button subscribeButton = findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Subscribing to weather topic");
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("weather")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = getString(R.string.msg_subscribed);
                                if (!task.isSuccessful()) {
                                    msg = getString(R.string.msg_subscribe_failed);
                                }

                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                // [END subscribe_topics]
            }

        });


        Button logTokenButton = findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                // [START retrieve_current_token]
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();
                                invia_dati( token, user_id );

                                // Log and toast
                                String msg = getString(R.string.msg_token_fmt, token);
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                // [END retrieve_current_token]
            }
        });


        Button presenzeButton = findViewById(R.id.presenzeButton);
        presenzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openPage2 = new Intent(getApplicationContext(), Page2.class);
                openPage2.putExtra("user_id", user_id);
                openPage2.putExtra("user_name", user_name);
                startActivity(openPage2);
            }
        });
        TextView utenteid = findViewById(R.id.utenteid);

        Button profilo = findViewById(R.id.profilo);
        profilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openPage3 = new Intent(getApplicationContext(), Page3.class);
                openPage3.putExtra("user_id", user_id);
                openPage3.putExtra("user_name", user_name);
                startActivity(openPage3);
            }

        });
        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_id = null;
                finish();
            }

        });
    }
    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String nickmail = data.getStringExtra( "nickmail" );
                String user_name = data.getStringExtra( "user_name" );
                String user_id = data.getStringExtra( "user_id" );
                String fcm_token =  data.getStringExtra( "fcm_token" );
                giornipw = data.getIntExtra( "giornipw", 0 );
                String pw = data.getStringExtra( "pw" );
                this.user_id = user_id;
                this.user_name = user_name;
                this.fcm_token = fcm_token;
                TextView utente = findViewById( R.id.utente );
                utente.setText( user_name  );
                TextView utenteid = findViewById( R.id.utenteid );
                utenteid.setText( user_id  );
                Button logTokenButton = findViewById( R.id.logTokenButton );
                Button subscribeButton = findViewById( R.id.subscribeButton );
                if (!fcm_token.equals(""))  {
                    logTokenButton.setVisibility( GONE );
                    subscribeButton.setVisibility( GONE );
                }
                Log.d( "ResultCode=1 ", user_id + " " + nickmail + " " + user_name + " " + giornipw );
                if (giornipw > 180) {
                    Intent openProfile = new Intent( MainActivity.this, ContactsContract.Profile.class );
                    openProfile.putExtra( "nickmail", nickmail );
                    openProfile.putExtra( "user_id", user_id );
                    openProfile.putExtra( "user_name", user_name );
                    openProfile.putExtra( "giornipw", giornipw );
                    openProfile.putExtra( "pw", pw );

                    startActivityForResult( openProfile, 2 );
                }
            }


            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d( "result code ", "no" );
                finish();
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                String user_name = data.getStringExtra( "user_name" );
                String user_id = data.getStringExtra( "user_id" );
                giornipw = data.getIntExtra( "giornipw", 0 );
                String pw = data.getStringExtra( "pw" );
                this.user_id = user_id;
                this.user_name = user_name;
                Log.d( "ResultCode=2 ", this.user_id + " " + user_name );
                TextView utente = findViewById( R.id.utente );
                utente.setText( user_name );


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d( "result code ", "no" );
                finish();
            }
        }
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {

                Toast.makeText(MainActivity.this, "scelto", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d( "result code ", "no" );
                finish();
            }
        }
    }

    private void invia_dati(String token, String utente){
        RequestQueue queue = Volley.newRequestQueue(this);
        final String URL = "http://80.211.35.77/AJAX/SERV_personale.php?action=write_token&token="+ token +"&uid=" + utente;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                        overridePendingTransition(0,0);
                        startActivity(getIntent());
                        overridePendingTransition(0,0);
                    }
                },
                new Response.ErrorListener() {
                    String body;
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error : "+URL, error.getMessage());
                        if (error.networkResponse == null) {
                            return;
                        }
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        try
                        {
                            body = new String(error.networkResponse.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                        }
                    }
                });

        queue.add(jsonObjectRequest);
    }

}
