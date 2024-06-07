package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.auxResource.DTOs.ViandaDTOAux;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
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
      ViandaDTOAux viandaDTOAux = context.bodyAsClass(ViandaDTOAux.class);
      fachadaHeladera.depositar(Math.toIntExact(viandaDTOAux.getHeladeraId()), viandaDTOAux.getQrVianda());
      context.status(200).result("Vianda depositada correctamente");
    }
    catch (NoSuchElementException e){
      e.printStackTrace();
      context.status(400).result("Error de solicitud");
    }
    catch (Exception e){
      e.printStackTrace();
      context.status(500).result("Error interno");
    }

  }

  public void deleteVianda(@NotNull Context context) {

    try {
      fachadaHeladera.retirar(context.bodyAsClass(RetiroDTO.class));
      context.status(200).result("Vianda retirada correctamente");
    }
    catch (NoSuchElementException e){
      context.status(400).result("Error de solicitud");
    }
    catch (Exception e){
      e.printStackTrace();
      context.status(500).result("Error interno");
    }
  }
}
