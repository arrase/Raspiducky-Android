package io.github.arrase.raspiducky.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import io.github.arrase.raspiducky.R;

public class PayloadAdapter extends ArrayAdapter<File> {
    private int mResource;

    public PayloadAdapter(Context context, int resource) {
        super(context, resource);
        mResource = resource;
    }

    public PayloadAdapter(Context context, int resource, List<File> zips) {
        super(context, resource, zips);
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(mResource, null);
        }

        File p = getItem(position);

        if (p != null) {
            TextView name = (TextView) v.findViewById(R.id.payload_name);

            if (name != null)
                name.setText(p.getName());
        }

        return v;
    }
}
