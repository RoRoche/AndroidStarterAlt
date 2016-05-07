package fr.guddy.androidstarteralt.mvp.repoDetail;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

import java.io.Serializable;

import icepick.Icepick;
import icepick.Icicle;

public class ViewStateRepoDetail implements RestorableViewState<ViewRepoDetail> {

    //region Data to retain
    @Icicle
    public Serializable data;
    //endregion

    //region ViewState
    @Override
    public void apply(final ViewRepoDetail poView, final boolean pbRetained) {
        if (data instanceof ModelRepoDetail) {
            final ModelRepoDetail loData = (ModelRepoDetail) data;
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
    public RestorableViewState<ViewRepoDetail> restoreInstanceState(final Bundle poIn) {
        if (poIn == null) {
            return null;
        }
        Icepick.restoreInstanceState(this, poIn);
        return this;
    }
    //endregion
}
