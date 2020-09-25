package com.example.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.criminalintent.R;
import com.example.criminalintent.controller.fragment.LoginFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.View;

import java.util.UUID;

public class LoginActivity extends SingleFragmentActivity {

    public static final String EXTRA_REPOSITORY_USER_LOGIN = "com.example.taskmanager.repositoryUserLogin";
    public static final String EXTRA_ID_USER = "com.example.taskmanager.idUser";

    public static Intent newIntent(Context context, UUID id) {
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.putExtra(EXTRA_ID_USER, id);
        return intent;
    }



    @Override
    public Fragment createFragment() {
        UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_ID_USER);
        LoginFragment loginFragment = LoginFragment.newInstance(id);
        return loginFragment;
    }
}