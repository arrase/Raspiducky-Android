package io.github.arrase.raspiducky.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;

import io.github.arrase.raspiducky.R;
import io.github.arrase.raspiducky.providers.PayloadsProvider;


public class SelectedPayloadsAdapter extends CursorRecyclerViewAdapter<SelectedPayloadsAdapter.ViewHolder> {

    public SelectedPayloadsAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payload_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor) {
        final int id = cursor.getInt(cursor.getColumnIndex(PayloadsProvider.Payload._ID));
        final String file_name = cursor.getString(cursor.getColumnIndex(PayloadsProvider.Payload.PATH));

        File file = new File(file_name);

        viewHolder.mName.setText(file.getName());
        viewHolder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mName;

        ViewHolder(View view) {
            super(view);
            mName = (TextView) view.findViewById(R.id.payload_name);
        }
    }
}
