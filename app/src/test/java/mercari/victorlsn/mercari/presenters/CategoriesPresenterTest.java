package mercari.victorlsn.mercari.presenters;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import mercari.victorlsn.mercari.BuildConfig;
import mercari.victorlsn.mercari.beans.Category;
import mercari.victorlsn.mercari.data.Controller;
import mercari.victorlsn.mercari.interfaces.CategoriesMVP;

import static org.mockito.Mockito.verify;

/**
 * Created by victorlsn on 24/07/18.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CategoriesPresenterTest {

    @Mock
    CategoriesMVP.Model model;

    @Mock
    CategoriesMVP.View view;

    private CategoriesPresenterImp presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Mockito.mock(Controller.class);

        presenter = new CategoriesPresenterImp();
        presenter.attachView(view);
    }

    @Test
    public void testGetCategoriesSuccess() {
        ArrayList<Category> categories = new ArrayList<>();

        presenter.requestCategories();
        verify(view).showProgressBar(true);

        presenter.requestCategoriesSuccessfully(null);
        verify(view).showProgressBar(false);
        verify(view).receiveCategoriesSuccessfully(null);
    }

    @Test
    public void testGetCategoriesFailure() {
        ArrayList<Category> categories = new ArrayList<>();

        presenter.requestCategoriesFailure("Error");
        verify(view).showProgressBar(false);
        verify(view).receiveCategoriesFailure();
    }
}
