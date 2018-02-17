package com.example.jorge.mytestapp.purchaseDetail;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.jorge.mytestapp.Injection;
import com.example.jorge.mytestapp.R;
import com.example.jorge.mytestapp.data.source.remote.model.Product;
import com.example.jorge.mytestapp.util.ActivityUtils;

import static com.example.jorge.mytestapp.products.ProductFragment.EXTRA_BUNDLE_PRODUCT;
import static com.example.jorge.mytestapp.products.ProductFragment.EXTRA_PRODUCT;

public class PurchaseDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "PRODUCT_ID";

    private Product mProduct;
    private Bundle mBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_detail);
        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        // Get the requested task id
        String shoppingId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);

        PurchaseDetailFragment PurchaseDetailFragment = (PurchaseDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        mBundle = getIntent().getBundleExtra(EXTRA_BUNDLE_PRODUCT);
        mProduct = (Product) mBundle.getSerializable(EXTRA_PRODUCT);

        if (PurchaseDetailFragment == null) {
            PurchaseDetailFragment = PurchaseDetailFragment.newInstance(shoppingId,mProduct);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    PurchaseDetailFragment, R.id.contentFrame);
        }

        // Create the presenter
        new PurchaseDetailPresenter(
                Injection.provideShoppingRepository(getApplicationContext()),
                PurchaseDetailFragment, shoppingId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
