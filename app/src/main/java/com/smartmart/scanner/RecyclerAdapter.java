package com.smartmart.scanner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private static final String TAG = "RecyclerAdapter";
    private int count = 0;
    private static ArrayList<String> name;
    private static ArrayList<String> price;
    private static ArrayList<String> quantity;
    private static Button del;


    public RecyclerAdapter(ArrayList<String> arr1,ArrayList<String> arr2,ArrayList<String> arr3)
    {
        this.name = arr1;
        this.price = arr2;
        this.quantity = arr3;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_rows, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

//        View view = View.inflate()

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemname.setText(name.get(position));
        holder.itemprice.setText(price.get(position));
        holder.itemquantity.setText(quantity.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView itemquantity, itemname, itemprice;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            itemquantity = itemView.findViewById (R.id.quantity);
            itemname = itemView.findViewById (R.id.name);
            itemprice = itemView.findViewById (R.id.price);


        }

    }
}







