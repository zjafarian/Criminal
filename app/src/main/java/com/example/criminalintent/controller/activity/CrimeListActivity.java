package com.example.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.criminalintent.R;
import com.example.criminalintent.controller.fragment.CrimeDetailFragment;
import com.example.criminalintent.controller.fragment.CrimeListFragment;
import com.example.criminalintent.model.Crime;

import java.util.UUID;

public class CrimeListActivity extends SingleFragmentActivity implements
        CrimeListFragment.Callbacks, CrimeDetailFragment.Callbacks {
    public static final String EXTRA_CURRENT_INDEX = "com.example.criminalintent.CurrentIndex";
    public static final String EXTRA_ID_USER = "com.example.criminalintent.idUser";
    int index;

    public static Intent newIntent(Context context, int index, UUID uuid) {
        Intent intent = new Intent(context, CrimeListActivity.class);
        intent.putExtra(EXTRA_CURRENT_INDEX,index);
        intent.putExtra(EXTRA_ID_USER,uuid);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        index = getIntent().getIntExtra(EXTRA_CURRENT_INDEX,0);
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_ID_USER);
        CrimeListFragment crimeListFragment = CrimeListFragment.newInstance(index,uuid);
        return crimeListFragment;
    }


    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment crimeListFragment = (CrimeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        crimeListFragment.updateUI(index);
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {
            //add fragment to container
            CrimeDetailFragment crimeDetailFragment =
                    CrimeDetailFragment.newInstance(crime.getId());

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, crimeDetailFragment)
                    .commit();
        }

    }
}