package fr.guddy.androidstarteralt.mvp.repoDetail;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import autodagger.AutoInjector;
import fr.guddy.androidstarteralt.ApplicationAndroidStarter;
import fr.guddy.androidstarteralt.persistence.entities.Repo;
import fr.guddy.androidstarteralt.persistence.entities.RepoEntity;
import io.requery.Persistable;
import io.requery.rx.SingleEntityStore;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@AutoInjector(ApplicationAndroidStarter.class)
public final class PresenterRepoDetail extends MvpBasePresenter<ViewRepoDetail> {

    //region Injected fields
    @Inject
    SingleEntityStore<Persistable> dataStore;
    //endregion

    //region Fields
    private Subscription mSubscriptionGetRepo;
    //endregion

    //region Constructor
    public PresenterRepoDetail() {
        ApplicationAndroidStarter.sharedApplication().componentApplication().inject(this);
    }
    //endregion


    //region Overridden method
    @Override
    public void detachView(final boolean pbRetainInstance) {
        super.detachView(pbRetainInstance);
        if (!pbRetainInstance) {
            unsubscribe();
        }
    }
    //endregion

    //region Visible API
    public void loadRepo(final long plRepoId, final boolean pbPullToRefresh) {
        final ViewRepoDetail loView = getView();
        if (isViewAttached() && loView != null) {
            loView.showLoading(pbPullToRefresh);
        }
        getRepo(plRepoId);
    }
    //endregion

    //region Reactive job
    private void getRepo(final long plRepoId) {
        unsubscribe();

        final ViewRepoDetail loView = getView();
        if (loView == null) {
            return;
        }

        mSubscriptionGetRepo = dataStore.select(RepoEntity.class)
                .where(RepoEntity.BASE_ID.eq(plRepoId))
                .get()
                .toObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        (final Repo poRepo) -> {
                            if (isViewAttached()) {
                                loView.setData(new ModelRepoDetail(poRepo));
                                if (poRepo == null) {
                                    loView.showEmpty();
                                } else {
                                    loView.showContent();
                                }
                            }
                        },
                        // onError
                        (final Throwable poException) -> {
                            if (isViewAttached()) {
                                loView.showError(poException, false);
                            }
                            unsubscribe();
                        },
                        // onCompleted
                        this::unsubscribe
                );
    }
    //endregion

    //region Specific job
    private void unsubscribe() {
        if (mSubscriptionGetRepo != null && !mSubscriptionGetRepo.isUnsubscribed()) {
            mSubscriptionGetRepo.unsubscribe();
        }

        mSubscriptionGetRepo = null;
    }
    //endregion
}
