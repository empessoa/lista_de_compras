package br.com.empessoa8.listamercado.entidade;

/**
 * Created by elias on 04/07/2017.
 */

public class Compras {
    private int _id;
    private int quantidade;
    private String produto;
    private float valor;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        return quantidade;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public float getValor() {
        return valor;
    }

    public float setValor(float valor) {
        this.valor = valor;
        return valor;
    }
}
