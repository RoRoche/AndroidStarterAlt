package fr.guddy.androidstarteralt.mvp.repoDetail;

import fr.guddy.androidstarteralt.persistence.entities.RepoEntity;

public final class ModelRepoDetail {
    public final RepoEntity repo;

    public ModelRepoDetail(final RepoEntity poRepo) {
        repo = poRepo;
    }
}
