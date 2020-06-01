package info.gam.gamfasthost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PersoneAdapter extends BaseAdapter {
    private List<Persone> persone  = null;
    private Context context = null;


    public PersoneAdapter(Context context, List<Persone> persone) {
        this.persone = persone;
        this.context = context;
    }
    public PersoneAdapter(Context context, int persone_list, List persone) {
        this.persone = persone;
        this.context = context;
    }

    @Override
    public int getCount() {
        return persone.size();
    }
    @Override
    public Object getItem(int position) {
        return persone.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View v, ViewGroup vg) {

        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.persone_list, null);
        }
        Persone persone = (Persone) getItem(position);
        String nominativo = persone.getNominativoDipendente();
        TextView txt = (TextView) v.findViewById(R.id.nome_dipendente);
        txt.setText( nominativo );
        return v;
    }



}
