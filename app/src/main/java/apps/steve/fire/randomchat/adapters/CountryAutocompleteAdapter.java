package apps.steve.fire.randomchat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.model.Country;

/**
 * Created by Steve on 7/10/2016.
 */

public class CountryAutocompleteAdapter extends ArrayAdapter<Country> implements Filterable {


    private static final String TAG = CountryAutocompleteAdapter.class.getSimpleName();
    private List<Country> originalData = new ArrayList<>();
    private List<Country> filteredData = new ArrayList<>();


    public CountryAutocompleteAdapter(Context context, List<Country> data) {
        super(context, R.layout.item_country, R.id.text_country);
        this.originalData = data;
        this.filteredData = data;
    }

    public int getCount() {
        if (filteredData != null){
            return filteredData.size();
        }else{
            return 0;
        }
    }

    public Country getItem(int position) {
        return filteredData.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = super.getView(position, convertView, parent);

        //

        Log.d(TAG, "getView position: " + position);
        Country country = getItem(position);

        TextView textViewCountry = (TextView) row.findViewById(R.id.text_country);
        ImageView imageView = (ImageView) row.findViewById(R.id.image_country_flag);
        textViewCountry.setText(country.getName());
        textViewCountry.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_grey_900));

        Glide.with(getContext())
                .load(country.getDrawable())
                .placeholder(country.getDrawable())
                .into(imageView);

        return row;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final List<Country> list = originalData;

                int count = list.size();
                final ArrayList<Country> nlist = new ArrayList<Country>(count);

                String filterableString ;

                for (int i = 0; i < count; i++) {
                    filterableString = list.get(i).getName();
                    if (filterableString.toLowerCase().contains(filterString)) {
                        nlist.add(list.get(i));
                    }
                }

                results.values = nlist;
                results.count = nlist.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (ArrayList<Country>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
