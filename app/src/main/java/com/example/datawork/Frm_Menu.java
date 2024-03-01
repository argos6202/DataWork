package com.example.datawork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.datawork.menu.PagTabPadre;
import com.google.android.material.tabs.TabLayout;

public class Frm_Menu extends AppCompatActivity {

    private TabLayout tabPadre;
    private ViewPager2 vPantalla;
    private PagTabPadre ADPPadre;
    private int backPressCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_menu);
        tabPadre = (TabLayout) findViewById(R.id.menu);
        vPantalla = (ViewPager2) findViewById(R.id.vPantalla);

        ADPPadre = new PagTabPadre(getSupportFragmentManager(),getLifecycle(),tabPadre.getTabCount());
        vPantalla.setAdapter(ADPPadre);

        tabPadre.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vPantalla.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backPressCount++;

            if (backPressCount == 3) {
                finishAffinity();
                System.exit(0);
            } else {
                // Mostrar un mensaje al usuario
                Toast.makeText(this, "Presione atrás nuevamente para salir de la aplicación", Toast.LENGTH_LONG).show();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}