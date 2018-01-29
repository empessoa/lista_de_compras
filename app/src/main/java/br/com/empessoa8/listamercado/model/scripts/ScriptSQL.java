package br.com.empessoa8.listamercado.model.scripts;

/**
 * Created by elias on 04/07/2017.
 */

public class ScriptSQL {
    public static String getCreateTableCompras(){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS COMPRAS( ");
        sqlBuilder.append("_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append("quantidade INTEGER(5), ");
        sqlBuilder.append("produto VARCHAR(50), ");
        sqlBuilder.append("valor INTEGER(10)");
        sqlBuilder.append(");");
        return  sqlBuilder.toString();
    }
}
