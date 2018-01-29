package br.com.empessoa8.listamercado;

/**
 * Created by elias on 14/07/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.empessoa8.listamercado.controller.Frag_Calculo;
import br.com.empessoa8.listamercado.controller.Frag_Compras;

/**
 * Created by elias on 14/06/2017.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"Compras", "Calcular"};

    public TabAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new Frag_Compras();
                break;
            case 1:
                fragment = new Frag_Calculo();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;//retornando o tamanho das abas
    }

    //recupera o titulo das abas
    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }
}
