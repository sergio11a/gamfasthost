package info.gam.gamfasthost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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


public class Page4 extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    String user_id = null;
    String presenza = null;
    ListView  listview = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.page4 );
        presenza = getIntent().getExtras().getString( "presenza" );
        esegui();
        listview = findViewById(R.id.persone_list);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
        public void onItemClick(AdapterView<?> adapterc, final View componente, int pos, long id){

             Intent openMain = new Intent(getApplicationContext(),MainActivity.class);
             openMain.setFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
             setResult(Activity.RESULT_OK, openMain);
             Persone p = (Persone) adapterc.getItemAtPosition(pos);
             openMain.putExtra("persona", p.getId());
             openMain.putExtra("presenza", presenza);
             setResult(Activity.RESULT_OK, openMain);
             finish();

         }
        });

      }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                finish();
                break;


            default:
                finish();
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText( this, "Item Click " + position, Toast.LENGTH_SHORT ).show();
    }
    public void esegui() {
        final List<Persone> list = new ArrayList<>();

        ListView listview = findViewById( R.id.persone_list );
        final PersoneAdapter personeAdapter = new PersoneAdapter( this, R.layout.persone_list, list ) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView( position, convertView, parent );

                TextView tv =  row.findViewById( R.id.nome_dipendente );
                String nome_dipendente = tv.getText().toString();



                return row;
            }
        };
        listview.setAdapter( personeAdapter );

        listview.setOnItemClickListener( this );

        final String URL = "http://80.211.35.77/AJAX/SERV_personale.php?action=list";

        RequestQueue queue = Volley.newRequestQueue( this );
        JsonArrayRequest arrayRequest = new JsonArrayRequest( Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject( i );

                                Persone persone = new Persone( object.getString( "id" ), object.getString( "cognome" ) + " "+ object.getString( "nome" ));
                                list.add( persone );

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        personeAdapter.notifyDataSetChanged();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d( "Error SERV_Personale: ", error.getMessage() );
                    }
                } );
        queue.add( arrayRequest );

        View.OnClickListener gestore = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    default:
                        finish();
                }
            }
        };
//        Button button1 =  findViewById( R.id.button1 );
 //       button1.setOnClickListener( gestore );

    }


}
