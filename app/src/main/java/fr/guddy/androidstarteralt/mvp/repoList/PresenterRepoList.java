package fr.guddy.androidstarteralt.mvp.repoList;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import fr.guddy.androidstarteralt.ApplicationAndroidStarter;
import fr.guddy.androidstarteralt.persistence.dao.DAORepo;
import fr.guddy.androidstarteralt.persistence.entities.RepoEntity;
import fr.guddy.androidstarteralt.rest.queries.QueryFactory;
import fr.guddy.androidstarteralt.rest.queries.QueryGetRepos;
import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@AutoInjector(ApplicationAndroidStarter.class)
public class PresenterRepoList extends MvpBasePresenter<ViewRepoList> {

    //region Injected fields
    @Inject
    Context context;
    @Inject
    EventBus eventBus;
    @Inject
    DAORepo daoRepo;
    @Inject
    QueryFactory queryFactory;
    //endregion

    //region Fields
    private Subscription mSubscriptionGetRepos;
    //endregion

    //region Constructor
    public PresenterRepoList() {
        ApplicationAndroidStarter.sharedApplication().componentApplication().inject(this);
    }
    //endregion

    //region Overridden methods
    @Override
    public void attachView(final ViewRepoList poView) {
        super.attachView(poView);

        eventBus.register(this);
    }

    @Override
    public void detachView(final boolean pbRetainInstance) {
        super.detachView(pbRetainInstance);

        if (!pbRetainInstance) {
            unsubscribe();
        }

        eventBus.unregister(this);
    }
    //endregion

    //region Visible API
    public void loadRepos(final boolean pbPullToRefresh) {
        startQueryGetRepos(pbPullToRefresh);
    }
    //endregion

    //region Reactive job
    private void getRepos(final boolean pbPullToRefresh) {
        unsubscribe();

        final ViewRepoList loView = getView();
        if (loView == null) {
            return;
        }

        mSubscriptionGetRepos = rxGetRepos()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        (final List<RepoEntity> ploRepos) -> {
                            if (isViewAttached()) {
                                loView.setData(new ModelRepoList(ploRepos));
                                if (ploRepos == null || ploRepos.isEmpty()) {
                                    loView.showEmpty();
                                } else {
                                    loView.showContent();
                                }
                            }
                        },
                        // onError
                        (final Throwable poException) -> {
                            if (isViewAttached()) {
                                loView.showError(poException, pbPullToRefresh);
                            }
                            unsubscribe();
                        },
                        // onCompleted
                        this::unsubscribe
                );
    }
    //endregion

    //region Database job
    private Observable<List<RepoEntity>> rxGetRepos() {
        return daoRepo.rxQueryForAll();
    }
    //endregion

    //region Network job
    private void startQueryGetRepos(final boolean pbPullToRefresh) {
        final ViewRepoList loView = getView();
        if (isViewAttached() && loView != null) {
            loView.showLoading(pbPullToRefresh);
        }

        queryFactory.startQueryGetRepos(context, "RoRoche", pbPullToRefresh);
    }
    //endregion

    //region Event management
    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventQueryGetRepos(@NonNull final QueryGetRepos.EventQueryGetReposDidFinish poEvent) {
        if (poEvent.success) {
            getRepos(poEvent.pullToRefresh);
        } else {
            final ViewRepoList loView = getView();
            if (isViewAttached() && loView != null) {
                loView.showError(poEvent.throwable, poEvent.pullToRefresh);
            }
        }
    }
    //endregion

    //region Specific job
    private void unsubscribe() {
        if (mSubscriptionGetRepos != null && !mSubscriptionGetRepos.isUnsubscribed()) {
            mSubscriptionGetRepos.unsubscribe();
        }

        mSubscriptionGetRepos = null;
    }
    //endregion
}
