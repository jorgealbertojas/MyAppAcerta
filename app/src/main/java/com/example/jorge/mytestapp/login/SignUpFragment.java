package com.example.jorge.mytestapp.login;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorge.mytestapp.R;
import com.example.jorge.mytestapp.data.source.onLine.login.LoginServiceImpl;
import com.example.jorge.mytestapp.data.source.onLine.login.model.Login;
import com.example.jorge.mytestapp.util.CustomToast;
import com.example.jorge.mytestapp.util.Util;
import static android.content.Context.MODE_PRIVATE;
import static com.example.jorge.mytestapp.products.ProductActivity.SHARED_KEY_PASSWORD;
import static com.example.jorge.mytestapp.products.ProductActivity.SHARED_KEY_USER;
import static com.example.jorge.mytestapp.products.ProductActivity.SHARED_PREF_USER;

/**
 * Created by jorge on 18/02/2018.
 */

public class SignUpFragment extends Fragment implements OnClickListener, LoginContract.View {
    private static View view;
    private static EditText emailId, password, confirmPassword;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;

    private String mGetEmailId;
    private String mGetPassword;
    private String mGetConfirmPassword;

    private LoginContract.Presenter mActionsListener;

    public SignUpFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_up_layout, container, false);
        initViews();
        setListeners();

        mActionsListener = new LoginPresenter(new LoginServiceImpl(),this);

        return view;
    }

    // Initialize all views
    private void initViews() {
        emailId = (EditText) view.findViewById(R.id.ev_userEmailId);
        password = (EditText) view.findViewById(R.id.ev_password);
        confirmPassword = (EditText) view.findViewById(R.id.ev_confirmPassword);
        signUpButton = (Button) view.findViewById(R.id.b_signUpBtn);

        login = (TextView) view.findViewById(R.id.already_user);
        terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_signUpBtn:

                // Call checkValidation method
                if (checkValidation()) {

                    Login login = new Login();

                    login.setLogin(mGetEmailId);
                    login.setPassword1(mGetPassword);
                    login.setPassoword2(mGetConfirmPassword);

                    mActionsListener.loadingLogin(login);
                }
                break;

            case R.id.already_user:

                // Replace login fragment
                //    new LoginActivity().replaceLoginFragment();
                break;
        }

    }

    // Check Validation Method
    private Boolean checkValidation() {

        // Get all edittext texts
        mGetEmailId = emailId.getText().toString();
        mGetPassword = password.getText().toString();
        mGetConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Util.regEx);
        Matcher m = p.matcher(mGetEmailId);

        // Check if all strings are null or not
        if (mGetEmailId.equals("") || mGetEmailId.length() == 0
                || mGetPassword.equals("") || mGetPassword.length() == 0
                || mGetConfirmPassword.equals("")
                || mGetConfirmPassword.length() == 0) {

            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");
            return  false;

        } else if (!mGetConfirmPassword.equals(mGetPassword)) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Both password doesn't match.");
            return  false;
            // Make sure user should check Terms and Conditions checkbox
        } else if (!terms_conditions.isChecked()) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Please select Terms and Conditions.");
            return  false;
            // Else do signUp or do your stuff
        } else {
            Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
                    .show();
            return  true;
        }

    }

    @Override
    public void showLogin() {
        Toast.makeText(getActivity(), getString(R.string.signUp) + " "  + getString(R.string.Ok) , Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void finish() {
        salveSharedPreferences(mGetEmailId,mGetPassword);
        getActivity().finish();
    }


    /**
     Shared Preferences for save Login and Password
     */
    public void salveSharedPreferences(String nameUser, String password){
        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_USER, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SHARED_KEY_USER, nameUser);
        editor.putString(SHARED_KEY_PASSWORD, password);
        editor.commit();
    }

}
