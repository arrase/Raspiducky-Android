package io.github.arrase.raspiducky.dialogs;


import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.arrase.raspiducky.R;
import io.github.arrase.raspiducky.adapters.PayloadAdapter;
import io.github.arrase.raspiducky.providers.PayloadsProvider;
import io.github.arrase.raspiducky.storage.ExternalStorage;


public class AddPayLoadDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder cookieBackupDialog = new AlertDialog.Builder(getActivity());

        cookieBackupDialog.setTitle(R.string.select_a_payload);

        File raspiduckyDir = ExternalStorage.getOrCreateRaspiduckyDir();
        File[] files = null;

        try {
            files = raspiduckyDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".dd");
                }
            });
        } catch (NullPointerException e) {
            // Silent block
        }

        if (files == null || files.length < 1) {
            cookieBackupDialog.setMessage(R.string.no_payload_available);
            cookieBackupDialog.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

            return cookieBackupDialog.create();
        }

        final View dialog_view = getActivity().getLayoutInflater().inflate(R.layout.available_payloads_list, null);

        cookieBackupDialog.setView(dialog_view);
        cookieBackupDialog.setPositiveButton(R.string.btn_close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        ListView backups = (ListView) dialog_view.findViewById(R.id.payloads_list);

        List<File> payload_files = new ArrayList<>();
        Collections.addAll(payload_files, files);

        backups.setAdapter(new PayloadAdapter(getContext(), R.layout.payload_item, payload_files));
        backups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File p = (File) parent.getItemAtPosition(position);
                ContentValues fields = new ContentValues();
                fields.put(PayloadsProvider.Payload.PATH, p.getAbsolutePath());
                ContentResolver mResolver = view.getContext().getContentResolver();
                mResolver.insert(
                        PayloadsProvider.CONTENT_URI, fields
                );
            }
        });

        return cookieBackupDialog.create();
    }
}
