package com.example.jorge.mytestapp.shopping;

import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.jorge.mytestapp.Injection;
import com.example.jorge.mytestapp.R;
import com.example.jorge.mytestapp.data.source.onLine.model.Product;
import com.example.jorge.mytestapp.util.ActivityUtils;
import com.example.jorge.mytestapp.util.EspressoIdlingResource;


import static com.example.jorge.mytestapp.products.ProductFragment.EXTRA_PRODUCT;
import static com.example.jorge.mytestapp.products.ProductFragment.EXTRA_BUNDLE_PRODUCT;

public class ShoppingActivity extends AppCompatActivity implements SearchView.OnQueryTextListener  {

    private DrawerLayout mDrawerLayout;
    private Bundle mBundle;

    private ShoppingPresenter mShoppingPresenter;
    private Product mProduct;

    private SearchView mSearchView;
    private MenuItem mSearchMenuItem;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);

        mBundle = getIntent().getBundleExtra(EXTRA_BUNDLE_PRODUCT);
        mProduct = (Product) mBundle.getSerializable(EXTRA_PRODUCT);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);

        mSearchMenuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();

        mSearchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));

        mSearchView.setSubmitButtonEnabled(true);

        mSearchMenuItem.setEnabled(true);


        mSearchView.setOnQueryTextListener(this);



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

        return true;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.equals("")){
            mShoppingPresenter.loadShopping(true);
        }else{
            mShoppingPresenter.FindShopping(newText);
        }

        return true;
    }


}



