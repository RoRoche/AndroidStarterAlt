package fr.guddy.androidstarteralt.mvp.repoList;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

public interface ViewRepoList extends MvpLceView<ModelRepoList> {
    void showEmpty();
}