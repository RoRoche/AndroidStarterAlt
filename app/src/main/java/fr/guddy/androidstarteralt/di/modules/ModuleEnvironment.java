package fr.guddy.androidstarteralt.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fr.guddy.androidstarteralt.BuildConfig;
import fr.guddy.androidstarteralt.IEnvironment;

@Module
public class ModuleEnvironment {

    @Provides
    @Singleton
    public IEnvironment provideEnvironment() {
        return BuildConfig.ENVIRONMENT;
    }
}
