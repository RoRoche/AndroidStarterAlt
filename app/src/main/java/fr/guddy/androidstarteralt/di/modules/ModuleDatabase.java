package fr.guddy.androidstarteralt.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import net.orange_box.storebox.StoreBox;
import net.sqlcipher.database.SQLiteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fr.guddy.androidstarteralt.persistence.Preferences;
import fr.guddy.androidstarteralt.persistence.entities.Models;
import io.requery.Persistable;
import io.requery.android.sqlcipher.SqlCipherDatabaseSource;
import io.requery.android.sqlite.DatabaseProvider;
import io.requery.rx.RxSupport;
import io.requery.rx.SingleEntityStore;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;

@Module
public class ModuleDatabase {

    @Provides
    @Singleton
    public SingleEntityStore<Persistable> provideDataStore(@NonNull final Context poContext) {
//        final DatabaseProvider<SQLiteDatabase> loSource = new DatabaseSource(poContext, Models.DEFAULT, "android_starter_alt.sqlite", 1);
        final DatabaseProvider<SQLiteDatabase> loSource = new SqlCipherDatabaseSource(poContext, Models.DEFAULT, "android_start_alt_requery_sqlcipher.sqlite", "android_starter_alt", 1);
        final Configuration loConfiguration = loSource.getConfiguration();
        final SingleEntityStore<Persistable> loDataStore = RxSupport.toReactiveStore(new EntityDataStore<>(loConfiguration));
        return loDataStore;
    }

    @Provides
    @Singleton
    public Preferences providePreferences(@NonNull final Context poContext) {
        return StoreBox.create(poContext, Preferences.class);
    }
}
