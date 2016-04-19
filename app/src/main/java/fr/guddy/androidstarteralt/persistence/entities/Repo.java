package fr.guddy.androidstarteralt.persistence.entities;

import android.os.Parcelable;

import java.io.Serializable;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;

@Entity
public interface Repo extends Parcelable, Persistable, Serializable {
    @Key
    @Generated
    long getBaseId();

    int getUid();

    String getName();

    String getDescription();

    String getUrl();

    String getAvatarUrl();
}
