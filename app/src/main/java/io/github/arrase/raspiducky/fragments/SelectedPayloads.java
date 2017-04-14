package io.github.arrase.raspiducky.fragments;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.arrase.raspiducky.R;
import io.github.arrase.raspiducky.adapters.SelectedPayloadsAdapter;
import io.github.arrase.raspiducky.providers.PayloadsProvider;

public class SelectedPayloads extends Fragment {
    private ContentResolver mContentResolver;
    private FloatingActionButton mFab;
    private Context mContext;
    private OnAddPayloadListener mListener;
    private SelectedPayloadsAdapter mPayloads;
    private PayloadsObserver mPayloadsObserver;

    private String[] mProjection = new String[]{
            PayloadsProvider.Payload._ID,
            PayloadsProvider.Payload.PATH
    };

    public SelectedPayloads() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mContentResolver = mContext.getContentResolver();
        mPayloadsObserver = new PayloadsObserver(new Handler());
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

        mPayloads = new SelectedPayloadsAdapter(
                mContentResolver.query(
                        PayloadsProvider.CONTENT_URI, mProjection, null, null, null
                ));

        mContentResolver.registerContentObserver(PayloadsProvider.CONTENT_URI, true, mPayloadsObserver);

        RecyclerView list = (RecyclerView) view.findViewById(R.id.selected_payloads_list);
        list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        list.setAdapter(mPayloads);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mContentResolver.unregisterContentObserver(mPayloadsObserver);
    }


    public interface OnAddPayloadListener {
        void onAddPayloadCallback();
    }

    class PayloadsObserver extends ContentObserver {
        PayloadsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            mPayloads.changeCursor(mContentResolver.query(
                    PayloadsProvider.CONTENT_URI, mProjection, null, null, null
            ));
        }

    }
}
