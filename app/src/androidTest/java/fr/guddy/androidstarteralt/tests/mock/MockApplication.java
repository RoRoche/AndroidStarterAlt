package fr.guddy.androidstarteralt.tests.mock;

import fr.guddy.androidstarteralt.ApplicationAndroidStarter;
import fr.guddy.androidstarteralt.DaggerApplicationAndroidStarterComponent;
import fr.guddy.androidstarteralt.di.modules.ModuleAsync;
import fr.guddy.androidstarteralt.di.modules.ModuleBus;
import fr.guddy.androidstarteralt.di.modules.ModuleContext;
import fr.guddy.androidstarteralt.di.modules.ModuleEnvironment;
import fr.guddy.androidstarteralt.di.modules.ModuleTransformer;

public class MockApplication extends ApplicationAndroidStarter {

    //region Fields
    private ModuleBus mModuleBus;
    private MockModuleRest mModuleRest;
    private ModuleEnvironment mModuleEnvironment;
    //endregion

    //region Singleton
    protected static MockApplication sSharedMockApplication;

    public static MockApplication sharedMockApplication() {
        return sSharedMockApplication;
    }
    //endregion

    //region Lifecycle
    @Override
    public void onCreate() {
        super.onCreate();
        sSharedMockApplication = this;
    }
    //endregion

    //region Overridden method
    @Override
    protected void buildComponent() {
        mModuleBus = new ModuleBus();
        mModuleRest = new MockModuleRest();
        mModuleEnvironment = new MockModuleEnvironment();

        mComponentApplication = DaggerApplicationAndroidStarterComponent.builder()
                .moduleAsync(new ModuleAsync())
                .moduleBus(mModuleBus)
                .moduleContext(new ModuleContext(getApplicationContext()))
                .moduleDatabase(new MockModuleDatabase())
                .moduleEnvironment(mModuleEnvironment)
                .moduleRest(mModuleRest)
                .moduleTransformer(new ModuleTransformer())
                .build();
    }
    //endregion

    //region Getters
    public MockModuleRest getModuleRest() {
        return mModuleRest;
    }

    public ModuleBus getModuleBus() {
        return mModuleBus;
    }

    public ModuleEnvironment getModuleEnvironment() {
        return mModuleEnvironment;
    }
    //endregion
}
