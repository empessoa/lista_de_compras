package br.com.empessoa8.listamercado.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.empessoa8.listamercado.R;
import br.com.empessoa8.listamercado.entidade.Compras;

/**
 * Created by elias on 04/07/2017.
 */

public class AdapterCompras extends ArrayAdapter<Compras> {
    private int resource = 0;
    private LayoutInflater inflater;

    public AdapterCompras(Context context, int resource){
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            view = inflater.inflate(resource, parent, false);//criando a linha a ser preenchida no ListView
            viewHolder.txt_quantidade = (TextView) view.findViewById(R.id.id_quantidade);
            viewHolder.txt_produtos = (TextView) view.findViewById(R.id.id_produto);
            viewHolder.txt_valores = (TextView) view.findViewById(R.id.id_valores);

            view.setTag(viewHolder);//associando a view, armazenando objeto

            convertView = view;

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        Compras compras = getItem(position);


        viewHolder.txt_quantidade.setText(Integer.toString(compras.getQuantidade()));
        viewHolder.txt_valores.setText(Float.toString(compras.getValor()));
        viewHolder.txt_produtos.setText(compras.getProduto());

        return view;
    }

    static class ViewHolder {

        TextView txt_quantidade;
        TextView txt_produtos;
        TextView txt_valores;

    }
}
