package com.example.datawork.menu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.datawork.R;

public class PagTabPadre extends FragmentStateAdapter {

    private int numftab;

    public PagTabPadre(@NonNull FragmentManager fragmentManager,@NonNull Lifecycle lifecycle, int numftab) {
        super(fragmentManager, lifecycle);
        this.numftab = numftab;
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new FragmentInicio();
            case 1: return new FragmentEmpleados();
            case 2: return new FragmentPagos();
            case 3: return new FragmentAjustes();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return numftab;
    }

}