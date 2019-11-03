package com.codelaxy.myquiz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codelaxy.myquiz.Models.History;
import com.codelaxy.myquiz.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context mContext;
    private ArrayList<History> dataList;

    public HistoryAdapter(Context mContext, ArrayList<History> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {

        History history = dataList.get(position);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s", Locale.US);
        DateFormat simpleFormat = new SimpleDateFormat("dd-MMMM-yyyy h:m:s a", Locale.US);
        Date order_date = null;
        try {
            order_date = format.parse(history.getDate_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.date_time.setText("Requested On - " + simpleFormat.format(order_date));

        holder.amount.setText("Amount - "+history.getAmount()+" Rs.");

        if (history.getStatus().equals("approved")) {
            holder.status.setText("Status - Approved");
            holder.status.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        if(history.getStatus().equals("requested"))
        {
            holder.status.setText("Status - Pending");
            holder.status.setTextColor(mContext.getResources().getColor(R.color.red));
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date_time)
        TextView date_time;

        @BindView(R.id.status)
        TextView status;

        @BindView(R.id.amount)
        TextView amount;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
