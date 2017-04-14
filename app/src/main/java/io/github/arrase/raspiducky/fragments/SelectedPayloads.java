package io.github.arrase.raspiducky.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.arrase.raspiducky.R;

public class SelectedPayloads extends Fragment {
    private FloatingActionButton mFab;
    private Context mContext;
    private OnAddPayloadListener mListener;

    public SelectedPayloads() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        if (context instanceof OnAddPayloadListener) {
            mListener = (OnAddPayloadListener) mContext;
        } else {
            throw new RuntimeException(mContext.toString()
                    + " must implement OnAddPayloadListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selected_payload_list, container, false);

        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAddPayloadCallback();
            }
        });

        return view;
    }

    public interface OnAddPayloadListener {
        void onAddPayloadCallback();
    }
}
