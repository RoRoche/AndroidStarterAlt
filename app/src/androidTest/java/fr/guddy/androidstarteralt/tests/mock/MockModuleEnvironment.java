package fr.guddy.androidstarteralt.tests.mock;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fr.guddy.androidstarteralt.Environment;
import fr.guddy.androidstarteralt.di.modules.ModuleEnvironment;

@Module
public class MockModuleEnvironment extends ModuleEnvironment {

    @Provides
    @Singleton
    public Environment provideEnvironment() {
        return Environment.TEST;
    }
}
