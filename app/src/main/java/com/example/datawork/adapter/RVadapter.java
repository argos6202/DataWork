package com.example.datawork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datawork.R;
import com.example.datawork.databinding.FragmentEmpleadosBinding;
import com.example.datawork.model.User;

import java.util.ArrayList;

public class RVadapter extends RecyclerView.Adapter<RVadapter.MyViewHolder> {

    Context ct;

    ArrayList<User> list;
    OnItemClickListener onItemClickListener;

    public RVadapter(Context ct, ArrayList<User> list) {
        this.ct = ct;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ct).inflate((R.layout.lista_empleados),parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = list.get(position);
        holder.nombre.setText(user.getNombre());
        holder.numero.setText(user.getNumero());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onClick(list.get(position)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nombre, numero;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombreEmpleado);
            numero = itemView.findViewById(R.id.txtNumeroEmpleado);
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener{
        void onClick(User user);
    }
}
