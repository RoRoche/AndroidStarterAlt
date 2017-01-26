package fr.guddy.androidstarteralt.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fr.guddy.androidstarteralt.IEnvironment;
import fr.guddy.androidstarteralt.rest.GitHubService;
import fr.guddy.androidstarteralt.rest.errorHandling.ErrorHandlingExecutorCallAdapterFactory;
import io.palaima.debugdrawer.picasso.PicassoModule;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@Module
public class ModuleRest {

    private final String mBaseUrl;

    public ModuleRest(@NonNull final String psBaseUrl) {
        mBaseUrl = psBaseUrl;
    }

    public ModuleRest() {
        mBaseUrl = null;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(@NonNull final IEnvironment poEnvironment) {
        final HttpLoggingInterceptor loHttpLoggingInterceptor = new HttpLoggingInterceptor();
        loHttpLoggingInterceptor.setLevel(poEnvironment.getHttpLoggingInterceptorLevel());
        return new OkHttpClient.Builder()
                .addInterceptor(loHttpLoggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    public GitHubService provideGithubService(@NonNull final OkHttpClient poOkHttpClient) {
        final Retrofit loRetrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(poOkHttpClient)
                .addConverterFactory(LoganSquareConverterFactory.create())
                .addCallAdapterFactory(new ErrorHandlingExecutorCallAdapterFactory(new ErrorHandlingExecutorCallAdapterFactory.MainThreadExecutor()))
                .build();
        return loRetrofit.create(GitHubService.class);
    }

    @Provides
    @Singleton
    public Merlin provideMerlin(@NonNull final Context poContext) {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .withLogging(true)
                .build(poContext);
    }

    @Provides
    @Singleton
    public MerlinsBeard provideMerlinsBeard(@NonNull final Context poContext) {
        return MerlinsBeard.from(poContext);
    }

    @Provides
    @Singleton
    public Picasso providePicasso(@NonNull final Context poContext) {
        final Picasso loPicasso = Picasso.with(poContext);
        loPicasso.setIndicatorsEnabled(true);
        loPicasso.setLoggingEnabled(true);
        return loPicasso;
    }

    @Provides
    @Singleton
    public PicassoModule providePicassoModule(@NonNull final Picasso poPicasso) {
        return new PicassoModule(poPicasso);
    }
}
