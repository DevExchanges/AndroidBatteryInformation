package info.devexchanges.androidbatteryinformation;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<BatteryFeature> batteryFeatures;
    private Activity activity;

    public RecyclerViewAdapter(Activity activity, List<BatteryFeature> batteryFeatures) {
        this.batteryFeatures = batteryFeatures;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_recycler_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        //setting data to view holder elements
        viewHolder.title.setText(batteryFeatures.get(position).getTitle());
        viewHolder.content.setText(batteryFeatures.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return (null != batteryFeatures ? batteryFeatures.size() : 0);
    }

    /**
     * View holder to display each RecylerView item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView content;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
        }
    }
}