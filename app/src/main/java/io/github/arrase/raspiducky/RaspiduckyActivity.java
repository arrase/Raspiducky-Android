package io.github.arrase.raspiducky;

import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import io.github.arrase.raspiducky.constants.RaspiduckyConstants;
import io.github.arrase.raspiducky.dialogs.AddPayLoadDialog;
import io.github.arrase.raspiducky.fragments.LockedByPermissions;
import io.github.arrase.raspiducky.fragments.RaspiduckySettings;
import io.github.arrase.raspiducky.fragments.SelectedPayloads;
import io.github.arrase.raspiducky.permissions.PermissionManager;
import io.github.arrase.raspiducky.providers.PayloadsProvider;

public class RaspiduckyActivity extends AppCompatActivity implements SelectedPayloads.OnAddPayloadListener {
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raspiducky);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = getFragmentManager();

        // Do not overlapping fragments.
        if (savedInstanceState != null) return;

        if (PermissionManager.isLollipopOrHigher() && !PermissionManager.hasExternalWritePermission(this)) {
            PermissionManager.requestExternalWritePermissions(this, RaspiduckyConstants.REQUEST_WRITE_STORAGE);
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new SelectedPayloads())
                    .commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case RaspiduckyConstants.REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new SelectedPayloads())
                            .commit();
                } else {
                    // Request rationale
                    PermissionManager.requestExternalWritePermissions(this, RaspiduckyConstants.REQUEST_WRITE_STORAGE);
                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new LockedByPermissions())
                            .commit();
                }
                break;
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0)
            mFragmentManager.popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_raspiducky, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                mFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, new RaspiduckySettings())
                    .commit();
                return true;
            case R.id.action_run:
                return true;
            case R.id.action_clear:
                getContentResolver().delete(PayloadsProvider.CONTENT_URI, null, null);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddPayloadCallback() {
        AddPayLoadDialog dialog = new AddPayLoadDialog();
        dialog.show(getSupportFragmentManager(), "AddPayLoadDialog");
    }
}
