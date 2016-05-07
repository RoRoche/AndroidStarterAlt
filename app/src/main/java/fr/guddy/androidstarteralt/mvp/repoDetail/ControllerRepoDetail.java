package fr.guddy.androidstarteralt.mvp.repoDetail;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hannesdorfmann.mosby.conductor.viewstate.MvpViewStateController;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.guddy.androidstarteralt.R;
import fr.guddy.androidstarteralt.persistence.entities.Repo;
import hugo.weaving.DebugLog;
import pl.aprilapps.switcher.Switcher;

public class ControllerRepoDetail
        extends MvpViewStateController<ViewRepoDetail, PresenterRepoDetail, ViewStateRepoDetail>
        implements ViewRepoDetail {

    //region Fields
    private final long mItemId;
    private Switcher mSwitcher;
    //endregion

    //region Injected views
    @Bind(R.id.ControllerRepoDetail_TextView_Description)
    TextView mTextViewDescription;
    @Bind(R.id.ControllerRepoDetail_TextView_Url)
    TextView mTextViewUrl;
    @Bind(R.id.ControllerRepoDetail_TextView_Empty)
    TextView mTextViewEmpty;
    @Bind(R.id.ControllerRepoDetail_TextView_Error)
    TextView mTextViewError;
    @Bind(R.id.ControllerRepoDetail_ProgressBar_Loading)
    ProgressBar mProgressBarLoading;
    @Bind(R.id.ControllerRepoDetail_ContentView)
    LinearLayout mContentView;
    //endregion

    //region Default constructor
    public ControllerRepoDetail(final long plItemId) {
        mItemId = plItemId;
    }

    public ControllerRepoDetail() {
        mItemId = -1;
    }
    //endregion

    //region Lifecycle
    @NonNull
    @Override
    protected View onCreateView(@NonNull final LayoutInflater poInflater, @NonNull final ViewGroup poContainer) {
        final View loView = poInflater.inflate(R.layout.controller_repo_detail, poContainer, false);

        ButterKnife.bind(this, loView);

        mSwitcher = new Switcher.Builder()
                .withEmptyView(mTextViewEmpty)
                .withProgressView(mProgressBarLoading)
                .withErrorView(mTextViewError)
                .withContentView(mContentView)
                .build();

        return loView;
    }
    //endregion

    //region MvpViewStateController
    @NonNull
    @Override
    public PresenterRepoDetail createPresenter() {
        return new PresenterRepoDetail();
    }
    //endregion

    //region ViewRepoDetail
    @Override
    public void showEmpty() {
        mSwitcher.showEmptyView();
    }

    @Override
    public void showContent() {
        mSwitcher.showContentView();
    }

    @Override
    public void showLoading(final boolean pbPullToRefresh) {
        mSwitcher.showProgressView();
    }

    @Override
    public void showError(final Throwable poThrowable, final boolean pbPullToRefresh) {
        mSwitcher.showErrorView();
    }

    @Override
    public void setData(final ModelRepoDetail poData) {
        configureViewWithRepo(poData.repo);
    }

    @Override
    public void loadData(final boolean pbPullToRefresh) {
        if (mItemId == -1) {
            mSwitcher.showErrorView();
        } else {
            getPresenter().loadRepo(mItemId, pbPullToRefresh);
        }
    }
    //endregion

    //region ViewState
    @DebugLog
    @NonNull
    @Override
    public ViewStateRepoDetail createViewState() {
        return new ViewStateRepoDetail();
    }

    @Override
    public void onViewStateInstanceRestored(final boolean pbInstanceStateRetained) {
    }

    @DebugLog
    @Override
    public void onNewViewStateInstance() {
        loadData(false);
    }
    //endregion

    //region Specific method
    private void configureViewWithRepo(@NonNull final Repo poRepo) {
        mTextViewDescription.setText(poRepo.getDescription());
        mTextViewUrl.setText(poRepo.getUrl());
    }
    //endregion
}
