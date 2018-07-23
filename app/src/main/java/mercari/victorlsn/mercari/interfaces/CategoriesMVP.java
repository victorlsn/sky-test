package mercari.victorlsn.mercari.interfaces;

import java.util.List;

import mercari.victorlsn.mercari.beans.Category;

/**
 * Created by victorlsn on 23/07/18.
 */

public interface CategoriesMVP {
    interface Model{
        void getCategories();
    }
    interface Presenter extends BaseMVP.Presenter{
        void requestCategories();
        void requestCategoriesSuccessfully(List<Category> categories);
        void requestCategoriesFailure(String message);

    }
    interface View extends BaseMVP.View{
        void receiveCategoriesSuccessfully(List<Category> categories);
    }
}
