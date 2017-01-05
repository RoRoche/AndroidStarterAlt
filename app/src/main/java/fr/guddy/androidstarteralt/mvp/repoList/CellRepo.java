package fr.guddy.androidstarteralt.mvp.repoList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.guddy.androidstarteralt.ApplicationAndroidStarter;
import fr.guddy.androidstarteralt.R;
import fr.guddy.androidstarteralt.persistence.entities.RepoEntity;
import io.nlopez.smartadapters.views.BindableFrameLayout;

@AutoInjector(ApplicationAndroidStarter.class)
public class CellRepo extends BindableFrameLayout<RepoEntity> {
    //region Interactions
    public static final int ROW_PRESSED = 0;
    //endregion

    //region Injected members
    @Inject
    Picasso mPicasso;
    //endregion

    //region Injected views
    @BindView(R.id.CellRepo_TextView)
    TextView mTextView;
    @BindView(R.id.CellRepo_ImageView_Avatar)
    ImageView mImageViewAvatar;
    //endregion

    //region Constructor
    public CellRepo(@NonNull final Context poContext) {
        super(poContext);
        ApplicationAndroidStarter.sharedApplication().componentApplication().inject(this);
    }
    //endregion

    //region Overridden methods
    @Override
    public int getLayoutId() {
        return R.layout.cell_repo;
    }

    @Override
    public void bind(@NonNull final RepoEntity poRepo) {
        mTextView.setText(poRepo.getUrl());

        final RequestCreator loRequest = mPicasso.load(poRepo.getAvatarUrl());
        if (loRequest != null) {
            loRequest
                    .placeholder(R.drawable.git_icon)
                    .error(R.drawable.git_icon)
                    .into(mImageViewAvatar);
        }

        setOnClickListener((final View poView) ->
                notifyItemAction(ROW_PRESSED)
        );
    }

    @Override
    public void onViewInflated() {
        super.onViewInflated();
        ButterKnife.bind(this);
    }
    //endregion
}
