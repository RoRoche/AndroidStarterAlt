package fr.guddy.androidstarteralt.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public class ModuleContext {
    private final Context mContext;

    public ModuleContext(@NonNull final Context poContext) {
        mContext = poContext;
    }

    @Provides
    public Context provideContext() {
        return mContext;
    }
}
