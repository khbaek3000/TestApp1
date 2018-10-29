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
import java.util.List;

public class RecyclerAdapterTicket extends RecyclerView.Adapter<RecyclerAdapterTicket.ItemViewHolder> {

    ArrayList<TicketData> mTicket;

    private LayoutInflater mInflater;
    private TicketClick ticketClick;

    public interface TicketClick{
        public void onClick(View view, int position);
    }
    public void setTicketClick(TicketClick ticketClick){this.ticketClick = ticketClick;}

    public RecyclerAdapterTicket(Context context, ArrayList<TicketData> tickets) {
        mInflater = LayoutInflater.from(context);
        mTicket = tickets;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {

        final int mPosition = i;
        itemViewHolder.tvTicket.setText(mTicket.get(i).getTicketname());
        itemViewHolder.imgTicket.setImageResource(mTicket.get(i).getTicketRes());

        if(mTicket.get(i).getTicketuse().equals("yes")){
            itemViewHolder.tvTicket.append("  :사용한티켓");
        }

        itemViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ticketClick != null){
                    ticketClick.onClick(view, mPosition);
                }
            }
        });

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.ticket_recycler_view, viewGroup, false);

        return new ItemViewHolder(view, this);
    }

    @Override
    public int getItemCount() {
        return mTicket.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        final RecyclerAdapterTicket mAdapter;

        View mView;
        private TextView tvTicket;
        private ImageView imgTicket;

        public ItemViewHolder(@NonNull View itemView, RecyclerAdapterTicket adapter) {
            super(itemView);

            this.mAdapter = adapter;
            this.mView = itemView;
            tvTicket = (TextView) itemView.findViewById(R.id.TxV_Ticket_ticketname);
            imgTicket = (ImageView) itemView.findViewById(R.id.Img_ticket_img);

        }
    }
}
