package fr.guddy.androidstarteralt.mvp.repoDetail;

import java.io.Serializable;

import fr.guddy.androidstarteralt.persistence.entities.Repo;

public final class ModelRepoDetail implements Serializable {
    public final Repo repo;

    public ModelRepoDetail(final Repo poRepo) {
        repo = poRepo;
    }
}
