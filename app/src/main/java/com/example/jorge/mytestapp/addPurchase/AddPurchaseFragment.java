package com.example.jorge.mytestapp.addPurchase;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static com.google.common.base.Preconditions.checkNotNull;
import android.support.design.widget.Snackbar;

import com.example.jorge.mytestapp.R;
import com.example.jorge.mytestapp.data.source.remote.model.Product;
import com.squareup.picasso.Picasso;

/**
 * Created by jorge on 15/02/2018.
 */

public class AddPurchaseFragment extends Fragment implements AddPurchaseContract.View {

    public static final String ARGUMENT_EDIT_SHOPPING_ID = "EDIT_SHOPPING_ID";


    private AddPurchaseContract.Presenter mPresenter;

    private TextView mProductId;
    private TextView mProductName;
    private ImageView mProductImage;

    private EditText mQuantity;

    private static Product mProduct;

    public static AddPurchaseFragment newInstance(Product product) {
        mProduct = product;
        return new AddPurchaseFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_purchase_fragment, container, false);

        mQuantity = (EditText) root.findViewById(R.id.et_quantity);

        mProductName = (TextView) root.findViewById(R.id.tv_product_name);
        mProductId = (TextView) root.findViewById(R.id.tv_code);
        mProductImage = (ImageView) root.findViewById(R.id.im_product_image);


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

    public void ShowProduct(Product product){
        mProductName.setText(product.getName());
        mProductId.setText(Integer.toString(product.getId()));

        Picasso.with(mProductImage.getContext())
                .load(product.getUrl_image_small())
                .fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(mProductImage);

    }
}
