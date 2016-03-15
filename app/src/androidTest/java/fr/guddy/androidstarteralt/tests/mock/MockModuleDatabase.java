package fr.guddy.androidstarteralt.tests.mock;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fr.guddy.androidstarteralt.di.modules.ModuleDatabase;
import fr.guddy.androidstarteralt.persistence.entities.Models;
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.rx.RxSupport;
import io.requery.rx.SingleEntityStore;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;

@Module
public class MockModuleDatabase extends ModuleDatabase {

    @Provides
    @Singleton
    @Override
    public SingleEntityStore<Persistable> provideDataStore(@NonNull final Context poContext) {
        final DatabaseSource loSource = new DatabaseSource(poContext, Models.DEFAULT, "mock_android_starter_alt.sqlite", 1);
        final Configuration loConfiguration = loSource.getConfiguration();
        final SingleEntityStore<Persistable> loDataStore = RxSupport.toReactiveStore(new EntityDataStore<>(loConfiguration));
        return loDataStore;
    }
}
