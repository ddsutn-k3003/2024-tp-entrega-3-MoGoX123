package ar.edu.utn.dds.k3003.clients.interfaces;

import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface ViandasRetrofitClient {

    @GET("viandas/{qr}")
    Call<ViandaDTO> getVianda(@Path("qr") String qr);

    @PATCH("viandas/{qr}")
    Call<ViandaDTO> modifEstado(@Path("qr") String qr, @Body String estadoViandaEnum);

}
