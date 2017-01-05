package fr.guddy.androidstarteralt.mvp.repoList;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.RouterTransaction;
import com.hannesdorfmann.mosby.conductor.viewstate.MvpViewStateController;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fr.guddy.androidstarteralt.R;
import fr.guddy.androidstarteralt.mvp.changehandlers.CircularRevealChangeHandlerCompat;
import fr.guddy.androidstarteralt.mvp.repoDetail.ControllerRepoDetail;
import fr.guddy.androidstarteralt.persistence.entities.Repo;
import fr.guddy.androidstarteralt.persistence.entities.RepoEntity;
import hugo.weaving.DebugLog;
import icepick.Icepick;
import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.utils.ViewEventListener;
import pl.aprilapps.switcher.Switcher;

public class ControllerRepoList
        extends MvpViewStateController<RepoListMvp.View, RepoListMvp.Presenter, RepoListMvp.ViewState>
        implements RepoListMvp.View, ViewEventListener<Repo>, SwipeRefreshLayout.OnRefreshListener {

    //region Injected views
    @BindView(R.id.ControllerRepoList_ProgressBar_Loading)
    ProgressBar mProgressBarLoading;
    @BindView(R.id.ControllerRepoList_RecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.ControllerRepoList_SwipeRefreshLayout_Empty)
    SwipeRefreshLayout mSwipeRefreshLayoutEmpty;
    @BindView(R.id.ControllerRepoList_SwipeRefreshLayout_Error)
    SwipeRefreshLayout mSwipeRefreshLayoutError;
    @BindView(R.id.ControllerRepoList_SwipeRefreshLayout_Content)
    SwipeRefreshLayout mSwipeRefreshLayoutContent;
    @BindViews({
            R.id.ControllerRepoList_SwipeRefreshLayout_Empty,
            R.id.ControllerRepoList_SwipeRefreshLayout_Error,
            R.id.ControllerRepoList_SwipeRefreshLayout_Content
    })
    List<SwipeRefreshLayout> mSwipeRefreshLayouts;
    //endregion

    //region Fields
    static final ButterKnife.Setter<SwipeRefreshLayout, SwipeRefreshLayout.OnRefreshListener> SET_LISTENER =
            (@NonNull final SwipeRefreshLayout poView, @NonNull final SwipeRefreshLayout.OnRefreshListener poListener, final int piIndex)
                    ->
                    poView.setOnRefreshListener(poListener);

    static final ButterKnife.Action<SwipeRefreshLayout> STOP_REFRESHING =
            (@NonNull final SwipeRefreshLayout poView, final int piIndex)
                    ->
                    poView.setRefreshing(false);

    private Switcher mSwitcher;
    private Unbinder mUnbinder;
    //endregion

    //region Constructor
    public ControllerRepoList() {
    }
    //endregion

    //region Lifecycle
    @NonNull
    @Override
    protected View onCreateView(@NonNull final LayoutInflater poInflater, @NonNull final ViewGroup poContainer) {
        final View loView = poInflater.inflate(R.layout.controller_repo_list, poContainer, false);
        mUnbinder = ButterKnife.bind(this, loView);

        ButterKnife.apply(mSwipeRefreshLayouts, SET_LISTENER, this);

        mSwitcher = new Switcher.Builder()
                .withEmptyView(mSwipeRefreshLayoutEmpty)
                .withProgressView(mProgressBarLoading)
                .withErrorView(mSwipeRefreshLayoutError)
                .withContentView(mSwipeRefreshLayoutContent)
                .build();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return loView;
    }

    @Override
    protected void onRestoreInstanceState(@NonNull final Bundle poSavedInstanceState) {
        super.onRestoreInstanceState(poSavedInstanceState);
        Icepick.restoreInstanceState(this, poSavedInstanceState);
    }

    @Override
    protected void onRestoreViewState(@NonNull final View poView, @NonNull final Bundle poSavedViewState) {
        super.onRestoreViewState(poView, poSavedViewState);
        Icepick.restoreInstanceState(this, poSavedViewState);
    }

    @Override
    protected void onAttach(@NonNull final View poView) {
        super.onAttach(poView);
    }

    @Override
    protected void onDestroyView(final View poView) {
        mUnbinder.unbind();
        super.onDestroyView(poView);
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle poOutState) {
        super.onSaveInstanceState(poOutState);
        Icepick.saveInstanceState(this, poOutState);
    }
    //endregion

    //region ViewEventListener
    @DebugLog
    @Override
    public void onViewEvent(final int piActionID, final Repo poRepo, final int piPosition, final View poView) {
        if (piActionID == CellRepo.ROW_PRESSED) {
            final ControllerRepoDetail loVC = new ControllerRepoDetail(poRepo.getBaseId());
            final ControllerChangeHandler loChangeHandler = new CircularRevealChangeHandlerCompat(poView, mRecyclerView);
            final RouterTransaction loTransaction = RouterTransaction.with(loVC)
                    .pushChangeHandler(loChangeHandler)
                    .popChangeHandler(loChangeHandler);
            getRouter().pushController(loTransaction);
        }
    }
    //endregion

    //region MvpViewStateController
    @DebugLog
    @NonNull
    @Override
    public RepoListMvp.Presenter createPresenter() {
        return new PresenterRepoList();
    }

    @DebugLog
    @NonNull
    @Override
    public RepoListMvp.ViewState createViewState() {
        return new RepoListMvp.ViewState();
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

    //region ViewRepoList
    @DebugLog
    @Override
    public void showEmpty() {
        ButterKnife.apply(mSwipeRefreshLayouts, STOP_REFRESHING);
        mSwitcher.showEmptyView();
    }
    //endregion

    //region MvpLceView
    @DebugLog
    @Override
    public void showLoading(final boolean pbPullToRefresh) {
        if (!pbPullToRefresh) {
            mSwitcher.showProgressView();
        }
    }

    @DebugLog
    @Override
    public void showContent() {
        ButterKnife.apply(mSwipeRefreshLayouts, STOP_REFRESHING);
        mSwitcher.showContentView();
    }

    @DebugLog
    @Override
    public void showError(final Throwable poThrowable, final boolean pbPullToRefresh) {
        ButterKnife.apply(mSwipeRefreshLayouts, STOP_REFRESHING);
        mSwitcher.showErrorView();
    }

    @DebugLog
    @Override
    public void setData(final RepoListMvp.Model poData) {
        viewState.data = poData;

        SmartAdapter.items(poData.repos)
                .map(RepoEntity.class, CellRepo.class)
                .listener(ControllerRepoList.this)
                .into(mRecyclerView);
    }

    @DebugLog
    @Override
    public void loadData(final boolean pbPullToRefresh) {
        getPresenter().loadRepos(pbPullToRefresh);
    }
    //endregion

    //region SwipeRefreshLayout.OnRefreshListener
    @Override
    public void onRefresh() {
        loadData(true);
    }
    //endregion
}
