package info.gam.gamfasthost;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Page2 extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    String user_id = null;
    String user_name = null;
    public final int HOST_SELECTION_CODE = 0x3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.page2 );
        esegui();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                String presenza = data.getStringExtra( "presenza" );
                String persona = data.getStringExtra( "persona" );
                trasferisci( presenza,persona );

                Toast.makeText( Page2.this, "scelto "+presenza, Toast.LENGTH_SHORT ).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d( "result code ", "no" );
                finish();
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                accogli( (String) v.getTag() );

                break;
            case R.id.button2:
                respingi( (String) v.getTag());
                break;
            case R.id.button3:
                sceltapersona( (String) v.getTag() );
                break;
            case R.id.button4:
                uscita( (String) v.getTag() );
                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText( this, "Item Click " + position, Toast.LENGTH_SHORT ).show();
    }
    public void esegui() {
        user_id = getIntent().getExtras().getString( "user_id" );
        user_name = getIntent().getExtras().getString( "user_name" );
        TextView utente = findViewById( R.id.utente );
        utente.setText( user_name );
        final List<Presenze> list = new ArrayList<>();

        ListView listview = findViewById( R.id.listPresenze );
        final PresenzeAdapter presenzeAdapter = new PresenzeAdapter( this, R.layout.presenze_list, list ) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView( position, convertView, parent );

                TextView tv =  row.findViewById( R.id.id_presenza );
                String id_presenza = tv.getText().toString();
                View button1 = row.findViewById( R.id.button1 );
                button1.setTag( id_presenza );
                button1.setOnClickListener( Page2.this );

                View button2 = row.findViewById( R.id.button2 );
                button2.setTag( id_presenza );
                button2.setOnClickListener( Page2.this );

                View button3 = row.findViewById( R.id.button3 );
                button3.setTag( id_presenza );
                button3.setOnClickListener( Page2.this );

                View button4 = row.findViewById( R.id.button4 );
                button4.setTag( id_presenza );
                button4.setOnClickListener( Page2.this );

                return row;
            }
        };
        listview.setAdapter( presenzeAdapter );

        listview.setOnItemClickListener( this );

        final String URL = "http://80.211.35.77/AJAX/SERV_visitatori.php?action=getVisiteInCorso&id=" + user_id;

        RequestQueue queue = Volley.newRequestQueue( this );
        JsonArrayRequest arrayRequest = new JsonArrayRequest( Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject( i );

                                Presenze presenze = new Presenze( object.getString( "id_presenza" ),
                                        object.getString( "nominativo_visitatore" ),
                                        object.getString( "azienda" ),
                                        object.getString( "nominativo_dipendente" ),
                                        object.getString( "entrata" ),
                                        object.getString( "stato" ) );
                                list.add( presenze );

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        presenzeAdapter.notifyDataSetChanged();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d( "Error: ", error.getMessage() );
                    }
                } );
        queue.add( arrayRequest );

        View.OnClickListener gestore = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    case R.id.button1:
                        finish();
                        break;
                    default:
                        finish();
                }
            }
        };
        ImageButton button1 = (ImageButton) findViewById( R.id.button1 );
        button1.setOnClickListener( gestore );

    }
    private void accogli(String id_presenza) {

        RequestQueue queue = Volley.newRequestQueue( this );
        String URL = "http://80.211.35.77/AJAX/SERV_visitatori.php?action=checkIn&id="+id_presenza;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                ( Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText( Page2.this, "Item Click ", Toast.LENGTH_SHORT ).show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                } );
        queue.add(jsonObjectRequest);
        esegui();
    }


    private void uscita(String id_presenza) {

        RequestQueue queue = Volley.newRequestQueue( this );
        String URL = "http://80.211.35.77/AJAX/SERV_visitatori.php?action=uscita&id="+id_presenza;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                ( Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText( Page2.this, "Item Click ", Toast.LENGTH_SHORT ).show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                } );
        queue.add(jsonObjectRequest);
        esegui();
    }
    private void respingi(String id_presenza) {

        RequestQueue queue = Volley.newRequestQueue( this );
        String URL = "http://80.211.35.77/AJAX/SERV_visitatori.php?action=respingi&id="+id_presenza;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                ( Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText( Page2.this, "Item Click ", Toast.LENGTH_SHORT ).show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                } );
        queue.add(jsonObjectRequest);
        esegui();
    }
    private void sceltapersona(String id_presenza) {
        Intent openPage4 = new Intent( getApplicationContext(), Page4.class );
        Toast.makeText( Page2.this, "presenza =  "+id_presenza, Toast.LENGTH_SHORT ).show();

        openPage4.putExtra( "presenza", id_presenza );
        startActivityForResult( openPage4, HOST_SELECTION_CODE );
    }
    private void trasferisci(String id_presenza, String id_dipendente) {
        RequestQueue queue = Volley.newRequestQueue( this );
        String URL = "http://80.211.35.77/AJAX/SERV_visitatori.php?action=trasferisci&id="+id_presenza+"&id_dipendente="+id_dipendente+"&user_name="+user_name;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                ( Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText( Page2.this, "Item Click ", Toast.LENGTH_SHORT ).show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                } );
        queue.add(jsonObjectRequest);
        esegui();

    }

}
