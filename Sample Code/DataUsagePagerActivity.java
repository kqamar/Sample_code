package com.stc.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.stc.R;
import com.stc.adapters.DataUsageAdapter;
import com.stc.fragments.DataUsageFragment;
import com.stc.managers.Screen;
import com.stc.managers.ScreenManager;
import com.stc.managers.ServiceTypesManager;
import com.stc.managers.StringsManager;
import com.stc.models.Account;
import com.stc.models.DataUsage;
import com.stc.models.DataUsageContainer;
import com.stc.network.BaseRequestActivity;
import com.stc.network.ResourceKey;
import com.stc.pagerindicator.CirclePageIndicator;
import com.stc.utils.IntentExtras;
import com.stc.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DataUsagePagerActivity extends BaseRequestActivity<DataUsageContainer> implements View.OnClickListener {
    List<DataUsageFragment> dataUsageItems = new ArrayList<>();
    CirclePageIndicator circlePageIndicator;
    private DataUsageAdapter dataUsageAdapter;
    private DataUsageContainer dataUsageContainer;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_usage);
        initGui();
    }

    private void initGui() {
        account = getIntent().getExtras().getParcelable(IntentExtras.ACCOUNT);
        setTitle(StringsManager.getString(StringsManager.actionbar_dataUsage) + " (" + StringUtils.localizeNumberString(StringUtils.addZeroToPhoneNumber(account.getPhoneNumbers().get(0).getNumber() + ")")));
        TextView allInternetBundles = (TextView) findViewById(R.id.allInternetBundlesLabel);
        if (!ServiceTypesManager.isAllInternetPackageAllowed(account))
            allInternetBundles.setVisibility(View.GONE);
        allInternetBundles.setText(StringsManager.getString(StringsManager.dataUsage_allInternetBundles));
    }

    private void fillGui() {
        if(dataUsageContainer!=null){
            TextView allInternetBundles = (TextView) findViewById(R.id.allInternetBundlesLabel);
            allInternetBundles.setOnClickListener(this);}
        for (DataUsage dataUsage : dataUsageContainer.getDataUsage())
            dataUsageItems.add(DataUsageFragment.newInstance(dataUsage));

        dataUsageAdapter = new DataUsageAdapter(getSupportFragmentManager(), dataUsageItems);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(dataUsageAdapter);


        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circleIndicator);
        circlePageIndicator.setViewPager(pager);

        if (dataUsageItems.size() == 1)
            circlePageIndicator.setVisibility(View.GONE);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.allInternetBundlesLabel:
                Bundle allInternetPackageBundle = new Bundle();
                allInternetPackageBundle.putParcelable(IntentExtras.ACCOUNT, account);
                allInternetPackageBundle.putParcelableArrayList(IntentExtras.DATA_USAGE, (ArrayList) dataUsageContainer.getDataUsage());
                ScreenManager.goTo(this, Screen.AllInternetPackage, false, allInternetPackageBundle);
                break;

            default:
                Log.d("DEBUG", "default id:" + v.getId());
        }
    }

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


    @Override
    public void autoStartRequests() {
        if (dataUsageContainer == null)
            execute(ResourceKey.DataUsage, account.getPhoneNumbers().get(0).getNumber(), this);
    }

    @Override
    public void onRequestSuccess(DataUsageContainer dataUsageContainer) {
        this.dataUsageContainer = dataUsageContainer;
        fillGui();
    }
}
