package com.example.criminalintent.controller.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.criminalintent.R;
import com.example.criminalintent.controller.activity.CrimeListActivity;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.repository.CrimeDBRepository;
import com.example.criminalintent.repository.IRepository;
import com.example.criminalintent.utils.PictureUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeDetailFragment extends Fragment {

    public static final String TAG = "CDF";
    public static final String ARGS_CRIME_ID = "crimeId";
    public static final String FRAGMENT_TAG_DATE_PICKER = "DatePicker";
    public static final int REQUEST_CODE_DATE_PICKER = 0;
    private static final int REQUEST_CODE_SELECT_CONTACT = 1;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 2;

    private ImageButton mImgBtnFirst;
    private ImageButton mImgBtnPrevious;
    private ImageButton mImgBtnNext;
    private ImageButton mImgBtnLast;
    private UUID mIdCrime;
    private int mCurrentIndex;
    private List<Crime> mCrimeList;
    private EditText mEditTextTitle;
    private Button mButtonDate;
    private CheckBox mCheckBoxSolved;
    private Crime mCrime;
    private IRepository mRepository;
    public static final String ARGS_SAVE_INDEX = "save_index";
    private Button mButtonSuspect;
    private Button mButtonReport;
    private Button mButtonCall;
    private Button mButtonDial;
    private ImageView mImageViewPhoto;
    private ImageButton mImageButtonTakePicture;

    private Callbacks mCallbacks;
    private File mPhotoFile;
    public static final String AUTHORITY = "com.example.criminalintent.fileProvider";


    public static CrimeDetailFragment newInstance(UUID crimeId) {

        Bundle args = new Bundle();
        args.putSerializable(ARGS_CRIME_ID, crimeId);
        CrimeDetailFragment fragment = new CrimeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CrimeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Callbacks)
            mCallbacks = (Callbacks) context;
        else {
            throw new ClassCastException(context.toString()
                    + " must implement Callbacks");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        mCrimeList = new ArrayList<>();
        mRepository = CrimeDBRepository.getInstance(getActivity());
        mCrimeList = mRepository.getCrimes();


        //this is storage of this fragment
        UUID crimeId = (UUID) getArguments().getSerializable(ARGS_CRIME_ID);
        mCurrentIndex = getArguments().getInt(ARGS_SAVE_INDEX);
        mCrime = mRepository.getCrime(crimeId);
        for (int i = 0; i < mRepository.getCrimes().size(); i++) {
            if (mCrimeList.get(i).getId().equals(crimeId))
                mCurrentIndex = i;
        }

    }

    /**
     * 1. Inflate the layout (or create layout in code)
     * 2. find all views
     * 3. logic for all views (like setListeners)
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_detail, container, false);

        findViews(view);
        initViews();
        setListeners();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        updateCrime();
        int index = 0;
        for (int i = 0; i < mCrimeList.size(); i++) {
            if (mCrimeList.get(i).getId().equals(mIdCrime))
                index = i;

        }

        Intent intent = CrimeListActivity.newIntent(getActivity(), index, mIdCrime);
        startActivity(intent);

        Log.d(TAG, "onPause");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (resultCode != Activity.RESULT_OK || intent == null)
            return;

        if (requestCode == REQUEST_CODE_DATE_PICKER) {
            Date userSelectedDate =
                    (Date) intent.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);

            updateCrimeDate(userSelectedDate);
        }else if (requestCode == REQUEST_CODE_SELECT_CONTACT) {
            handleSelectedContact(intent);
        } else if (requestCode == REQUEST_CODE_IMAGE_CAPTURE) {
            Uri photoUri = generateUriForPhotoFile();
            getActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }


    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists())
            return;

        //this has a better memory management.
        Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getAbsolutePath(), getActivity());
        mImageViewPhoto.setImageBitmap(bitmap);
    }

    private void handleSelectedContact(Intent intent) {
        Uri contactUri = intent.getData();

        String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
        Cursor cursor = getActivity().getContentResolver().query(
                contactUri,
                projection,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return;

        try {
            cursor.moveToFirst();

            String suspect = cursor.getString(0);
            mCrime.setSuspect(suspect);
            mButtonSuspect.setText(suspect);
        } finally {
            cursor.close();
        }
    }

    private void findViews(View view) {
        mEditTextTitle = view.findViewById(R.id.crime_title);
        mButtonDate = view.findViewById(R.id.crime_date);
        mCheckBoxSolved = view.findViewById(R.id.crime_solved);
        mImgBtnPrevious = view.findViewById(R.id.img_btn_previous);
        mImgBtnFirst = view.findViewById(R.id.img_btn_first);
        mImgBtnNext = view.findViewById(R.id.img_btn_next);
        mImgBtnLast = view.findViewById(R.id.img_btn_last);
        mButtonSuspect = view.findViewById(R.id.choose_suspect);
        mButtonReport = view.findViewById(R.id.send_report);
        mButtonCall = view.findViewById(R.id.call_suspect);
        mButtonDial = view.findViewById(R.id.dial_suspect);
        mImageViewPhoto = view.findViewById(R.id.imgview_photo);
        mImageButtonTakePicture = view.findViewById(R.id.imgbtn_take_picture);
    }

    private void initViews() {
        mEditTextTitle.setText(mCrime.getTitle());
        mCheckBoxSolved.setChecked(mCrime.isSolved());
        mButtonDate.setText(mCrime.getDate().toString());
        if (mCrime.getSuspect() != null)
            mButtonSuspect.setText(mCrime.getSuspect());

    }

    private void setListeners() {
        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s + ", " + start + ", " + before + ", " + count);
                mCrime.setTitle(s.toString());
                mIdCrime = mCrime.getId();
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCheckBoxSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                mIdCrime = mCrime.getId();
                updateCrime();
            }
        });

        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment =
                        DatePickerFragment.newInstance(mCrime.getDate());

                //create parent-child relations between CDF and DPF
                datePickerFragment.setTargetFragment(
                        CrimeDetailFragment.this,
                        REQUEST_CODE_DATE_PICKER);

                datePickerFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_DATE_PICKER);
            }
        });

        mImgBtnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = 0;
                updateData(mCurrentIndex);
            }
        });

        mImgBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1 + mCrimeList.size()) % mCrimeList.size();
                updateData(mCurrentIndex);
            }
        });

        mImgBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mCrimeList.size();
                updateData(mCurrentIndex);
            }
        });

        mImgBtnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = mCrimeList.size() - 1;
                updateData(mCurrentIndex);

            }
        });

        mButtonSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectContact();
            }
        });

        mButtonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareReportIntent();
            }
        });

        mButtonDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mButtonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mImageButtonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureIntent();
            }
        });


    }

    private void updateCrime() {
        mRepository.updateCrime(mCrime);
    }

    private void updateCrimeDate(Date userSelectedDate) {
        mCrime.setDate(userSelectedDate);
        updateCrime();

        mButtonDate.setText(mCrime.getDate().toString());
    }

    private void updateData(int index) {
        mCrime = mCrimeList.get(index);
        mEditTextTitle.setText(mCrime.getTitle());
        mCheckBoxSolved.setChecked(mCrime.isSolved());

    }

    private String getReport() {
        String title = mCrime.getTitle();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:SS");
        String dateString = simpleDateFormat.format(mCrime.getDate());

        String solvedString = mCrime.isSolved() ?
                getString(R.string.crime_report_solved) :
                getString(R.string.crime_report_unsolved);

        String suspectString = mCrime.getSuspect() == null ?
                getString(R.string.crime_report_no_suspect) :
                getString(R.string.crime_report_suspect, mCrime.getSuspect());

        String report = getString(
                R.string.crime_report,
                title,
                dateString,
                solvedString,
                suspectString);

        return report;
    }

    private void shareReportIntent() {
       /* Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getReport());
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
        sendIntent.setType("text/plain");


        Intent shareIntent =
                Intent.createChooser(sendIntent, getString(R.string.send_report));
        //we prevent app from crash if the intent has no destination.
        if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivity(shareIntent);*/

        ShareCompat.IntentBuilder shareCompat = null;
        shareCompat.setText(getReport());
        Intent shareIntent =
                Intent.createChooser(shareCompat.getIntent(), getString(R.string.send_report));

        //we prevent app from crash if the intent has no destination.
        if (shareCompat.getIntent().resolveActivity(getActivity().getPackageManager()) != null)
            startActivity(shareIntent);


    }

    private void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_CONTACT);
        }
    }

    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if (mPhotoFile != null && takePictureIntent
                    .resolveActivity(getActivity().getPackageManager()) != null) {

                // file:///data/data/com.example.ci/files/234234234234.jpg
                Uri photoUri = generateUriForPhotoFile();

                grantWriteUriToAllResolvedActivities(takePictureIntent, photoUri);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE);
            }
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }


    }

    private Uri generateUriForPhotoFile() {
        return FileProvider.getUriForFile(
                getContext(),
                AUTHORITY,
                mPhotoFile);
    }

    private void grantWriteUriToAllResolvedActivities(Intent takePictureIntent, Uri photoUri) {
        List<ResolveInfo> activities = getActivity().getPackageManager()
                .queryIntentActivities(
                        takePictureIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity : activities) {
            getActivity().grantUriPermission(
                    activity.activityInfo.packageName,
                    photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }
}