package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class ViandaController {
  private final Fachada fachadaHeladera;

  public ViandaController(Fachada fachada) {
    this.fachadaHeladera = fachada;
  }


  public void addVianda(@NotNull Context context) {

    try {
      ViandaDTO viandaDTO = context.bodyAsClass(ViandaDTO.class);
      fachadaHeladera.depositar(Math.toIntExact(viandaDTO.getHeladeraId()), viandaDTO.getCodigoQR());
      context.status(200).result("Vianda depositada correctamente");
    }
    catch (NoSuchElementException e){
      e.printStackTrace();
      context.status(400).result("Error de solicitud " + e.getMessage());
    }
    catch (Exception e){
      e.printStackTrace();
      context.status(500).result("Error interno " + e.getMessage());
    }

  }

  public void deleteVianda(@NotNull Context context) {

    try {
      fachadaHeladera.retirar(context.bodyAsClass(RetiroDTO.class));
      context.status(200).result("Vianda retirada correctamente");
    }
    catch (NoSuchElementException e){
      context.status(400).result("Error de solicitud " + e.getMessage());
    }
    catch (Exception e){
      e.printStackTrace();
      context.status(500).result("Error interno " + e.getMessage());
    }
  }
}
