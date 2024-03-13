package com.example.datawork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datawork.R;
import com.example.datawork.model.Pagos;
import com.example.datawork.model.User;

import java.util.ArrayList;

public class RVPadapter extends RecyclerView.Adapter<RVPadapter.ViewHolder> {

    Context ct;
    ArrayList<Pagos> listpagos;

    ClickListener clickListener;

    public RVPadapter(Context ct, ArrayList<Pagos> listpagos) {
        this.ct = ct;
        this.listpagos = listpagos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ct).inflate(R.layout.lista_pagos, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pagos pagos = listpagos.get(position);
        holder.nombreLayout.setText(pagos.getNombre());
        holder.pagoLayout.setText(String.valueOf(pagos.getPago()));
        holder.itemView.setOnClickListener(view -> clickListener.OnClick(listpagos.get(position)));
    }

    @Override
    public int getItemCount() {
        return listpagos.size();
    }

    //Este extend se pone primero que el del principio
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreLayout, pagoLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreLayout = itemView.findViewById(R.id.txtNombrePago);
            pagoLayout = itemView.findViewById(R.id.txtMontoPagado);
        }
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener{
        void OnClick(Pagos pagos);
    }
}
