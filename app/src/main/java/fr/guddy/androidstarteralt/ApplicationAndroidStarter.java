package fr.guddy.androidstarteralt;

import android.app.Application;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.novoda.merlin.Merlin;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

import javax.inject.Inject;
import javax.inject.Singleton;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import fr.guddy.androidstarteralt.di.modules.ModuleAsync;
import fr.guddy.androidstarteralt.di.modules.ModuleBus;
import fr.guddy.androidstarteralt.di.modules.ModuleContext;
import fr.guddy.androidstarteralt.di.modules.ModuleDatabase;
import fr.guddy.androidstarteralt.di.modules.ModuleEnvironment;
import fr.guddy.androidstarteralt.di.modules.ModuleRest;

@AutoComponent(
        modules = {
                ModuleAsync.class,
                ModuleBus.class,
                ModuleContext.class,
                ModuleDatabase.class,
                ModuleEnvironment.class,
                ModuleRest.class,
        }
)
@Singleton
@AutoInjector(ApplicationAndroidStarter.class)
public class ApplicationAndroidStarter extends Application {
    private static final String TAG = ApplicationAndroidStarter.class.getSimpleName();

    //region Singleton
    protected static ApplicationAndroidStarter sSharedApplication;

    public static ApplicationAndroidStarter sharedApplication() {
        return sSharedApplication;
    }
    //endregion

    //region Fields (components)
    protected ApplicationAndroidStarterComponent mComponentApplication;
    //endregion

    //region Injected fields
    @Inject
    Merlin merlin;
    //endregion

    //region Overridden methods
    @Override
    public void onCreate() {
        super.onCreate();
        sSharedApplication = this;

        Logger.init(TAG)
                .logLevel(LogLevel.FULL);

        Hawk.init(this).build();

        Stetho.initializeWithDefaults(this);

        try {
            buildComponent();
        } catch (final GeneralSecurityException poException) {
            if (BuildConfig.DEBUG) {
                Logger.t(TAG).e(poException, null);
                poException.printStackTrace();
            }
        }

        mComponentApplication.inject(this);
        merlin.bind();

        final StrictMode.ThreadPolicy loStrictModeThreadPolicy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyDeath()
                .build();
        StrictMode.setThreadPolicy(loStrictModeThreadPolicy);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        sSharedApplication = null;
        merlin.unbind();
    }
    //endregion

    //region Protected methods
    protected void buildComponent() throws GeneralSecurityException {
        final String lsKey = getString(R.string.base_url_key);
        final String lsValue = getString(R.string.base_url_value);

        final String lsBaseUrl = AESCrypt.decrypt(lsKey, lsValue);

        mComponentApplication = DaggerApplicationAndroidStarterComponent.builder()
                .moduleAsync(new ModuleAsync())
                .moduleBus(new ModuleBus())
                .moduleContext(new ModuleContext(getApplicationContext()))
                .moduleDatabase(new ModuleDatabase())
                .moduleEnvironment(new ModuleEnvironment())
                .moduleRest(new ModuleRest(lsBaseUrl))
                .build();
    }
    //endregion

    //region Getters
    public ApplicationAndroidStarterComponent componentApplication() {
        return mComponentApplication;
    }
    //endregion
}
