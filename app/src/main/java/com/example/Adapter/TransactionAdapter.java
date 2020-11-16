package com.example.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Model.TransactionsModel;
import com.example.merakirana.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ItemRowHolder> {

    private ArrayList<TransactionsModel> dataList;
    private Context mContext;

    public TransactionAdapter(Context mContext, ArrayList<TransactionsModel> dataList) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_list_item, viewGroup, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ItemRowHolder holder, int position) {

        TransactionsModel current = dataList.get(position);

        String mytime=current.getCreatedAt();
        String transDate="";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date = null;
        try {
            date = dateFormat.parse(mytime);
            SimpleDateFormat timeFormat = new SimpleDateFormat("dd,MMM yyyy");
            transDate = timeFormat.format(date);

        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        if (current.getTrans_type().equals("Debit")) {
            //  holder.txtCountryCode.setText(current.getTransfer_country_code());
            holder.tvTransactionContact.setText("Transferred to : " + current.getTransfer_country_code() + " - " +  current.getTransferContact());
            holder.tvTransactionAmount.setText("Amount : " + current.getTransferAmount());
            holder.tvTransactionDate.setText(transDate);
            holder.imageView.setImageResource(R.drawable.send);

        }

        if (current.getTrans_type().equals("Credit")) {
            //   holder.txtCountryCode.setText(current.getTransfer_country_code());
            holder.tvTransactionContact.setText("Recieved from : " + current.getTransfer_country_code() + " - " + current.getTransferContact());
            holder.tvTransactionAmount.setText("Amount : " + current.getTransferAmount());
            holder.tvTransactionDate.setText(transDate);
            holder.imageView.setImageResource(R.drawable.receive);
        }

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }


    class ItemRowHolder extends RecyclerView.ViewHolder {

        TextView tvTransactionContact, tvTransactionAmount, tvTransactionDate,txtCountryCode;
        ImageView imageView;

        ItemRowHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.transImg);
            tvTransactionContact = itemView.findViewById(R.id.tvTransactionContact);
            tvTransactionAmount = itemView.findViewById(R.id.tvTransactionAmount);
            tvTransactionDate = itemView.findViewById(R.id.tvTransactionDate);
            //  txtCountryCode = itemView.findViewById(R.id.txtCountryCode);

        }
    }


}
