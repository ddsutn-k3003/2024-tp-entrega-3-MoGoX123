package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class HeladeraController {


  private final Fachada fachada;

  public HeladeraController(Fachada fachada) {
    this.fachada = fachada;
  }

  public void getHeladeras(@NotNull Context ctx) {

    try {
      ctx.status(200).json(fachada.obtenerHeladeras());
    }
    catch (Exception e){
      ctx.status(500).result("Error interno " + e.getMessage());
    }

  }

  public void addHeladera(@NotNull Context ctx) {

    try {
      ctx.status(200).json(fachada.agregar(ctx.bodyAsClass(HeladeraDTO.class)));
    }
    catch (Exception e){
      e.printStackTrace();
      ctx.status(400).result("Error de solicitud " + e.getMessage());
    }

  }

  public void getHeladeraId(@NotNull Context ctx) {

    try {
      long id = Long.parseLong(ctx.pathParam("id"));

      HeladeraDTO heladeraDTO = fachada.obtenerHeladeraPorId(id);
      ctx.status(200).json(heladeraDTO);
    }
    catch (NoSuchElementException e){
      ctx.status(404).result("Heladera no encontrada " + e.getMessage());
    }
    catch (Exception e){
      e.printStackTrace();
      ctx.status(500).result("Error interno " + e.getMessage());
    }

  }

  public void cleanAll(Context context) {

    boolean status = fachada.cleanAll();

    if(status){

      context.status(200).result("Base de datos reseteada!");
    }else {

      context.status(500).result("Error Base de datos, no borrada...");
    }

  }
}
