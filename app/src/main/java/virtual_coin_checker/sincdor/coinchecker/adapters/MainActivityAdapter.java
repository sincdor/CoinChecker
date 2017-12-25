package virtual_coin_checker.sincdor.coinchecker.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import virtual_coin_checker.sincdor.coinchecker.ChangeActivity;
import virtual_coin_checker.sincdor.coinchecker.MainActivity;
import virtual_coin_checker.sincdor.coinchecker.R;
import virtual_coin_checker.sincdor.coinchecker.models.Ticker;
import virtual_coin_checker.sincdor.coinchecker.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by andre on 28-11-2017.
 */

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {

    private List<Ticker> dataSet;
    private  Integer changeType;
    private Context context;

    public MainActivityAdapter(List<Ticker> dataSet, Integer changeType, Context context){
        this.dataSet = dataSet;
        this.changeType = changeType;
        this.context = context;

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
        ImageButton imageButton;
        TextView estimatedValue;

        public ViewHolder(View itemView) {
            super(itemView);

            tvCoin = itemView.findViewById(R.id.id_card_main_activity_name);
            tvDate = itemView.findViewById(R.id.id_card_main_activity_date);
            tvPrice = itemView.findViewById(R.id.id_card_main_activity_price);
            tvChange24_hours = itemView.findViewById(R.id.id_card_main_activity_change);
            tvChangeFinal = itemView.findViewById(R.id.id_card_main_activity_change_24_final);
            imageButton = itemView.findViewById(R.id.id_image_button);
            estimatedValue = itemView.findViewById(R.id.id_card_main_activity_estimated_price);
        }
    }


    @Override
    public MainActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_main_activity, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainActivityAdapter.ViewHolder holder, final int position) {
        holder.tvCoin.setText(dataSet.get(position).getName());
        holder.tvPrice.setText(dataSet.get(position).getPrice_usd() + "$");
        holder.tvDate.setText(Utils.getDateTime());
        if(changeType.equals(Utils.ONE_HOUR_CHANGE)) {
            holder.tvChange24_hours.setText(dataSet.get(position).getPercent_change_1h() + "%");
            holder.tvChangeFinal.setText("Change in last hour");
        }else if(changeType.equals(Utils.TWENTY_FOUR_HOURS_CHANGE)) {
            holder.tvChange24_hours.setText(dataSet.get(position).getPercent_change_24h()+"%");
            holder.tvChangeFinal.setText("Change last 24 hours");
        }else if(changeType.equals(Utils.SEVEN_DAYS_CHANGE)) {
            holder.tvChange24_hours.setText(dataSet.get(position).getPercent_change_7d()+"%");
            holder.tvChangeFinal.setText("Change in last seven days");
        }
        if(dataSet.get(position).getUnits_total() > 0){
            Double estimatedValue = dataSet.get(position).getUnits_total() * Double.valueOf(dataSet.get(position).getPrice_usd());
            holder.estimatedValue.setText(estimatedValue.intValue()+"$");
        }else{
            holder.estimatedValue.setText("0.0");
        }
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(context, ChangeActivity.class);
                it.putExtra("COIN_ID", dataSet.get(position).getId());

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Units Total");
                alertDialog.setMessage("Enter Units");

                final EditText input = new EditText(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String units = input.getText().toString();
                                try{
                                    Double unit_d = Double.valueOf(units);
                                    dataSet.get(position).setUnits_total(unit_d);
                                    Utils.changeUnits(dataSet.get(position).getId(), unit_d, context);
                                    notifyDataSetChanged();
                                }catch (ClassCastException e)
                                {
                                    Log.e(TAG, "onClick: " + e);
                                }
                            }
                        });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }

        });
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
