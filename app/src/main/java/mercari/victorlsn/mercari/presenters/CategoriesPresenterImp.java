package mercari.victorlsn.mercari.presenters;

import android.widget.Toast;

import java.util.List;

import mercari.victorlsn.mercari.beans.Category;
import mercari.victorlsn.mercari.interfaces.BaseMVP;
import mercari.victorlsn.mercari.interfaces.CategoriesMVP;
import mercari.victorlsn.mercari.models.CategoriesModelImp;

/**
 * Created by victorlsn on 23/07/18.
 */

public class CategoriesPresenterImp implements CategoriesMVP.Presenter {
    private CategoriesMVP.View  view;
    private CategoriesMVP.Model model;

    public CategoriesPresenterImp() {
        model = new CategoriesModelImp(this);
    }

    @Override
    public boolean attachView(BaseMVP.View view) {
        if(null == view) return false;
        this.view = (CategoriesMVP.View) view;

        return true;
    }

    @Override
    public void requestCategories() {
        view.showProgressBar(true);
        model.getCategories();
    }

    @Override
    public void requestCategoriesSuccessfully(List<Category> categories) {
        view.showProgressBar(false);
        view.receiveCategoriesSuccessfully(categories);
    }

    @Override
    public void requestCategoriesFailure(String message) {
        view.showProgressBar(false);
        if(null == message || message.isEmpty()) return;
        view.showToast(message, Toast.LENGTH_SHORT);
        view.receiveCategoriesFailure();
    }
}
