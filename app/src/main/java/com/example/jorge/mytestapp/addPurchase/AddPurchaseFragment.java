package com.example.jorge.mytestapp.addPurchase;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;
import static com.example.jorge.mytestapp.products.ProductActivity.SHARED_KEY_USER;
import static com.example.jorge.mytestapp.products.ProductActivity.SHARED_PREF_USER;
import static com.google.common.base.Preconditions.checkNotNull;
import android.support.design.widget.Snackbar;

import com.example.jorge.mytestapp.R;
import com.example.jorge.mytestapp.data.source.onLine.model.Product;
import com.squareup.picasso.Picasso;

/**
 * Created by jorge on 15/02/2018.
 */

public class AddPurchaseFragment extends Fragment implements AddPurchaseContract.View {

    public static final String ARGUMENT_EDIT_SHOPPING_ID = "EDIT_SHOPPING_ID";


    private AddPurchaseContract.Presenter mPresenter;

    private TextView mProductId;
    private TextView mProductName;
    private TextView mProductURL;
    private ImageView mProductImage;

    private TextView mQuantity;

    private static Product mProduct;

    public static AddPurchaseFragment newInstance(Product product) {
        mProduct = product;
        return new AddPurchaseFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public AddPurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_purchase_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = getUserSharedPreference();
                mPresenter.savePurchase( mProductId.getText().toString(),user,mQuantity.getText().toString(), mProductName.getText().toString(), mProductURL.getText().toString());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_purchase_fragment, container, false);




        mProductImage = (ImageView) root.findViewById(R.id.im_product_image);
        mProductName = (TextView) root.findViewById(R.id.tv_product_name);
        mProductId = (TextView) root.findViewById(R.id.tv_code);
        mProductURL = (TextView) root.findViewById(R.id.tv_url);

        mQuantity = (TextView) root.findViewById(R.id.et_quantity);

        ShowProduct(mProduct);

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void setPresenter(AddPurchaseContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showEmptyPurchaseError() {
        Snackbar.make(mProductName, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showPurchaseList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setQuantity(String quantity) {
        mQuantity.setText(quantity);
    }

    @Override
    public void setProductId(String productId) {
        mProductId.setText(productId);
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }

    public void ShowProduct(Product product){
        mProductName.setText(product.getName());
        mProductURL.setText(product.getUrl_image_small());
        mProductId.setText(Integer.toString(product.getId()));

        Picasso.with(mProductImage.getContext())
                .load(product.getUrl_image_small())
                .fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(mProductImage);
    }

    private String getUserSharedPreference(){
        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_USER, MODE_PRIVATE);
        return sp.getString(SHARED_KEY_USER, null);
    }
}
