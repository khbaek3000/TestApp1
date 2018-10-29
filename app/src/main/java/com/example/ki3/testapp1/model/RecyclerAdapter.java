package com.example.ki3.testapp1.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ki3.testapp1.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    ArrayList<ItemsData> mItems;
    private  LayoutInflater mInflater;
    private ItemClick itemClick;

    public interface ItemClick{
        public void onClick(View view, int position);
    }
    public void setItemClick(ItemClick itemClick){
        this.itemClick = itemClick;
    }


    public RecyclerAdapter(Context context, ArrayList<ItemsData> items) {
        mInflater = LayoutInflater.from(context);
        mItems = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_view, viewGroup, false);
        View view = mInflater.inflate(R.layout.item_recycler_view, viewGroup, false);
        return new ItemViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        final int mPosition= i;
        itemViewHolder.tvItem.setText(mItems.get(i).getItemname() + ":" + mItems.get(i).getItemdetail());

        //itemViewHolder.imgView.setImageResource(R.drawable.baseline_nature_people_black_18dp);
        if(mItems.get(i).getItemcategory().equals("park")){
            itemViewHolder.imgView.setImageResource(R.drawable.baseline_nature_people_black_18dp);
        }
        else if(mItems.get(i).getItemcategory().equals("theater")){
            itemViewHolder.imgView.setImageResource(R.drawable.icontheater);
        }
        else if(mItems.get(i).getItemcategory().equals("museum")){
            itemViewHolder.imgView.setImageResource(R.drawable.iconmuseum);
        }
        else if(mItems.get(i).getItemcategory().equals("beach")){
            itemViewHolder.imgView.setImageResource(R.drawable.iconbeach);
        }
         else if(mItems.get(i).getItemcategory().equals("mountain")){
            itemViewHolder.imgView.setImageResource(R.drawable.iconmountain);
        }
         else if(mItems.get(i).getItemcategory().equals("exhibition")){
            itemViewHolder.imgView.setImageResource(R.drawable.iconexhibition);
        }
         else if(mItems.get(i).getItemcategory().equals("art")){
            itemViewHolder.imgView.setImageResource(R.drawable.iconart);
        }
         else if(mItems.get(i).getItemcategory().equals("festival")){
            itemViewHolder.imgView.setImageResource(R.drawable.iconfestival);
        }
        else {
            itemViewHolder.imgView.setImageResource(R.drawable.baseline_sentiment_satisfied_alt_black_18dp);

        }
        itemViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClick != null){
                    itemClick.onClick(view, mPosition );
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        final RecyclerAdapter mAdapter;

        View mView;
        private TextView tvItem;
        private ImageView imgView;

        public ItemViewHolder(@NonNull View itemView, RecyclerAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;
            this.mView = itemView;
            tvItem = (TextView) itemView.findViewById(R.id.TxV_item_recycler);
            imgView = (ImageView) itemView.findViewById(R.id.Img_item_recycler);


        }
    }
}
