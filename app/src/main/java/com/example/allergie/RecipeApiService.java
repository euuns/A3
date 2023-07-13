package com.example.allergie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecipeApiService {
    @GET("RCP_PARTS_DTLS = \"{food}\"&RCP_PAT2 = \"{kind}\"")

    Call<String> createGET(@Path("food")String food, @Path("kind")String kind);
}
