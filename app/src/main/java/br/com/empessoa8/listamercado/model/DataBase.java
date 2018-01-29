package br.com.empessoa8.listamercado.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.empessoa8.listamercado.model.scripts.ScriptSQL;

/**
 * Created by elias on 04/07/2017.
 */

public class DataBase extends SQLiteOpenHelper{

    public DataBase(Context context) {
        super(context, "Compras", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScriptSQL.getCreateTableCompras());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
