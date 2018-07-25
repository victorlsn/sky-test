package mercari.victorlsn.mercari.presenters;

import android.widget.Toast;

import java.util.List;

import mercari.victorlsn.mercari.beans.Product;
import mercari.victorlsn.mercari.interfaces.BaseMVP;
import mercari.victorlsn.mercari.interfaces.ProductsMVP;
import mercari.victorlsn.mercari.models.ProductsModelImp;

/**
 * Created by victorlsn on 23/07/18.
 */

public class ProductsPresenterImp implements ProductsMVP.Presenter {
    private ProductsMVP.View view;
    private ProductsMVP.Model model;

    public ProductsPresenterImp() {
        model = new ProductsModelImp(this);
    }

    @Override
    public boolean attachView(BaseMVP.View view) {
        if (null == view) return false;
        this.view = (ProductsMVP.View) view;

        return true;
    }

    @Override
    public void requestProducts(String categoryUrl) {
        view.showProgressBar(true);
        model.getProducts(categoryUrl);
    }

    @Override
    public void requestProductsSuccessfully(List<Product> products) {
        view.showProgressBar(false);
        view.receiveProductsSuccessfully(products);
    }

    @Override
    public void requestProductsFailure(String message) {
        view.showProgressBar(false);
        if (null == message || message.isEmpty()) return;
        view.showToast(message, Toast.LENGTH_SHORT);
        view.receiveProductsFailure();
    }
}
