package com.example.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.criminalintent.R;
import com.example.criminalintent.controller.fragment.CrimeListFragment;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.repository.CrimeRepository;

public class CrimeListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, CrimeListActivity.class);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return CrimeListFragment.newInstance();
    }

    @Override
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
    }
}