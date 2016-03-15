package fr.guddy.androidstarteralt.persistence.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.mobandme.android.transformer.compiler.Mappable;
import com.mobandme.android.transformer.compiler.Mapped;

import java.io.Serializable;

import fr.guddy.androidstarteralt.persistence.dao.DAORepo;
import fr.guddy.androidstarteralt.rest.dto.DTORepo;

@Mappable(with = DTORepo.class)
@DatabaseTable(tableName = "REPO", daoClass = DAORepo.class)
public class RepoEntity extends AbstractOrmLiteEntity implements Serializable {
    @Mapped
    @DatabaseField
    public Integer id;

    @Mapped
    @DatabaseField
    public String name;

    @Mapped
    @DatabaseField
    public String description;

    @Mapped
    @DatabaseField
    public String url;

    @DatabaseField
    public String avatarUrl;
}
