package br.com.empessoa8.listamercado.controller;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.empessoa8.listamercado.R;
import br.com.empessoa8.listamercado.mask.Mask;
import br.com.empessoa8.listamercado.mask.MonetaryMask;

public class Calcular_Produto extends AppCompatActivity implements View.OnClickListener {
    View view;

    private AlertDialog.Builder mensagem;
    private EditText edt_peso_produto_1, edt_peso_produto_2, edt_valor_produto_1, edt_valor_produto_2;
    private Button btn_calcular, btn_limpar;
    private TextView txtv_informar_produto,  txtv_produto_1, txtv_produto_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcular__produto);
        edt_peso_produto_1 = (EditText) findViewById(R.id.edt_peso_produto_1);
        edt_peso_produto_1.addTextChangedListener(Mask.insert(Mask.MASK_PESO, edt_peso_produto_1));
        edt_peso_produto_2 = (EditText) findViewById(R.id.edt_peso_produto_2);
        edt_peso_produto_2.addTextChangedListener(Mask.insert(Mask.MASK_PESO, edt_peso_produto_2));
        edt_valor_produto_1 = (EditText) findViewById(R.id.edt_valor_produto_1);
        edt_valor_produto_1.addTextChangedListener(new MonetaryMask(edt_valor_produto_1) {
        });
        edt_valor_produto_2 = (EditText) findViewById(R.id.edt_valor_produto_2);
        edt_valor_produto_2.addTextChangedListener(new MonetaryMask(edt_valor_produto_2) {
        });
        btn_calcular = (Button) findViewById(R.id.btn_calcular);
        btn_calcular.setOnClickListener(this);
        btn_limpar = (Button) findViewById(R.id.btn_limpar);
        btn_limpar.setOnClickListener(this);

        txtv_informar_produto = (TextView) findViewById(R.id.txtv_informar_produto);
        txtv_produto_1 = (TextView) findViewById(R.id.txtv_produto_1);
        txtv_produto_2 = (TextView) findViewById(R.id.txtv_produto_2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_calcular:
                calcularProdutos();
                break;
            case R.id.btn_limpar:
                limparCampos();
                break;
        }
    }

    private void calcularProdutos() {
        if ((!edt_peso_produto_1.getText().toString().equals("")) &&
                (!edt_peso_produto_2.getText().toString().equals("")) &&
                (!edt_valor_produto_1.getText().toString().equals("")) &&
                (!edt_valor_produto_2.getText().toString().equals(""))) {

            int peso_produto_1, peso_produto_2;
            float valor_produto_1, valor_produto_2;
            String valor_produto_1_replace_1, valor_produto_1_replace_2, valor_produto_2_replace_1, valor_produto_2_replace_2;

            valor_produto_1_replace_2 = edt_valor_produto_1.getText().toString().replaceAll("[R$.,]", "");
            //valor_produto_1_replace_2 = valor_produto_1_replace_1.replaceAll("[,]", ".");
            valor_produto_1 = Float.parseFloat(valor_produto_1_replace_2);
            peso_produto_1 = Integer.parseInt(edt_peso_produto_1.getText().toString());


            valor_produto_2_replace_2 = edt_valor_produto_2.getText().toString().replaceAll("[R$.,]", "");
            //valor_produto_2_replace_2 = valor_produto_2_replace_1.replaceAll("[,]", ".");
            peso_produto_2 = Integer.parseInt(edt_peso_produto_2.getText().toString());
            valor_produto_2 = Float.parseFloat(valor_produto_2_replace_2);

            float result_produto_1 = (valor_produto_1 / peso_produto_1);
            //float result_1_produto = Float.valueOf(String.format(Locale.US, "%.2", result_produto_1));
            float result_produto_2 = (valor_produto_2 / peso_produto_2);
            //float result_2_produto = Float.valueOf(String.format(Locale.US, "%.2", result_produto_2));

            if (result_produto_1 == result_produto_2) {
                //exibirMsg("PRODUTO CALCULADO", "MELHOR OPÇÃO: \n\n\nPRODUTOS IGUAIS", "SAIR");
                txtv_informar_produto.setTextColor(Color.parseColor("#D50000"));
                txtv_produto_2.setTextColor(Color.parseColor("#D50000"));
                txtv_produto_1.setTextColor(Color.parseColor("#D50000"));
                txtv_informar_produto.setText("PRODUTOS EQUIVALENTES!");

            } else if (result_produto_1 < result_produto_2) {
                txtv_informar_produto.setTextColor(Color.parseColor("#D50000"));
                txtv_produto_1.setTextColor(Color.parseColor("#D50000"));
                txtv_produto_2.setTextColor(Color.parseColor("#000000"));
                txtv_informar_produto.setText("PRODUTO 1 É A MELHOR OPÇÃO!");
                //exibirMsg("PRODUTO CALCULADO", "MELHOR OPÇÃO:  \n\n\nPRODUTO 1", "SAIR");
            } else {
                txtv_informar_produto.setTextColor(Color.parseColor("#D50000"));
                txtv_produto_2.setTextColor(Color.parseColor("#D50000"));
                txtv_produto_1.setTextColor(Color.parseColor("#000000"));
                txtv_informar_produto.setText("PRODUTO 2 É A MELHOR OPÇÃO!");
                //exibirMsg("PRODUTO CALCULADO", "MELHOR OPÇÃO:  \n\n\nPRODUTO 2" , "SAIR");
            }
        } else {
            Toast.makeText(getApplicationContext(), "Campos em branco", Toast.LENGTH_SHORT).show();
        }
    }

    private void limparCampos() {
        edt_peso_produto_1.setText("");
        edt_peso_produto_2.setText("");
        edt_valor_produto_1.setText("");
        edt_valor_produto_2.setText("");
        txtv_informar_produto.setTextColor(Color.parseColor("#000000"));
        txtv_informar_produto.setText("Melhor Opção!");
        txtv_produto_1.setTextColor(Color.parseColor("#000000"));
        txtv_produto_2.setTextColor(Color.parseColor("#000000"));
    }

    private void exibirMsg(String titulo, String msn, String sair) {
        mensagem = new AlertDialog.Builder(getApplicationContext());
        mensagem.setTitle(titulo);
        mensagem.setMessage(msn);
        mensagem.setNeutralButton(sair, null);
        mensagem.create();
        mensagem.show();
    }
}
