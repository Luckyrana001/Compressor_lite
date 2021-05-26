package id.zelory.compressor.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.compressor.imagecompress.R;

import java.util.List;

/* renamed from: id.zelory.compressor.sample.StringSpinnerAdapter */
public class StringSpinnerAdapter extends BaseAdapter {
    Context context;
    List<String> countryNames;
    LayoutInflater inflter;

    public StringSpinnerAdapter(Context applicationContext, List<String> countryNames2) {
        this.context = applicationContext;
        this.countryNames = countryNames2;
        this.inflter = LayoutInflater.from(applicationContext);
    }

    public int getCount() {
        return this.countryNames.size();
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2 = this.inflter.inflate(R.layout.custom_states_list_adapter, null);
        ((TextView) view2.findViewById(R.id.textView)).setText((String) this.countryNames.get(i));
        return view2;
    }

    public void updateList(List<String> list) {
        this.countryNames = list;
        notifyDataSetChanged();
    }
}
