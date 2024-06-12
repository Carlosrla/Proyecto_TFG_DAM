package com.example.tfg_carlosramos.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://172.20.10.2:4000/"; // Cambia la URL según sea necesario
    private static Retrofit retrofit = null;

    /**
     * Obtiene una instancia de Retrofit.
     * @return La instancia de Retrofit.
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * Obtiene una instancia del servicio API.
     * @return La instancia de ApiService.
     */
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}
//http://192.168.1.149:4000/
