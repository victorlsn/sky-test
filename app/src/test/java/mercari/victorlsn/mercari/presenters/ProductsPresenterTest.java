package mercari.victorlsn.mercari.presenters;

import android.content.Context;
import android.widget.Toast;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import mercari.victorlsn.mercari.MyApplication;
import mercari.victorlsn.mercari.beans.Product;
import mercari.victorlsn.mercari.data.Controller;
import mercari.victorlsn.mercari.interfaces.ProductsMVP;
import mercari.victorlsn.mercari.presenters.ProductsPresenterImp;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by victorlsn on 24/07/18.
 */

@RunWith(MockitoJUnitRunner.class)
public class ProductsPresenterTest {

    @Mock
    ProductsMVP.Model model;

    @Mock
    ProductsMVP.View view;

    private ProductsPresenterImp presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Mockito.mock(Controller.class);

        presenter = new ProductsPresenterImp();
        presenter.attachView(view);
    }

    @Test
    public void testGetProductsSuccess() {
        String category = "Men";
        ArrayList<Product> products = new ArrayList<>();

        presenter.requestProducts("https://s3-ap-northeast-1.amazonaws.com/m-et/Android/json/all.json");
        verify(view).showProgressBar(true);

        presenter.requestProductsSuccessfully(null);
        verify(view).showProgressBar(false);
        verify(view).receiveProductsSuccessfully(null);
    }

    @Test
    public void testGetProductsFailure() {
        String category = "Men";
        ArrayList<Product> products = new ArrayList<>();

        presenter.requestProducts("https://s3-ap-northeast-1.amazonaws.com/m-et/Android/json/all");
        verify(view).showProgressBar(true);

        presenter.requestProductsFailure("Error");
        verify(view).showProgressBar(false);
        verify(view).showToast("Error", Toast.LENGTH_SHORT);
        verify(view).receiveProductsFailure();
    }
}
