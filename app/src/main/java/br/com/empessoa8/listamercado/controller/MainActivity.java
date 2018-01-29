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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import br.com.empessoa8.listamercado.R;
import br.com.empessoa8.listamercado.TabAdapter;
import br.com.empessoa8.listamercado.helper.SlidingTabLayout;
import br.com.empessoa8.listamercado.model.DataBase;
import br.com.empessoa8.listamercado.entidade.Compras;
import br.com.empessoa8.listamercado.entidade.persistencia.Pers_Compras;
import br.com.empessoa8.listamercado.mask.Mask;
import br.com.empessoa8.listamercado.mask.MonetaryMask;

public class MainActivity extends AppCompatActivity {
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //montando as abas
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs_compras);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina_compras);
        //configurar aliding tabs
        slidingTabLayout.setDistributeEvenly(true);//distribui as tabs no espaço do layout
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorSliding));//configurando a cor
        //configurando o adapter Act_Servicos
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());//gerencia os fragmentos
        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.enviar_feedback) {
            startActivity(new Intent(getApplicationContext(), Act_Email.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
