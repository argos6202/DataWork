package com.example.datawork.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datawork.R;
import com.example.datawork.databinding.FragmentPagosBinding;
import com.example.datawork.model.Pagos;
import com.example.datawork.model.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RVPadapter extends RecyclerView.Adapter<RVPadapter.ViewHolder> {
    private TextView txt;
    private TextView txt1;
    Context ct;
    ArrayList<Pagos> listpagos;
    //SELECCIONAR ELEMENTOS PRESIONADOS
    private Set<Integer> selectedItems = new HashSet<>();

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
        holder.pagoLayout.setText("S/. -"+ String.valueOf(pagos.getPago()));
        // Verifica si no hay elementos seleccionados y establece el estado inicial
        if (selectedItems.isEmpty()) {
            holder.imageView.setImageResource(R.drawable.check1); // Establece el estado inicial para la imagen
        } else {
            // Verifica si el elemento actual está seleccionado y actualiza la imagen correspondiente
            if (selectedItems.contains(position)) {
                holder.imageView.setImageResource(R.drawable.check2);
            } else {
                holder.imageView.setImageResource(R.drawable.check1);
            }
        }
        holder.itemView.setOnClickListener(view -> clickListener.OnClick(listpagos.get(position)));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (selectedItems.contains(adapterPosition)) {
                    selectedItems.remove(adapterPosition);
                } else {
                    selectedItems.add(adapterPosition);
                }
                // Actualiza la vista del RecyclerView
                notifyDataSetChanged();
                updateSelectionTextView();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listpagos.size();
    }

    //Este extend se pone primero que el del principio
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreLayout, pagoLayout;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreLayout = itemView.findViewById(R.id.txtNombrePago);
            pagoLayout = itemView.findViewById(R.id.txtMontoPagado);
            imageView = itemView.findViewById(R.id.icon);
        }
    }
    private void updateSelectionTextView() {
        int numSelectedItems = selectedItems.size();
        String selectionText = numSelectedItems + " elemento(s) seleccionado(s)";
        if(numSelectedItems == 0){
            txt.setText("Registro de pagos");
            txt1.setText("Imprimir");
        }else{
            txt.setText(selectionText);
            txt1.setText("Borrar");
        }
    }
    public void setSelectionTextView(TextView textView) {
        txt = textView;
    }
    public void setSelectionTextView1(TextView textView) {
        txt1 = textView;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener{
        void OnClick(Pagos pagos);
    }
    public Set<Integer> getSelectedItems() {
        return selectedItems;
    }
}
