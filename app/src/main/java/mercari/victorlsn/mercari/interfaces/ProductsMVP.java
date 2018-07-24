package mercari.victorlsn.mercari.interfaces;

import java.util.List;

import mercari.victorlsn.mercari.beans.Product;

/**
 * Created by victorlsn on 23/07/18.
 */

public interface ProductsMVP {
    interface Model{
        void getProducts(String categoryUrl);
    }
    interface Presenter extends BaseMVP.Presenter{
        void requestProducts(String categoryUrl);
        void requestProductsSuccessfully(List<Product> products);
        void requestProductsFailure(String message);
    }
    interface View extends BaseMVP.View{
        void receiveProductsSuccessfully(List<Product> products);
        void receiveProductsFailure();
    }
}
