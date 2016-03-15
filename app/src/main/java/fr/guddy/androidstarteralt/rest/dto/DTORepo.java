package fr.guddy.androidstarteralt.rest.dto;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class DTORepo {
    @JsonField(name = "id")
    public Integer id;
    @JsonField(name = "name")
    public String name;
    @JsonField(name = "owner")
    public DTOOwner owner;
    @JsonField(name = "description")
    public String description;
    @JsonField(name = "url")
    public String url;
    @JsonField(name = "language")
    public String language;
}
