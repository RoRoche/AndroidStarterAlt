package fr.guddy.androidstarteralt.mvp.repoList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.birbit.android.jobqueue.JobManager;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.novoda.merlin.MerlinsBeard;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

import autodagger.AutoInjector;
import fr.guddy.androidstarteralt.ApplicationAndroidStarter;
import fr.guddy.androidstarteralt.persistence.Preferences;
import fr.guddy.androidstarteralt.persistence.entities.RepoEntity;
import fr.guddy.androidstarteralt.rest.queries.QueryGetRepos;
import hugo.weaving.DebugLog;
import io.requery.Persistable;
import io.requery.rx.SingleEntityStore;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@AutoInjector(ApplicationAndroidStarter.class)
public class PresenterRepoList extends MvpBasePresenter<RepoListMvp.View> implements RepoListMvp.Presenter {

    //region Injected fields
    @Inject
    Context context;
    @Inject
    EventBus eventBus;
    @Inject
    SingleEntityStore<Persistable> dataStore;
    @Inject
    MerlinsBeard merlinsBeard;
    @Inject
    JobManager jobManager;
    @Inject
    Preferences preferences;
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
    public void attachView(final RepoListMvp.View poView) {
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

    //region Specific job
    private void unsubscribe() {
        if (mSubscriptionGetRepos != null && !mSubscriptionGetRepos.isUnsubscribed()) {
            mSubscriptionGetRepos.unsubscribe();
        }

        mSubscriptionGetRepos = null;
    }
    //endregion

    //region RepoListMvp.Presenter
    @Override
    public void loadRepos(final boolean pbPullToRefresh) {
        startQueryGetRepos(pbPullToRefresh);
    }
    //endregion

    //region Network job
    private void startQueryGetRepos(final boolean pbPullToRefresh) {
        final RepoListMvp.View loView = getView();
        if (isViewAttached() && loView != null) {
            loView.showLoading(pbPullToRefresh);
        }

        // "standard" preferences
        if (TextUtils.isEmpty(preferences.getUsername())) {
            preferences.setUsername("RoRoche");
        }
        // vs. Hawk
        if (!Hawk.contains("key_username")) {
            Hawk.put("key_username", "RoRoche");
        }

        //final QueryGetRepos loQuery = new QueryGetRepos(preferences.getUsername(), pbPullToRefresh);
        final QueryGetRepos loQuery = new QueryGetRepos(Hawk.get("key_username"), pbPullToRefresh);
        // If query requires network, and if network is unreachable, and if the query must not persist
        if (loQuery.requiresNetwork() &&
                !merlinsBeard.isConnected() &&
                !loQuery.isPersistent()) {
            // then, we post an event to notify the job could not be done because of network connectivity
            loQuery.inject();
            loQuery.postEventQueryFinishedNoNetwork();
        } else {
            // otherwise, we can add the job
            jobManager.addJobInBackground(loQuery);
        }
    }
    //endregion

    //region Event management
    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventQueryGetRepos(@NonNull final QueryGetRepos.EventQueryGetReposDidFinish poEvent) {
        if (poEvent.success) {
            getRepos(poEvent.pullToRefresh);
        } else {
            final RepoListMvp.View loView = getView();
            if (isViewAttached() && loView != null) {
                loView.showError(poEvent.throwable, poEvent.pullToRefresh);
            }
        }
    }
    //endregion

    //region Reactive job
    private void getRepos(final boolean pbPullToRefresh) {
        unsubscribe();

        final RepoListMvp.View loView = getView();
        if (loView == null) {
            return;
        }

        final ArrayList<RepoEntity> lloRepos = new ArrayList<>();
        mSubscriptionGetRepos = dataStore.select(RepoEntity.class)
                .get()
                .toObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        (final RepoEntity poRepo) -> lloRepos.add(poRepo),
                        // onError
                        (final Throwable poException) -> {
                            if (isViewAttached()) {
                                loView.showError(poException, pbPullToRefresh);
                            }
                            unsubscribe();
                        },
                        // onCompleted
                        () -> {
                            if (isViewAttached()) {
                                loView.setData(new RepoListMvp.Model(lloRepos));
                                if (lloRepos.isEmpty()) {
                                    loView.showEmpty();
                                } else {
                                    loView.showContent();
                                }
                            }
                            unsubscribe();
                        }
                );
    }
    //endregion
}
