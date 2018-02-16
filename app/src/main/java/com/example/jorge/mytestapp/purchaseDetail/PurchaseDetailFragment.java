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
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.jorge.mytestapp.R;
import com.example.jorge.mytestapp.addPurchase.AddPurchaseActivity;
import com.example.jorge.mytestapp.addPurchase.AddPurchaseFragment;
import com.example.jorge.mytestapp.data.source.remote.model.Product;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 16/02/2018.
 */

public class PurchaseDetailFragment extends Fragment implements PurchaseDetailContract.View{

    @NonNull
    private static final String ARGUMENT_PRODUCT_ID = "PRODUCT_ID";
    private static final String ARGUMENT_USER = "USER";

    @NonNull
    private static final int REQUEST_EDIT_TASK = 1;

    private PurchaseDetailContract.Presenter mPresenter;

    private TextView mProductID;

    private TextView mName;

    private TextView mQuantity;

    public static PurchaseDetailFragment newInstance(@Nullable String productId, @Nullable String user) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PRODUCT_ID, productId);
        arguments.putString(ARGUMENT_USER, user);
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
        mName = (TextView) root.findViewById(R.id.tv_name);
        mQuantity = (TextView) root.findViewById(R.id.tv_quantity);


        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_purchase_done);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.editPurchase();
            }
        });

        return root;
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

    }

    @Override
    public void hideQuantity() {
        mQuantity.setVisibility(View.GONE);
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
    public void showCompletionStatus(boolean complete) {


    }

    @Override
    public void showEditPurchase(String shoppingId) {
        Intent intent = new Intent(getContext(), AddPurchaseActivity.class);
        intent.putExtra(AddPurchaseFragment.ARGUMENT_EDIT_SHOPPING_ID, shoppingId);
        startActivityForResult(intent, REQUEST_EDIT_TASK);
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
        return false;
    }
}