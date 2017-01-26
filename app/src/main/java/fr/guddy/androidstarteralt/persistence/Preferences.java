package fr.guddy.androidstarteralt.persistence;

import android.support.annotation.NonNull;

import net.orange_box.storebox.annotations.method.KeyByString;

public interface Preferences {
    @KeyByString("key_username")
    String getUsername();

    @KeyByString("key_username")
    void setUsername(@NonNull final String psValue);
}
