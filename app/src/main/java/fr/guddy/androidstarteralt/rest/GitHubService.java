package fr.guddy.androidstarteralt.rest;

import java.util.List;

import fr.guddy.androidstarteralt.rest.dto.DTORepo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {
    @GET("/users/{user}/repos")
    Call<List<DTORepo>> listRepos(@Path("user") final String psUser);
}
