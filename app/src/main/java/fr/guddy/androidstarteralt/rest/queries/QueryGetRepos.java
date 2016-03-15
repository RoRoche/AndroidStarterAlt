package fr.guddy.androidstarteralt.rest.queries;

import com.j256.ormlite.dao.Dao;
import com.mobandme.android.transformer.Transformer;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import autodagger.AutoInjector;
import fr.guddy.androidstarteralt.ApplicationAndroidStarter;
import fr.guddy.androidstarteralt.BuildConfig;
import fr.guddy.androidstarteralt.bus.event.AbstractEventQueryDidFinish;
import fr.guddy.androidstarteralt.di.modules.ModuleTransformer;
import fr.guddy.androidstarteralt.persistence.dao.DAORepo;
import fr.guddy.androidstarteralt.persistence.entities.RepoEntity;
import fr.guddy.androidstarteralt.rest.GitHubService;
import fr.guddy.androidstarteralt.rest.dto.DTORepo;
import retrofit2.Call;
import retrofit2.Response;

@AutoInjector(ApplicationAndroidStarter.class)
public class QueryGetRepos extends AbstractQuery {
    private static final String TAG = QueryGetRepos.class.getSimpleName();
    private static final boolean DEBUG = true;

    //region Injected fields
    @Inject
    transient GitHubService gitHubService;
    @Inject
    transient EventBus eventBus;
    @Inject
    transient DAORepo daoRepo;
    @Inject
    @Named(ModuleTransformer.TRANSFORMER_REPO)
    transient Transformer transformerRepo;
    //endregion

    //region Fields
    public final boolean pullToRefresh;
    public final String user;
    public List<DTORepo> results;
    //endregion

    //region Constructor matching super
    protected QueryGetRepos(final String psUser, final boolean pbPullToRefresh) {
        super(Priority.MEDIUM);
        user = psUser;
        pullToRefresh = pbPullToRefresh;
    }
    //endregion

    //region Overridden method
    @Override
    public void inject() {
        ApplicationAndroidStarter.sharedApplication().componentApplication().inject(this);
    }

    @Override
    protected void execute() throws Exception {
        inject();

        final Call<List<DTORepo>> loCall = gitHubService.listRepos(user);
        final Response<List<DTORepo>> loExecute = loCall.execute();
        results = loExecute.body();

        final int liDeleted = daoRepo.deleteBuilder().delete();

        if (BuildConfig.DEBUG && DEBUG) {
            Logger.t(TAG).d("deleted row count = %d", liDeleted);
        }

        int liCount = 0;
        for (final DTORepo loDTORepo : results) {
            final RepoEntity loRepo = transformerRepo.transform(loDTORepo, RepoEntity.class);
            loRepo.avatarUrl = loDTORepo.owner.avatarUrl;
            final Dao.CreateOrUpdateStatus loStatus = daoRepo.createOrUpdate(loRepo);
            if (loStatus.isCreated() || loStatus.isUpdated()) {
                ++liCount;
            }
        }

        if (BuildConfig.DEBUG && DEBUG) {
            Logger.t(TAG).d("created or updated row count = %d", liCount);
        }
    }

    @Override
    protected void postEventQueryFinished() {
        final EventQueryGetReposDidFinish loEvent = new EventQueryGetReposDidFinish(this, mSuccess, mErrorType, mThrowable, pullToRefresh, results);
        eventBus.post(loEvent);
    }

    @Override
    public void postEventQueryFinishedNoNetwork() {
        final EventQueryGetReposDidFinish loEvent = new EventQueryGetReposDidFinish(this, false, AbstractEventQueryDidFinish.ErrorType.NETWORK_UNREACHABLE, null, pullToRefresh, null);
        eventBus.post(loEvent);
    }
    //endregion

    //region Dedicated EventQueryDidFinish
    public static final class EventQueryGetReposDidFinish extends AbstractEventQueryDidFinish<QueryGetRepos> {
        public final boolean pullToRefresh;
        public final List<DTORepo> results;

        public EventQueryGetReposDidFinish(final QueryGetRepos poQuery, final boolean pbSuccess, final ErrorType poErrorType, final Throwable poThrowable, final boolean pbPullToRefresh, final List<DTORepo> ploResults) {
            super(poQuery, pbSuccess, poErrorType, poThrowable);
            pullToRefresh = pbPullToRefresh;
            results = ploResults;
        }
    }
    //endregion
}
