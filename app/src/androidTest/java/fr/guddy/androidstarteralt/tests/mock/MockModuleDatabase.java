package fr.guddy.androidstarteralt.tests.mock;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fr.guddy.androidstarteralt.di.modules.ModuleDatabase;
import fr.guddy.androidstarteralt.persistence.DatabaseHelperAndroidStarter;

@Module
public class MockModuleDatabase extends ModuleDatabase {

    @Provides
    @Singleton
    public DatabaseHelperAndroidStarter provideDatabaseHelperAndroidStarter(@NonNull final Context poContext) {
        return new MockDatabaseHelperAndroidStarter(poContext);
    }
}
