package com.parse.starter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter implements OnClickListener {
    private Context context;

    private List<Transakcja> listaTransakcji;

    public ListAdapter(Context context, List<Transakcja> listaTransakcji) {
        this.context = context;
        this.listaTransakcji = listaTransakcji;
    }

    public int getCount() {
        return listaTransakcji.size();
    }

    public Object getItem(int position) {
        return listaTransakcji.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {	
        Transakcja transakcja = listaTransakcji.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row, null);
        }
        TextView nazwa = (TextView) convertView.findViewById(R.id.nazwa);
        nazwa.setText(transakcja.getNazwa());

        TextView koszt = (TextView) convertView.findViewById(R.id.koszt);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String formattedValueKoszt = decimalFormat.format(transakcja.getKoszt());
        if (transakcja.getKoszt() <= 0) koszt.setTextColor(context.getResources().getColor(R.color.wydatek));
        if (transakcja.getKoszt() > 0) koszt.setTextColor(context.getResources().getColor(R.color.przychod));
        koszt.setText(formattedValueKoszt);
        
        TextView saldo = (TextView) convertView.findViewById(R.id.saldo);
        Double suma = 0.0;
        for (int i = 0; i <= position; i++) {
        	suma += listaTransakcji.get(i).getKoszt();
        }
        String formattedValueSaldo = decimalFormat.format(suma);
        if (suma <= 0) saldo.setTextColor(context.getResources().getColor(R.color.wydatek));
        if (suma > 0) saldo.setTextColor(context.getResources().getColor(R.color.przychod));
        saldo.setText(formattedValueSaldo);

        return convertView;
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}