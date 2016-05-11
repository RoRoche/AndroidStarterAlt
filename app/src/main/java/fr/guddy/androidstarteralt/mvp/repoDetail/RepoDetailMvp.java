package fr.guddy.androidstarteralt.mvp.repoDetail;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

import java.io.Serializable;

import fr.guddy.androidstarteralt.persistence.entities.Repo;
import icepick.Icepick;
import icepick.Icicle;

public interface RepoDetailMvp {

    //region Model
    final class Model implements Serializable {
        public final Repo repo;

        public Model(final Repo poRepo) {
            repo = poRepo;
        }
    }
    //endregion

    //region View
    interface View extends MvpLceView<Model> {
        void showEmpty();
    }
    //endregion

    //region Presenter
    interface Presenter extends MvpPresenter<View> {
        void loadRepo(final long plRepoId, final boolean pbPullToRefresh);
    }
    //endregion

    //region ViewState
    final class ViewState implements RestorableViewState<View> {

        //region Data to retain
        @Icicle
        public Serializable data;
        //endregion

        //region ViewState
        @Override
        public void apply(final View poView, final boolean pbRetained) {
            if (data instanceof Model) {
                final Model loData = (Model) data;
                poView.setData(loData);
                if (loData.repo == null) {
                    poView.showEmpty();
                } else {
                    poView.showContent();
                }
            }
        }
        //endregion

        //region RestorableViewState
        @Override
        public void saveInstanceState(@NonNull final Bundle poOut) {
            Icepick.saveInstanceState(this, poOut);
        }

        @Override
        public RestorableViewState<View> restoreInstanceState(final Bundle poIn) {
            if (poIn == null) {
                return null;
            }
            Icepick.restoreInstanceState(this, poIn);
            return this;
        }
        //endregion
    }
    //endregion

}
