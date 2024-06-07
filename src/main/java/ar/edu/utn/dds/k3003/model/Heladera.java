package ar.edu.utn.dds.k3003.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Data
@Entity
@Table(name = "heladera")
public class Heladera extends PersistenceClass{

  @Column
  private String nombre;
  @Column(name = "cant_viandas")
  private int cantViandas;
  @Column
  private boolean activa;
  @Column
  private LocalDateTime ultimaVezAbierto;

  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  private List<RegistroTemperatura> registroTemperaturas;

  public Heladera(){
    super();
  }

  public Heladera(String nombre) {
    this.nombre = nombre;
    this.cantViandas = 0;
    this.activa = true;
    this.ultimaVezAbierto = LocalDateTime.now();
    this.registroTemperaturas = new ArrayList<>();
  }

  public void addVianda() {
    this.cantViandas++;
    this.ultimaVezAbierto = LocalDateTime.now();
  }

  public void removeVianda() throws NoSuchElementException {
    if (this.cantViandas > 0) {
      this.cantViandas--;
      this.ultimaVezAbierto = LocalDateTime.now();
    } else {
      throw new NoSuchElementException("No hay viandas para remover");
    }
  }

  public void AgregarTemperatura(RegistroTemperatura registroTemperatura) {
    this.registroTemperaturas.add(new RegistroTemperatura(registroTemperatura.getFechaMedicion(), registroTemperatura.getTemperatura()));
    this.registroTemperaturas.sort((registroTemperatura1, registroTemperatura2)
        -> ChronoLocalDateTime.timeLineOrder().compare(registroTemperatura2.getFechaMedicion(), registroTemperatura1.getFechaMedicion()));
  }
}
