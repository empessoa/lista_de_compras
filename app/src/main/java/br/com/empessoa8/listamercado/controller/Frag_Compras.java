package br.com.empessoa8.listamercado.controller;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import br.com.empessoa8.listamercado.R;
import br.com.empessoa8.listamercado.entidade.Compras;
import br.com.empessoa8.listamercado.entidade.persistencia.Pers_Compras;
import br.com.empessoa8.listamercado.mask.Mask;
import br.com.empessoa8.listamercado.mask.MonetaryMask;
import br.com.empessoa8.listamercado.model.DataBase;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Compras extends Fragment implements View.OnClickListener  {

    View view;
    AlertDialog.Builder mensagem;
    private DataBase dataBase;
    private SQLiteDatabase connBanco;
    private Compras compras;
    private Pers_Compras pers_compras;
    private ArrayAdapter<Compras> arrayAdapter;

    private EditText edt_quantidade;
    private EditText edt_produtos;
    private EditText edt_valor;
    private ListView lista_compras;

    private TextView result_total_itens;
    private TextView result_total_compras;

    private int soma_itens_total = 0;
    private float soma_compras_total = 0;

    private float soma_total_compra;

    private float valor_item_deletado;
    private int itens_por_produto;
    private int item_deletado;

    private double valor_produto_formatado;

    private ImageButton btn_adicionar;

    private FloatingActionButton fab_main, fab_feedback, fab_limpar_compras, fab_calcular_produtos;
    private Animation fab_open, fab_close, fab_sentido_horario, fab_anti_horario;
    boolean isOpenFab = false;


    public Frag_Compras() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.frag__compras, container, false);

        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        fab_sentido_horario = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_horario);
        fab_anti_horario = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_anti_horario);

        fab_main = (FloatingActionButton) view.findViewById(R.id.fab_main);
        //fab_feedback = (FloatingActionButton) view.findViewById(R.id.fab_feedback);
        fab_calcular_produtos = (FloatingActionButton) view.findViewById(R.id.fab_calcularProduto);
        fab_limpar_compras = (FloatingActionButton) view.findViewById(R.id.fab_limparCompras);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpenFab){
                    fab_main.startAnimation(fab_anti_horario);
       //             fab_feedback.startAnimation(fab_close);
                    fab_calcular_produtos.startAnimation(fab_close);
                    fab_limpar_compras.startAnimation(fab_close);
       //             fab_feedback.setClickable(false);
                    fab_calcular_produtos.setClickable(false);
                    fab_limpar_compras.setClickable(false);
                    isOpenFab = false;

                }else {
                    fab_main.startAnimation(fab_sentido_horario);
//                    fab_feedback.startAnimation(fab_open);
                    fab_calcular_produtos.startAnimation(fab_open);
                    fab_limpar_compras.startAnimation(fab_open);
//                    fab_feedback.setClickable(true);
                    fab_calcular_produtos.setClickable(true);
                    fab_limpar_compras.setClickable(true);
                    isOpenFab = true;
                }
            }
        });

       /* fab_feedback = (FloatingActionButton) view.findViewById(R.id.fab_feedback);
        fab_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Act_Email.class));
            }
        });*/
        fab_calcular_produtos = (FloatingActionButton) view.findViewById(R.id.fab_calcularProduto);
        fab_calcular_produtos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Calcular_Produto.class));
            }
        });
        fab_limpar_compras = (FloatingActionButton) view.findViewById(R.id.fab_limparCompras);
        fab_limpar_compras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limparTela();
            }
        });

        edt_quantidade = (EditText) view.findViewById(R.id.edt_quantidade);
        edt_quantidade.addTextChangedListener(Mask.insert(Mask.MASK_QUANTIDADE, edt_quantidade));
        edt_produtos = (EditText) view.findViewById(R.id.edt_produtos);

        edt_valor = (EditText) view.findViewById(R.id.edt_valor);
//        edt_valor.addTextChangedListener(Mask.insert(Mask.MASK_VALOR, edt_valor));
        edt_valor.addTextChangedListener(new MonetaryMask(edt_valor) {
        });

        btn_adicionar = (ImageButton) view.findViewById(R.id.btn_adicionar);
        btn_adicionar.setOnClickListener(this);

        result_total_itens = (TextView) view.findViewById(R.id.total_itens);
        result_total_compras = (TextView) view.findViewById(R.id.total_compras);

        lista_compras = (ListView) view.findViewById(R.id.lista_compras);
        lista_compras.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deletarCompras(position);
                return false;
            }
        });

        compras = new Compras();
        conectarBanco();
        recuperarSharedPreferences();

        return view;
    }

    private void conectarBanco() {
        try {
            dataBase = new DataBase(getActivity());
            connBanco = dataBase.getReadableDatabase();
            pers_compras = new Pers_Compras(connBanco);
            arrayAdapter = pers_compras.buscarCompras(getActivity());
            lista_compras.setAdapter(arrayAdapter);

            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Verificando conexão com o Banco");
            mensagem.setMessage("Conectado com sucesso Serviços!...");
//            mensagem.setNeutralButton("Sair", null);
//            mensagem.show();


        } catch (SQLException ex) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Verificando conexão com o Banco");
            mensagem.setMessage("Erro de conexão:" + ex.getMessage());
            mensagem.setNeutralButton("Sair", null);
            mensagem.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_adicionar:
                salvarCompras();
                break;
        }
    }

    private void salvarCompras() {

        pers_compras = new Pers_Compras(connBanco);
        try {
            if ((!edt_quantidade.getText().toString().equals("")) &&
                    (!edt_produtos.getText().toString().equals("")) &&
                    (!edt_valor.getText().toString().equals(""))) {

                itens_por_produto = Integer.parseInt(edt_quantidade.getText().toString());

                String valor_produto_replace_1 = edt_valor.getText().toString().replaceAll("[R$.]", "");
                String valor_produto_replace_2 = valor_produto_replace_1.replaceAll("[,]", ".");
                valor_produto_formatado = Double.parseDouble(valor_produto_replace_2);

                double itens_x_valor = itens_por_produto * valor_produto_formatado;
                float valor_x_item = Float.valueOf(String.format(Locale.US, "%.2f", itens_x_valor));
                soma_compras_total += valor_x_item;
                soma_total_compra = Float.valueOf(String.format(Locale.US, "%.2f", soma_compras_total));
                result_total_compras.setText(String.valueOf(soma_total_compra));

                soma_itens_total += itens_por_produto;
                result_total_itens.setText(Integer.toString(soma_itens_total));

                compras.setQuantidade(itens_por_produto);
                compras.setProduto(edt_produtos.getText().toString());
                compras.setValor(valor_x_item);
                pers_compras.inserirCompras(compras);

                atualizarCompras();
                salvarSharedPreferences();

            } else {
                Toast.makeText(getActivity(), "Item em branco!!", Toast.LENGTH_SHORT).show();
            }

        } catch (SQLException ex) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Salvar Compras!");
            mensagem.setMessage("Erro método salvarComparas() :" + ex.getMessage());
            mensagem.setNeutralButton("Sair", null);
            mensagem.show();
        }
    }

    private void deletarCompras(int position) {
        try {
            compras = new Compras();
            compras = arrayAdapter.getItem(position);
            dataBase = new DataBase(getActivity());
            connBanco = dataBase.getReadableDatabase();
            pers_compras = new Pers_Compras(connBanco);
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("DELETAR ITEM");
            mensagem.setMessage("Deseja deletar item?");
            mensagem.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    valor_item_deletado = pers_compras.excluirValorItem(compras.getId());
                    soma_compras_total -= valor_item_deletado;
                    soma_total_compra = Float.valueOf(String.format(Locale.US, "%.2f", soma_compras_total));

                    item_deletado = pers_compras.excluirItens(compras.getId());
                    soma_itens_total -= item_deletado;


                    if(soma_compras_total < 0 ||soma_itens_total < 0){
                        soma_compras_total = 0;
                        soma_itens_total = 0;
                    }else{
                        result_total_compras.setText(String.valueOf(soma_total_compra));
                        result_total_itens.setText(Integer.toString(soma_itens_total));
                    }

                    pers_compras.excluirCompras(compras.getId());
                    atualizarCompras();
                }
            });
            mensagem.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Cancelado!!", Toast.LENGTH_SHORT).show();
                }
            });
            mensagem.create();
            mensagem.show();
        } catch (SQLException ex) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("deletar Compras!");
            mensagem.setMessage("Erro método deletarComparas() :" + ex.getMessage());
            mensagem.setNeutralButton("Sair", null);
            mensagem.show();
        }
    }

    private void atualizarCompras() {
        pers_compras = new Pers_Compras(connBanco);
        try {
            arrayAdapter = pers_compras.buscarCompras(getActivity());
            lista_compras.setAdapter(arrayAdapter);
            edt_quantidade.setText("");
            edt_produtos.setText("");
            edt_valor.setText("");
        } catch (SQLException ex) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Atualizar Compras!");
            mensagem.setMessage("Erro método atualizarComparas() :" + ex.getMessage());
            mensagem.setNeutralButton("Sair", null);
            mensagem.show();
        }
    }

    public void limparTela() {
        try {
            compras = new Compras();
            dataBase = new DataBase(getActivity());
            connBanco = dataBase.getReadableDatabase();
            pers_compras = new Pers_Compras(connBanco);
            pers_compras.deletarTodaTabela();
            atualizarCompras();
            soma_itens_total = 0;
            soma_compras_total = 0;
            result_total_compras.setText(Float.toString(soma_compras_total));
            result_total_itens.setText(Float.toString(soma_itens_total));
        } catch (SQLException ex) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Atualizar Compras!");
            mensagem.setMessage("Erro método atualizarComparas() :" + ex.getMessage());
            mensagem.setNeutralButton("Sair", null);
            mensagem.show();
        }
    }

    private void salvarSharedPreferences() {
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString("total_itens", result_total_itens.getText().toString());
        edt.putString("total_compras", result_total_compras.getText().toString());//salvando
        edt.commit();
    }

    private void recuperarSharedPreferences() {
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        if ((pref.contains("total_itens")) && (pref.contains("total_compras"))) {
            result_total_itens.setText(pref.getString("total_itens", result_total_itens.getText().toString()));
            result_total_compras.setText(pref.getString("total_compras", result_total_compras.getText().toString()));
        } else {
            //Toast.makeText(getActivity(), "Km Incial não definido!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarSharedPreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        recuperarSharedPreferences();
    }

    @Override
    public void onStop() {
        super.onStop();
        salvarSharedPreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        salvarSharedPreferences();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connBanco != null) {
            connBanco.close();
        }
//        salvarSharedPreferences();
        limparTela();
    }
}
