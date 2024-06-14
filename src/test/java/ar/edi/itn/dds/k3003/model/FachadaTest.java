package ar.edi.itn.dds.k3003.model;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.repository.RepoHeladera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class FachadaTest {

  String codigoQrVianda1;
  Fachada fachadaHeladera;
  @Mock
  FachadaViandas fachadaViandas;
  RepoHeladera repoHeladera;
  HeladeraDTO heladeraDTOTest1;
  HeladeraDTO heladeraDTOTest2;
  HeladeraDTO heladeraDTOTest3;
  ViandaDTO viandaMockPreparada;
  ViandaDTO viandaMockDepositada;
  ViandaDTO viandaMockRetirada;
  RetiroDTO retiroDTOHeladera1Existente;
  RetiroDTO retiroDTOHeladera2Existente;

  @BeforeEach
  void setUp() {
    repoHeladera = new RepoHeladera();

    fachadaHeladera = new Fachada();
    fachadaHeladera.setViandasProxy(fachadaViandas);
    fachadaHeladera.cleanAll();

    heladeraDTOTest1 = new HeladeraDTO(null, "Heladera Medrano UTN", 0);
    heladeraDTOTest2 = new HeladeraDTO(null, "Heladera Campus UTN", 0);
    heladeraDTOTest3 = new HeladeraDTO(null, "Heladera Avellaneda UTN", 0);

    codigoQrVianda1 = "vianda1";
    LocalDateTime tiempo = LocalDateTime.now();

    viandaMockPreparada = new ViandaDTO(codigoQrVianda1, tiempo, EstadoViandaEnum.PREPARADA, 1L, 1);
    viandaMockDepositada = new ViandaDTO(codigoQrVianda1, tiempo, EstadoViandaEnum.DEPOSITADA, 1L, 1);
    viandaMockRetirada = new ViandaDTO(codigoQrVianda1, tiempo, EstadoViandaEnum.RETIRADA, 1L, 1);
  }

  @Test
  void agregarTest() {

    fachadaHeladera.agregar(heladeraDTOTest1);
    assertEquals(1, repoHeladera.findAll().size());

    fachadaHeladera.agregar(heladeraDTOTest2);
    assertEquals(2, repoHeladera.findAll().size());
  }

  @Test
  void depositarTest() {

    Mockito.when(fachadaViandas.buscarXQR("vianda1")).thenReturn(viandaMockPreparada);
    Mockito.when(fachadaViandas.modificarEstado(viandaMockPreparada.getCodigoQR(), EstadoViandaEnum.DEPOSITADA))
        .thenReturn(viandaMockDepositada);

    HeladeraDTO heladeraAgregada1 = fachadaHeladera.agregar(heladeraDTOTest1);
    HeladeraDTO heladeraAgregada2 = fachadaHeladera.agregar(heladeraDTOTest2);

    fachadaHeladera.depositar(heladeraAgregada1.getId(), "vianda1");

    Heladera heladera1 = repoHeladera
            .findById(Long.valueOf(heladeraAgregada1.getId()))
            .orElseThrow(() -> new NoSuchElementException("Heladera not found for id: " + heladeraAgregada1.getId()));
    Heladera heladera2 = repoHeladera
            .findById(Long.valueOf(heladeraAgregada2.getId()))
            .orElseThrow(() -> new NoSuchElementException("Heladera not found for id: " + heladeraAgregada2.getId()));

    assertEquals(1, fachadaHeladera.cantidadViandas(Math.toIntExact(heladera1.getId())));
    assertEquals(0, fachadaHeladera.cantidadViandas(Math.toIntExact(heladera2.getId())));

    fachadaHeladera.depositar(heladeraAgregada2.getId(), "vianda1");
    fachadaHeladera.depositar(heladeraAgregada1.getId(), "vianda1");

    heladera1 = repoHeladera
            .findById(Long.valueOf(heladeraAgregada1.getId()))
            .orElseThrow(() -> new NoSuchElementException("Heladera not found for id: " + heladeraAgregada1.getId()));
    heladera2 = repoHeladera
            .findById(Long.valueOf(heladeraAgregada2.getId()))
            .orElseThrow(() -> new NoSuchElementException("Heladera not found for id: " + heladeraAgregada2.getId()));

    assertEquals(2, fachadaHeladera.cantidadViandas(Math.toIntExact(heladera1.getId())));
    assertEquals(1, fachadaHeladera.cantidadViandas(Math.toIntExact(heladera2.getId())));
  }

  @Test
  void retirarViandaExistenteTest() {

    Mockito.when(fachadaViandas.buscarXQR("vianda1")).thenReturn(viandaMockPreparada);
    Mockito.when(fachadaViandas.modificarEstado(viandaMockPreparada.getCodigoQR(), EstadoViandaEnum.DEPOSITADA))
        .thenReturn(viandaMockDepositada);

    HeladeraDTO heladeraAgregada1 = fachadaHeladera.agregar(heladeraDTOTest1);
    HeladeraDTO heladeraAgregada2 = fachadaHeladera.agregar(heladeraDTOTest2);

    fachadaHeladera.depositar(heladeraAgregada1.getId(), "vianda1");
    fachadaHeladera.depositar(heladeraAgregada2.getId(), "vianda1");
    fachadaHeladera.depositar(heladeraAgregada1.getId(), "vianda1");

    Mockito.when(fachadaViandas.buscarXQR("vianda1")).thenReturn(viandaMockDepositada);
    Mockito.when(fachadaViandas.modificarEstado(viandaMockDepositada.getCodigoQR(), EstadoViandaEnum.RETIRADA))
        .thenReturn(viandaMockRetirada);

    retiroDTOHeladera1Existente = new RetiroDTO(codigoQrVianda1, "tarjeta", heladeraAgregada1.getId());
    retiroDTOHeladera2Existente = new RetiroDTO(codigoQrVianda1, "tarjeta", heladeraAgregada2.getId());

    fachadaHeladera.retirar(retiroDTOHeladera1Existente);
    fachadaHeladera.retirar(retiroDTOHeladera2Existente);

    Heladera heladera1 = repoHeladera
            .findById(Long.valueOf(heladeraAgregada1.getId()))
            .orElseThrow(() -> new NoSuchElementException("Heladera not found for id: " + heladeraAgregada2.getId()));
    Heladera heladera2 = repoHeladera
            .findById(Long.valueOf(heladeraAgregada2.getId()))
            .orElseThrow(() -> new NoSuchElementException("Heladera not found for id: " + heladeraAgregada2.getId()));

    assertEquals(1, fachadaHeladera.cantidadViandas(Math.toIntExact(heladera1.getId())));
    assertEquals(0, fachadaHeladera.cantidadViandas(Math.toIntExact(heladera2.getId())));
  }

  @Test
  void retirarViandaNoExistenteTest() {

    Mockito.when(fachadaViandas.buscarXQR("vianda1")).thenReturn(viandaMockPreparada);
    Mockito.when(fachadaViandas.modificarEstado(viandaMockPreparada.getCodigoQR(), EstadoViandaEnum.DEPOSITADA))
        .thenReturn(viandaMockDepositada);

    HeladeraDTO heladeraAgregada1 = fachadaHeladera.agregar(heladeraDTOTest1);
    HeladeraDTO heladeraAgregada2 = fachadaHeladera.agregar(heladeraDTOTest2);

    fachadaHeladera.depositar(heladeraAgregada1.getId(), "vianda1");
    fachadaHeladera.depositar(heladeraAgregada2.getId(), "vianda1");
    fachadaHeladera.depositar(heladeraAgregada1.getId(), "vianda1");

    Mockito.when(fachadaViandas.buscarXQR("ViandaFalla"))
        .thenThrow(new NoSuchElementException());

    // Espero que lance una excepcion
    assertThrows(NoSuchElementException.class, () -> fachadaHeladera.retirar(new RetiroDTO("ViandaFalla", "tarjeta", heladeraAgregada1.getId())));

    // Se ejecuta aunque haya excepcion
    assertDoesNotThrow(() -> {
      Heladera heladera1 = repoHeladera
              .findById(Long.valueOf(heladeraAgregada1.getId()))
              .orElseThrow(() -> new NoSuchElementException("Heladera not found for id: " + heladeraAgregada2.getId()));
      assertEquals(2, fachadaHeladera.cantidadViandas(Math.toIntExact(heladera1.getId())));
    });
  }

  @Test
  void temperaturaTest(){

    LocalDateTime tiempo = LocalDateTime.of(2000, 1, 1, 10, 0);

    HeladeraDTO heladeraAgregada1 = fachadaHeladera.agregar(heladeraDTOTest1);
    HeladeraDTO heladeraAgregada2 = fachadaHeladera.agregar(heladeraDTOTest2);
    HeladeraDTO heladeraAgregada3 = fachadaHeladera.agregar(heladeraDTOTest3);

    TemperaturaDTO temperaturaDTO1 = new TemperaturaDTO(10, heladeraAgregada1.getId(), tiempo.plusMinutes(5));
    TemperaturaDTO temperaturaDTO2 = new TemperaturaDTO(1, heladeraAgregada1.getId(), tiempo.plusMinutes(10));
    TemperaturaDTO temperaturaDTO3 = new TemperaturaDTO(-18, heladeraAgregada1.getId(), tiempo.plusMinutes(15));

    TemperaturaDTO temperaturaDTO4 = new TemperaturaDTO(2, heladeraAgregada2.getId(), tiempo.plusMinutes(5));
    TemperaturaDTO temperaturaDTO5 = new TemperaturaDTO(5, heladeraAgregada2.getId(), tiempo.plusMinutes(10));

    fachadaHeladera.temperatura(temperaturaDTO1);
    fachadaHeladera.temperatura(temperaturaDTO2);
    fachadaHeladera.temperatura(temperaturaDTO3);

    fachadaHeladera.temperatura(temperaturaDTO4);
    fachadaHeladera.temperatura(temperaturaDTO5);

    List<TemperaturaDTO> temperaturaDTOSHeladera1 = fachadaHeladera.obtenerTemperaturas(heladeraAgregada1.getId());
    List<TemperaturaDTO> temperaturaDTOSHeladera2 = fachadaHeladera.obtenerTemperaturas(heladeraAgregada2.getId());
    List<TemperaturaDTO> temperaturaDTOSHeladera3 = fachadaHeladera.obtenerTemperaturas(heladeraAgregada3.getId());

    assertEquals(3, temperaturaDTOSHeladera1.size());
    assertEquals(2, temperaturaDTOSHeladera2.size());
    assertEquals(0, temperaturaDTOSHeladera3.size());

    assertTrue(temperaturaDTOSHeladera1.contains(temperaturaDTO1));
    assertTrue(temperaturaDTOSHeladera1.contains(temperaturaDTO2));
    assertTrue(temperaturaDTOSHeladera1.contains(temperaturaDTO3));

    assertTrue(temperaturaDTOSHeladera2.contains(temperaturaDTO4));
    assertTrue(temperaturaDTOSHeladera2.contains(temperaturaDTO5));
  }

  @Test
  void temperaturaTestHeladeraNoExiste(){

    LocalDateTime tiempo = LocalDateTime.of(2000, 1, 1, 10, 0);

    HeladeraDTO heladeraAgregada1 = fachadaHeladera.agregar(heladeraDTOTest1);
    HeladeraDTO heladeraAgregada2 = fachadaHeladera.agregar(heladeraDTOTest2);
    HeladeraDTO heladeraAgregada3 = fachadaHeladera.agregar(heladeraDTOTest3);

    TemperaturaDTO temperatura = new TemperaturaDTO(10, heladeraAgregada3.getId() + 1, tiempo.plusMinutes(5));

    // Espero que lance una excepcion
    assertThrows(NoSuchElementException.class, () -> fachadaHeladera.temperatura(temperatura));

    // Se ejecuta aunque haya excepcion
    assertDoesNotThrow(() -> {
      assertEquals(0, fachadaHeladera.obtenerTemperaturas(heladeraAgregada1.getId()).size());
      assertEquals(0, fachadaHeladera.obtenerTemperaturas(heladeraAgregada2.getId()).size());
      assertEquals(0, fachadaHeladera.obtenerTemperaturas(heladeraAgregada3.getId()).size());
    });

  }

  @Test
  void resetearDB(){

    LocalDateTime tiempo = LocalDateTime.of(2000, 1, 1, 10, 0);

    HeladeraDTO heladeraAgregada1 = fachadaHeladera.agregar(heladeraDTOTest1);
    HeladeraDTO heladeraAgregada2 = fachadaHeladera.agregar(heladeraDTOTest2);

    TemperaturaDTO temperaturaDTO1 = new TemperaturaDTO(10, heladeraAgregada1.getId(), tiempo.plusMinutes(5));
    TemperaturaDTO temperaturaDTO2 = new TemperaturaDTO(1, heladeraAgregada1.getId(), tiempo.plusMinutes(10));

    TemperaturaDTO temperaturaDTO3 = new TemperaturaDTO(-18, heladeraAgregada2.getId(), tiempo.plusMinutes(15));

    fachadaHeladera.temperatura(temperaturaDTO1);
    fachadaHeladera.temperatura(temperaturaDTO2);

    fachadaHeladera.temperatura(temperaturaDTO3);

    assertEquals(2, fachadaHeladera.obtenerHeladeras().size());
    assertEquals(2, fachadaHeladera.obtenerTemperaturas(heladeraAgregada1.getId()).size());
    assertEquals(1, fachadaHeladera.obtenerTemperaturas(heladeraAgregada2.getId()).size());

    fachadaHeladera.cleanAll();
    assertEquals(0, fachadaHeladera.obtenerHeladeras().size());
  }


}
