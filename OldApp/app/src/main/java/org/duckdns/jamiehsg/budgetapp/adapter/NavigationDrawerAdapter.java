package org.duckdns.jamiehsg.budgetapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.duckdns.jamiehsg.budgetapp.R;
import org.duckdns.jamiehsg.budgetapp.model.NavDrawerItem;
import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

/**
 * Created by jamie on 11/21/15.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder>{

    List<NavDrawerItem> data= Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    public void delete(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view=inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
    }

    @Override
    public int getItemCount(){
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;

        public MyViewHolder(View itemView){
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
        }
    }
}
