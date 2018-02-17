package com.example.jorge.mytestapp.shopping;

import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.jorge.mytestapp.Injection;
import com.example.jorge.mytestapp.R;
import com.example.jorge.mytestapp.data.source.remote.model.Product;
import com.example.jorge.mytestapp.util.ActivityUtils;
import com.example.jorge.mytestapp.util.EspressoIdlingResource;


import static com.example.jorge.mytestapp.products.ProductFragment.EXTRA_PRODUCT;
import static com.example.jorge.mytestapp.products.ProductFragment.EXTRA_BUNDLE_PRODUCT;

public class

ShoppingActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Bundle mBundle;

    private ShoppingPresenter mShoppingPresenter;
    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);

        mBundle = getIntent().getBundleExtra(EXTRA_BUNDLE_PRODUCT);
        mProduct = (Product) mBundle.getSerializable(EXTRA_PRODUCT);



        ShoppingFragment shoppingFragment =
                (ShoppingFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (shoppingFragment == null) {
            // Create the fragment
            shoppingFragment = ShoppingFragment.newInstance(mProduct);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), shoppingFragment, R.id.contentFrame);
        }

        // Create the presenter
        mShoppingPresenter = new ShoppingPresenter(
                Injection.provideShoppingRepository(getApplicationContext()), shoppingFragment);

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
          //  TasksFilterType currentFiltering =
          //          (TasksFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
          //  mTasksPresenter.setFiltering(currentFiltering);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
       // outState.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter.getFiltering());

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
