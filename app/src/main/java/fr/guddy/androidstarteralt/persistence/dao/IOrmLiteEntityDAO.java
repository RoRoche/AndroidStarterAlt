package fr.guddy.androidstarteralt.persistence.dao;

import com.j256.ormlite.dao.Dao;

import fr.guddy.androidstarteralt.persistence.entities.AbstractOrmLiteEntity;

/**
 * An interface to contract implementation with a {@link Long} ID type.
 */
public interface IOrmLiteEntityDAO<DataType extends AbstractOrmLiteEntity> extends Dao<DataType, Long> {
}
