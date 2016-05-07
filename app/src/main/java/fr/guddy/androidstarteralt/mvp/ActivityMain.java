package fr.guddy.androidstarteralt.mvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;
import fr.guddy.androidstarteralt.ApplicationAndroidStarter;
import fr.guddy.androidstarteralt.IEnvironment;
import fr.guddy.androidstarteralt.R;
import fr.guddy.androidstarteralt.mvp.repoList.ControllerRepoList;
import io.palaima.debugdrawer.DebugDrawer;
import io.palaima.debugdrawer.commons.BuildModule;
import io.palaima.debugdrawer.commons.DeviceModule;
import io.palaima.debugdrawer.commons.NetworkModule;
import io.palaima.debugdrawer.commons.SettingsModule;
import io.palaima.debugdrawer.fps.FpsModule;
import io.palaima.debugdrawer.picasso.PicassoModule;
import io.palaima.debugdrawer.scalpel.ScalpelModule;
import jp.wasabeef.takt.Takt;

@AutoInjector(ApplicationAndroidStarter.class)
public class ActivityMain extends AppCompatActivity {

    //region Fields
    private DebugDrawer mDebugDrawer;
    private Router mRouter;
    //endregion

    //region Injected views
    @Bind(R.id.ActivityMain_ViewGroup_Container)
    ViewGroup mViewGroupContainer;
    //endregion

    //region Injected fields
    @Inject
    Picasso mPicasso;
    @Inject
    IEnvironment mEnvironment;
    //endregion

    //region Lifecycle
    @Override
    protected void onCreate(final Bundle poSavedInstanceState) {
        super.onCreate(poSavedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationAndroidStarter.sharedApplication().componentApplication().inject(this);

        ButterKnife.bind(this);

        if (mEnvironment.isDebugDrawerEnabled()) {
            mDebugDrawer = new DebugDrawer.Builder(this).modules(
                    new FpsModule(Takt.stock(getApplication())),
                    new ScalpelModule(this),
                    new PicassoModule(mPicasso),
                    new DeviceModule(this),
                    new BuildModule(this),
                    new NetworkModule(this),
                    new SettingsModule(this)
            ).build();
        }

        mRouter = Conductor.attachRouter(this, mViewGroupContainer, poSavedInstanceState);
        if (!mRouter.hasRootController()) {
            mRouter.setRoot(new ControllerRepoList());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mDebugDrawer != null) {
            mDebugDrawer.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDebugDrawer != null) {
            mDebugDrawer.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mDebugDrawer != null) {
            mDebugDrawer.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mDebugDrawer != null) {
            mDebugDrawer.onStop();
        }
    }

    @Override
    public void onBackPressed() {
        if (!mRouter.handleBack()) {
            super.onBackPressed();
        }
    }
    //endregion
}
