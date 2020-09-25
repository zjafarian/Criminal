package com.example.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.criminalintent.controller.fragment.CrimeListFragment;

import java.util.UUID;

public class CrimeListActivity extends SingleFragmentActivity {

    public static final String EXTRA_ID_USER = "idUser";

    public static Intent newIntent(Context context, UUID uuid) {
        Intent intent = new Intent(context, CrimeListActivity.class);
        intent.putExtra(EXTRA_ID_USER,uuid);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_ID_USER);
        CrimeListFragment crimeListFragment = CrimeListFragment.newInstance(uuid);
        return crimeListFragment;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cime_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("CLA", "item: " + item.getItemId());

        switch (item.getItemId()) {
            case R.id.menu_item_add_crime:
                Crime crime = new Crime();
                CrimeRepository.getInstance().insertCrime(crime);

                Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
                startActivity(intent);

                return true;
            case R.id.menu_item_clear:
                //remove all items in repository
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}