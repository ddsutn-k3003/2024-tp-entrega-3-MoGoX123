package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.base.PersistenceClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity @Table
@Data @AllArgsConstructor
public class RegistroTemperatura extends PersistenceClass {

  //Las temperaturas pueden estar en cualquier medida
  @Column
  private LocalDateTime fechaMedicion;
  @Column
  private long temperatura;

  public RegistroTemperatura() {
    super();
  }
}
