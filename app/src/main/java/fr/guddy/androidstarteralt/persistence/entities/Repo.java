package fr.guddy.androidstarteralt.persistence.entities;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.os.Parcelable;

import java.io.Serializable;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;

@Entity
public interface Repo extends Observable, Parcelable, Persistable, Serializable {
    @Key
    @Generated
    long getBaseId();

    @Bindable
    int getUid();

    @Bindable
    String getName();

    @Bindable
    String getDescription();

    @Bindable
    String getUrl();

    @Bindable
    String getAvatarUrl();
}
