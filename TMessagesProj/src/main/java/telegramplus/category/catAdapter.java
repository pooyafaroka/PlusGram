package telegramplus.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ir.mmnotimm.telegramplus.R;

import java.util.List;




public class catAdapter extends ArrayAdapter<category> {

    Context context;


    public catAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public catAdapter(Context context, int resource, List<category> items) {
        super(context, resource, items);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item_cats, null);
        }

        final category p = getItem(position);

        if (p != null) {
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView size = (TextView) v.findViewById(R.id.size);


            if (name != null) {
                name.setText(p.getName());
            }

            catDBAdapter catDBAdapter = new catDBAdapter(context);
            catDBAdapter.open();

            size.setText(String.format(context.getResources().getString(R.string.items_few) , catDBAdapter.getCatSize(p.getId())));

            catDBAdapter.close();



        }

        return v;
    }




}
