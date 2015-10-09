package com.stc.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.stc.R;
import com.stc.fragments.SendsmsFragment;

/**
 * Created by kashanqamar on 9/9/14.
 */
public class SendSMSActivity extends BaseActivity implements SendsmsFragment.OnSendsmsCompleted {
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        bundle = this.getIntent().getExtras();
        if (findViewById(R.id.fragmentContainer) != null) {
            if (savedInstanceState != null)
                return;

            initGui();
        }
    }

    //-------------> don't delete it cant use manifest on this one because i want back to number dashboard fragment :)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStack();
                else finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void initGui() {
        SendsmsFragment sendsmsFragment = new SendsmsFragment();
        sendsmsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, sendsmsFragment).commit();
    }
}