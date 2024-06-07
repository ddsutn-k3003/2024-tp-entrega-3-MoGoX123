package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class TemperaturaController {
  private final Fachada fachadaHeladera;

  public TemperaturaController(Fachada fachadaHeladera) {
    this.fachadaHeladera = fachadaHeladera;
  }

  public void addTemperatura(@NotNull Context context) {

    try {
      fachadaHeladera.temperatura(context.bodyAsClass(TemperaturaDTO.class));
      context.status(200).result("Temperatura registrada correctamente");
    }
    catch (NoSuchElementException e){
      context.status(400).result("Error de solicitud");
    }
    catch (Exception e){
      e.printStackTrace();
      context.status(500).result("Error interno");
    }

  }

  public void allTemperatura(@NotNull Context context) {

    try {
      Integer id = Integer.valueOf(context.pathParam("heladeraId"));
      context.status(200).json(fachadaHeladera.obtenerTemperaturas(id));
    }
    catch (NoSuchElementException e){
      context.status(400).result("Heladera no encontrada");
    }
    catch (Exception e){
      e.printStackTrace();
      context.status(500).result("Error interno");
    }

  }
}
