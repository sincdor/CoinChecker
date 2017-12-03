package virtual_coin_checker.sincdor.coinchecker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import virtual_coin_checker.sincdor.coinchecker.R;
import virtual_coin_checker.sincdor.coinchecker.models.Ticker;
import virtual_coin_checker.sincdor.coinchecker.utils.Utils;

/**
 * Created by andre on 28-11-2017.
 */

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {

    private List<Ticker> dataSet;
    private  Integer changeType;

    public MainActivityAdapter(List<Ticker> dataSet, Integer changeType){
        this.dataSet = dataSet;
        this.changeType = changeType;

    }

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvCoin;
        TextView tvDate;
        TextView tvPrice;
        TextView tvChange24_hours;
        TextView tvChangeFinal;

        public ViewHolder(View itemView) {
            super(itemView);

            tvCoin = itemView.findViewById(R.id.id_card_main_activity_name);
            tvDate = itemView.findViewById(R.id.id_card_main_activity_date);
            tvPrice = itemView.findViewById(R.id.id_card_main_activity_price);
            tvChange24_hours = itemView.findViewById(R.id.id_card_main_activity_change);
            tvChangeFinal = itemView.findViewById(R.id.id_card_main_activity_change_24_final);
        }
    }


    @Override
    public MainActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_main_activity, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainActivityAdapter.ViewHolder holder, int position) {
        holder.tvCoin.setText(dataSet.get(position).getName());
        holder.tvPrice.setText(dataSet.get(position).getPrice_usd());
        holder.tvDate.setText(Utils.getDateTime());
        if(changeType.equals(Utils.ONE_HOUR_CHANGE)) {
            holder.tvChange24_hours.setText(dataSet.get(position).getPercent_change_1h());
            holder.tvChangeFinal.setText("(Change in last hour)");
        }else if(changeType.equals(Utils.TWENTY_FOUR_HOURS_CHANGE)) {
            holder.tvChange24_hours.setText(dataSet.get(position).getPercent_change_24h());
            holder.tvChangeFinal.setText("(Change last 24 hours)");
        }else if(changeType.equals(Utils.SEVEN_DAYS_CHANGE)) {
            holder.tvChange24_hours.setText(dataSet.get(position).getPercent_change_7d());
            holder.tvChangeFinal.setText("(Change in last seven days)");
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void addTicker(Ticker t){
        this.dataSet.add(0, t);
        notifyItemInserted(0);
    }
}
