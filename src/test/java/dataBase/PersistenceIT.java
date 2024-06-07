package dataBase;

import ar.edu.utn.dds.k3003.db.EntityManagerHelper;
import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.RegistroTemperatura;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersistenceIT {

//    EntityManager entityManager ;
//
//    @BeforeEach
//    public void setup() throws Exception {
//        entityManager = EntityManagerHelper.getEntityManager();
//    }
    @Test
    public void testConectar() {
// vacío, para ver que levante el ORM
    }
    @Test
    public void testGuardarYRecuperarHeladera() throws Exception {

        Heladera heladera1 = new Heladera( "HeladeraTest");

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(heladera1);
        EntityManagerHelper.commitTransaction();

        Heladera heladera2 = EntityManagerHelper.getEntityManager().find(Heladera.class,1L);

        assertEquals(heladera1.getNombre(), heladera2.getNombre());
        assertEquals(0, heladera2.getCantViandas());

        heladera2.addVianda();
        heladera2.addVianda();
        heladera2.addVianda();
        heladera2.AgregarTemperatura(new RegistroTemperatura(LocalDateTime.now(), 0L));
        heladera2.AgregarTemperatura(new RegistroTemperatura(LocalDateTime.now(), 3L));

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(heladera2);
        EntityManagerHelper.commitTransaction();

        Heladera heladera3 = EntityManagerHelper.getEntityManager().find(Heladera.class,1L);

        assertEquals(heladera1.getNombre(), heladera3.getNombre());
        assertEquals(3, heladera3.getCantViandas());
        assertEquals(2, heladera3.getRegistroTemperaturas().size());
        EntityManagerHelper.closeEntityManager();
    }

}

