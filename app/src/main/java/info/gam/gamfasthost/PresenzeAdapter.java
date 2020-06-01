package info.gam.gamfasthost;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PresenzeAdapter extends BaseAdapter {

    private List<Presenze> presenze  = null;
    private Context context = null;
    private SimpleDateFormat simple = new SimpleDateFormat("dd/MM", Locale.ITALIAN);


    public PresenzeAdapter(Context context, List<Presenze> presenze) {
        this.presenze = presenze;
        this.context = context;
    }
    public PresenzeAdapter(Context context, int presenze_list, List presenze) {
        this.presenze = presenze;
        this.context = context;
    }

    @Override
    public int getCount() {
        return presenze.size();
    }

    @Override
    public Object getItem(int position) {
        return presenze.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View v, ViewGroup vg) {

        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.presenze_list, null);
        }
            Presenze presenze = (Presenze) getItem(position);
            String stato_presenza = presenze.getStato_presenza();
            TextView txt = (TextView) v.findViewById(R.id.visitatore);
            txt.setText(presenze.getNominativoVisitatore());
            if (stato_presenza.equals("Accolto")) txt.setBackgroundColor(0xFF00FF00);
            if (stato_presenza.equals("In attesa")) txt.setBackgroundColor( Color.YELLOW);
            txt = v.findViewById( R.id.id_presenza );
            txt.setText(presenze.getId_presenza());
            txt =  v.findViewById(R.id.azienda);
            txt.setText(presenze.getAzienda());
            txt = v.findViewById(R.id.dipendente);
            txt.setText(presenze.getNominativoDipendente());
            txt =  v.findViewById(R.id.ora_entrata);
            txt.setText(presenze.getEntrata());
            if (stato_presenza.equals("Accolto")) {
                v.findViewById( R.id.button1 ).setEnabled( false );
                v.findViewById( R.id.button2 ).setEnabled( false );
                v.findViewById( R.id.button3 ).setEnabled( false );
                v.findViewById( R.id.button4 ).setEnabled( true );
            }
            if (stato_presenza.equals("In attesa")) {
                v.findViewById( R.id.button1 ).setEnabled( true );
                v.findViewById( R.id.button2 ).setEnabled( true );
                v.findViewById( R.id.button3 ).setEnabled( true );
                v.findViewById( R.id.button4 ).setEnabled( false );
            }

            return v;
    }


    private class ViewHolder {
        public TextView name;
        public TextView number;
        public TextView committente;

    }
}
