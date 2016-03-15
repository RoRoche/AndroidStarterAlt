package fr.guddy.androidstarteralt.mvp.repoList;

import java.io.Serializable;
import java.util.List;

import fr.guddy.androidstarteralt.persistence.entities.RepoEntity;

public final class ModelRepoList implements Serializable {

    public final List<RepoEntity> repos;

    public ModelRepoList(final List<RepoEntity> ploRepos) {
        repos = ploRepos;
    }
}
