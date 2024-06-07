package ar.edu.utn.dds.k3003.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class PersistenceClass {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
