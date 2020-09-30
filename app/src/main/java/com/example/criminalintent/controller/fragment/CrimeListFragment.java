package com.example.criminalintent.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.criminalintent.R;
import com.example.criminalintent.controller.activity.CrimePagerActivity;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.model.User;
import com.example.criminalintent.repository.CrimeDBRepository;
import com.example.criminalintent.repository.IRepository;

import java.util.List;
import java.util.UUID;

public class CrimeListFragment extends Fragment {

    public static final String TAG = "CLF";
    public static final String BUNDLE_ARG_IS_SUBTITLE_VISIBLE = "isSubtitleVisible";
    public static final String ARGS_INDEX = "Index";
    private int mIndex = 0;
    public static final String ARGS_ID_USER = "argsIdUser";
    private List<User> mUsers;
    String mUserName;
    private RecyclerView mRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private IRepository mRepository;
    private boolean mIsSubtitleVisible = false;
    private UUID mUserId;
    private User mUser;


    public static CrimeListFragment newInstance(int index, UUID uuid) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INDEX, index);
        args.putSerializable(ARGS_ID_USER, uuid);
        CrimeListFragment fragment = new CrimeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CrimeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRepository = CrimeDBRepository.getInstance(getActivity());
        mUsers = mRepository.getUsers();
        if (getArguments() != null) {
            mUserId = (UUID) getArguments().getSerializable(ARGS_ID_USER);
        }
        for (User userFind : mUsers) {
            if (userFind.getIdUser().equals(mUserId))
                mUser = userFind;
        }
        setHasOptionsMenu(true);
        if (savedInstanceState != null)
            mIsSubtitleVisible =
                    savedInstanceState.getBoolean(BUNDLE_ARG_IS_SUBTITLE_VISIBLE, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        findViews(view);
        initViews();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cime_list, menu);

        MenuItem item = menu.findItem(R.id.menu_item_subtitle);
        setMenuItemSubtitle(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_crime:
                Crime crime = new Crime();
                mRepository.insertCrime(crime);

                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);

                return true;
            case R.id.menu_item_subtitle:
                mIsSubtitleVisible = !mIsSubtitleVisible;

                updateSubtitle();
                setMenuItemSubtitle(item);

//                getActivity().invalidateOptionsMenu();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(BUNDLE_ARG_IS_SUBTITLE_VISIBLE, mIsSubtitleVisible);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARGS_INDEX);
            updateUI(mIndex);
        }

        updateSubtitle();
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_crime_list);
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI(mIndex);
    }

    private void updateUI(int index) {
        List<Crime> crimes = mRepository.getCrimes();

        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            //mCrimeAdapter.setCrimes(crimes);
            mCrimeAdapter.notifyItemChanged(index);
        }
    }

    private void updateSubtitle() {
        int numberOfCrimes = mRepository.getCrimes().size();
        String crimesText = mIsSubtitleVisible ? numberOfCrimes + " crimes" : null;

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(mUser.getName());
        activity.getSupportActionBar().setSubtitle(crimesText);
    }

    private void setMenuItemSubtitle(MenuItem item) {
        item.setTitle(
                mIsSubtitleVisible ?
                        R.string.menu_item_hide_subtitle :
                        R.string.menu_item_show_subtitle);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private ImageView mImageViewSolved;
        private Crime mCrime;

        public CrimeHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewTitle = itemView.findViewById(R.id.row_item_crime_title);
            mTextViewDate = itemView.findViewById(R.id.row_item_crime_date);
            mImageViewSolved = itemView.findViewById(R.id.imgview_solved);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
                    startActivity(intent);
                }
            });
        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTextViewTitle.setText(crime.getTitle());
            mTextViewDate.setText(crime.getDate().toString());
            mImageViewSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public List<Crime> getCrimes() {
            return mCrimes;
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder");

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.crime_row_list, parent, false);
            CrimeHolder crimeHolder = new CrimeHolder(view);
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: " + position);

            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }
    }

}