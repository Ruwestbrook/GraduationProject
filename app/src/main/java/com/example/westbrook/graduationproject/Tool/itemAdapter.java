package com.example.westbrook.graduationproject.Tool;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.westbrook.graduationproject.R;

/**
 * Created by westbrook on 2018/1/20.
 * 适配器
 */

public  class itemAdapter extends RecyclerView.Adapter<itemAdapter.ViewHolder> implements View.OnClickListener{
    private String[] item;

    public void setItemClick(onItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public onItemClick itemClick=null;
    public itemAdapter(String[] item) {
        this.item = item;
    }

    @Override
    public void onClick(View v) {
        if(itemClick!=null);
        itemClick.itemClick((Integer) v.getTag());
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.textView.setText(item[position]);

    }

    @Override
    public int getItemCount() {
        return item==null?0:item.length;
    }
    public interface onItemClick{
        void itemClick(int position);
    }
}


