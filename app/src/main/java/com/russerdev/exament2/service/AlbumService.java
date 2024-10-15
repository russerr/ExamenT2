package com.russerdev.exament2.service;

import com.russerdev.exament2.entity.Album;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AlbumService {

    @GET("albums")
    public abstract Call<List<Album>> listAlbums();
}
