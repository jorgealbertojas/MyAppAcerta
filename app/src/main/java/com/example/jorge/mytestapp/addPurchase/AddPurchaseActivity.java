package com.example.jorge.mytestapp.addPurchase;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.jorge.mytestapp.R;

import com.example.jorge.mytestapp.Injection;

import com.example.jorge.mytestapp.data.source.local.ShoppingDao;
import com.example.jorge.mytestapp.data.source.remote.model.Product;
import com.example.jorge.mytestapp.util.ActivityUtils;

import com.example.jorge.mytestapp.util.EspressoIdlingResource;

import static com.example.jorge.mytestapp.shopping.ShoppingFragment.EXTRA_BUNDLE_PRODUCT_SHOPPING;
import static com.example.jorge.mytestapp.shopping.ShoppingFragment.EXTRA_PRODUCT_SHOPPING;


public class AddPurchaseActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_PURCHASE = 1;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";

    private AddPurchasePresenter mAddPurchasePresenter;

    private ActionBar mActionBar;

    private static Product mProduct;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase);
        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);






        AddPurchaseFragment addPurchaseFragment = (AddPurchaseFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        String shoppingId = getIntent().getStringExtra(AddPurchaseFragment.ARGUMENT_EDIT_SHOPPING_ID);

        mBundle = getIntent().getBundleExtra(EXTRA_BUNDLE_PRODUCT_SHOPPING);
        mProduct = (Product) mBundle.getSerializable(EXTRA_PRODUCT_SHOPPING);

        setToolbarTitle(shoppingId);

        if (addPurchaseFragment == null) {
            addPurchaseFragment = AddPurchaseFragment.newInstance(mProduct);

            if (getIntent().hasExtra(AddPurchaseFragment.ARGUMENT_EDIT_SHOPPING_ID)) {
                Bundle bundle = new Bundle();
                bundle.putString(AddPurchaseFragment.ARGUMENT_EDIT_SHOPPING_ID, shoppingId);
                addPurchaseFragment.setArguments(bundle);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addPurchaseFragment, R.id.contentFrame);
        }

        boolean shouldLoadDataFromRepo = true;

        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        // Create the presenter
        mAddPurchasePresenter = new AddPurchasePresenter(
                Injection.provideShoppingRepository(getApplicationContext()),
                addPurchaseFragment,
                shoppingId,
                shouldLoadDataFromRepo);
    }

    private void setToolbarTitle(@Nullable String taskId) {
        if(taskId == null) {
            mActionBar.setTitle(R.string.add_purchase);
        } else {
            mActionBar.setTitle(R.string.edit_purchase);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the state so that next time we know if we need to refresh data.
        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, mAddPurchasePresenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
