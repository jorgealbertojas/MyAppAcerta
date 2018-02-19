package com.example.jorge.mytestapp.purchaseDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.mytestapp.R;
import com.example.jorge.mytestapp.addPurchase.AddPurchaseActivity;
import com.example.jorge.mytestapp.data.source.onLine.model.Product;
import com.squareup.picasso.Picasso;

import static com.example.jorge.mytestapp.addPurchase.AddPurchaseFragment.ARGUMENT_EDIT_SHOPPING_ID;
import static com.example.jorge.mytestapp.shopping.ShoppingFragment.EXTRA_BUNDLE_PRODUCT_SHOPPING;
import static com.example.jorge.mytestapp.shopping.ShoppingFragment.EXTRA_PRODUCT_SHOPPING;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 16/02/2018.
 * Fragment implements Contract of the purchase
 */

public class PurchaseDetailFragment extends Fragment implements PurchaseDetailContract.View {

    @NonNull
    private static final String ARGUMENT_PRODUCT_ID = "PRODUCT_ID";
    private static final String ARGUMENT_PRODUCT = "PRODUCT";

    @NonNull
    private static final int REQUEST_EDIT_TASK = 1;

    private PurchaseDetailContract.Presenter mPresenter;

    private TextView mName;
    private TextView mQuantity;
    private TextView mProductId;
    private TextView mUser;
    private ImageView mImage;

    private Product mProduct;

    private String mUrl;


    public static PurchaseDetailFragment newInstance(@Nullable String shoppingId, Product product) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PRODUCT_ID, shoppingId);
        arguments.putSerializable(ARGUMENT_PRODUCT, product);
        PurchaseDetailFragment fragment = new PurchaseDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.purchase_detail_frag, container, false);
        setHasOptionsMenu(true);

        mName = (TextView) root.findViewById(R.id.tv_product_name);
        mQuantity = (TextView) root.findViewById(R.id.tv_quantity);
        mProductId = (TextView) root.findViewById(R.id.tv_code);

        mUser = (TextView) root.findViewById(R.id.tv_user);

        mImage = (ImageView) root.findViewById(R.id.im_product_image);


        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_purchase);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.editPurchase();
            }
        });

        return root;
    }


    @Override
    public void setPresenter(PurchaseDetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mName.setText("");
            mQuantity.setText(getString(R.string.loading));
        }
    }

    @Override
    public void showMissingPurchase() {
        mName.setText("");
        mProductId.setText("no_data");
        mQuantity.setText("no_data");
    }

    @Override
    public void showName(String name) {
        mName.setVisibility(View.VISIBLE);
        mName.setText(name);


    }

    @Override
    public void hideName() {
        mName.setVisibility(View.GONE);
    }

    @Override
    public void showQuantity(String quantity) {
        mQuantity.setVisibility(View.VISIBLE);
        mQuantity.setText(quantity);
    }

    @Override
    public void hideQuantity() {
        mQuantity.setVisibility(View.GONE);
    }

    @Override
    public void showUrl(String url) {
        mUrl = url;
        Picasso.with(mImage.getContext())
                .load(url)
                .fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(mImage);
    }

    @Override
    public void hideUrl() {

    }

    @Override
    public void showUser(String user) {
        mUser.setVisibility(View.VISIBLE);
        mUser.setText(user);
    }

    @Override
    public void hideUser() {
        mUser.setVisibility(View.GONE);
    }

    @Override
    public void showProductId(String productId) {
        mProductId.setVisibility(View.VISIBLE);
        mProductId.setText(productId);
    }

    @Override
    public void hideProductId() {
        mProductId.setVisibility(View.GONE);
    }


    @Override
    public void showCompletionStatus(boolean complete) {

    }

    @Override
    public void showEditPurchase(String shoppingId, Product product) {

        mProduct = product;

        if (mProduct == null) {
            mProduct = new Product();
            mProduct.setId(Integer.parseInt(mProductId.getText().toString()));
            mProduct.setUrl_image_small(mUrl);
            mProduct.setName(mName.toString());

        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PRODUCT_SHOPPING, mProduct);

        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), AddPurchaseActivity.class);
        intent.putExtra(ARGUMENT_EDIT_SHOPPING_ID, shoppingId);
        intent.putExtra(EXTRA_BUNDLE_PRODUCT_SHOPPING, bundle);
        startActivity(intent);
    }


    @Override
    public void showPurchaseDeleted() {
        getActivity().finish();
    }

    @Override
    public void showPurchaseMarkedComplete() {
        Snackbar.make(getView(), getString(R.string.purchase_marked_complete), Snackbar.LENGTH_LONG)
                .show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                mPresenter.deletePurchase();
                return true;
        }
        return false;
    }

    @Override
    public void showPurchaseMarkedActive() {
        Snackbar.make(getView(), getString(R.string.purchase_marked_active), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.purchase_detail_fragment_menu, menu);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


}
