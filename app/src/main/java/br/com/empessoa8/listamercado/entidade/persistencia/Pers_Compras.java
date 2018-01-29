package br.com.empessoa8.listamercado.entidade.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.empessoa8.listamercado.adapter.AdapterCompras;
import br.com.empessoa8.listamercado.R;
import br.com.empessoa8.listamercado.entidade.Compras;

/**
 * Created by elias on 04/07/2017.
 */

public class Pers_Compras {

    SQLiteDatabase conn;

    public Pers_Compras(SQLiteDatabase conn) {
        this.conn = conn;
    }

    private ContentValues preencherTabelaCompras(Compras compras) {
        ContentValues values = new ContentValues();
        values.put("quantidade", compras.getQuantidade());
        values.put("produto", compras.getProduto());
        values.put("valor", compras.getValor());
        return values;
    }

    public void inserirCompras(Compras compras) {
        ContentValues values = preencherTabelaCompras(compras);
        conn.insertOrThrow("COMPRAS", null, values);
    }

    public void excluirCompras(int _id) {
        conn.delete("COMPRAS", "_id = ?", new String[]{String.valueOf(_id)});
    }


    public AdapterCompras buscarCompras(Context context) {
        AdapterCompras adapterCompras = new AdapterCompras(
                context,
                R.layout.list_compras
        );
        Cursor cursor = conn.rawQuery("SELECT * FROM COMPRAS ORDER BY _id DESC", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();//movendo o cursor para o primeiro elemento da tabela
            do {
                Compras compras = new Compras();
                compras.setId(cursor.getInt(0));
                compras.setQuantidade(cursor.getInt(1));
                compras.setProduto(cursor.getString(2));
                compras.setValor(cursor.getFloat(3));
                adapterCompras.add(compras);
            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }
        return adapterCompras;
    }

    public void deletarTodaTabela() {
        try {
            conn.delete("COMPRAS", null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float excluirValorItem(int id) {
        float result = 0;
        try {
            Cursor cursor = conn.rawQuery("SELECT * FROM COMPRAS WHERE _id = '" + id + "' ", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();//movendo o cursor para o primeiro elemento da tabela
                do {
                    Compras compras = new Compras();
                    result  = compras.setValor(cursor.getFloat(3));
//                    excluirCompras(id);
                } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public int excluirItens(int id) {
        int result = 0;
        try {
            Cursor cursor = conn.rawQuery("SELECT * FROM COMPRAS WHERE _id = '" + id + "' ", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();//movendo o cursor para o primeiro elemento da tabela
                do {
                    Compras compras = new Compras();
                    result  = compras.setQuantidade(cursor.getInt(1));
//                    excluirCompras(id);
                } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
