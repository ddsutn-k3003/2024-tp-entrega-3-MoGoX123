package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import ar.edu.utn.dds.k3003.mapper.HeladeraMapper;
import ar.edu.utn.dds.k3003.mapper.TemperaturaMapper;
import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.repository.RepoHeladera;
import lombok.Data;

import java.util.List;
import java.util.NoSuchElementException;

@Data
public class Fachada implements ar.edu.utn.dds.k3003.facades.FachadaHeladeras{

  private FachadaViandas fachadaViandas;
  private RepoHeladera repoHeladera = new RepoHeladera();
  private static HeladeraMapper heladeraMapper = new HeladeraMapper();
  private static TemperaturaMapper temperaturaMapper = new TemperaturaMapper();

  public Fachada() {}

  @Override
  public HeladeraDTO agregar(HeladeraDTO heladeraDTO) {

    // new Heladera(heladeraDTO.getNombre());
    Heladera heladera = heladeraMapper.DTOtoOrigin(heladeraDTO);
    heladera = this.repoHeladera.save(heladera);

    return heladeraMapper.originToDTO(heladera);
  }

  @Override
  public void depositar(Integer heladeraId, String qrVianda) throws NoSuchElementException {

    Heladera heladera = this.repoHeladera
            .findById(Long.valueOf(heladeraId))
            .orElseThrow(() -> new NoSuchElementException("Heladera not found for id: " + heladeraId));

    ViandaDTO viandaDTO = this.fachadaViandas.buscarXQR(qrVianda);

    viandaDTO = this.fachadaViandas.modificarEstado(viandaDTO.getCodigoQR(), EstadoViandaEnum.DEPOSITADA);
    heladera.addVianda();

    this.repoHeladera.update(heladera);
  }

  @Override
  public void retirar(RetiroDTO retiroDTO) throws NoSuchElementException {

    Heladera heladera = this.repoHeladera
            .findById(Long.valueOf(retiroDTO.getHeladeraId()))
            .orElseThrow(() -> new NoSuchElementException("Heladera not found for id: " + retiroDTO.getHeladeraId()));
    ViandaDTO viandaDTO = this.fachadaViandas.buscarXQR(retiroDTO.getQrVianda());

    this.fachadaViandas.modificarEstado(viandaDTO.getCodigoQR(), EstadoViandaEnum.RETIRADA);
    heladera.removeVianda();

    this.repoHeladera.update(heladera);
  }

  @Override
  public Integer cantidadViandas(Integer heladeraId) throws NoSuchElementException {

    Heladera heladera = this.repoHeladera
            .findById(Long.valueOf(heladeraId))
            .orElseThrow(() -> new NoSuchElementException("Heladera not found for id: " + heladeraId));
    return heladera.getCantViandas();
  }

  @Override
  public void temperatura(TemperaturaDTO temperaturaDTO) throws NoSuchElementException{

    try {
      Heladera heladera = this.repoHeladera
              .findById(Long.valueOf(temperaturaDTO.getHeladeraId()))
              .orElseThrow(() -> new NoSuchElementException("Heladera not found for id: " + temperaturaDTO.getHeladeraId()));
      heladera.AgregarTemperatura(temperaturaMapper.DTOtoOrigin(temperaturaDTO));
      this.repoHeladera.update(heladera);

    } catch (Exception e){
      throw new NoSuchElementException();
    }

  }

  @Override
  public List<TemperaturaDTO> obtenerTemperaturas(Integer heladeraId) {

    Heladera heladera = this.repoHeladera
            .findById(Long.valueOf(heladeraId))
            .orElseThrow(() -> new NoSuchElementException("Heladera not found for id: " + heladeraId));

    return temperaturaMapper.listOriginToListDTO(heladera.getRegistrosTemperatura(), heladera.getId());
  }

  @Override
  public void setViandasProxy(FachadaViandas fachadaViandas) {
    this.fachadaViandas = fachadaViandas;
  }

  public HeladeraDTO obtenerHeladeraPorId(Long id) throws NoSuchElementException{

    Heladera heladera = this.repoHeladera.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Heladera not found for id: " + id));

    return heladeraMapper.originToDTO(heladera);
  }

  public List<HeladeraDTO> obtenerHeladeras() {

    return heladeraMapper.originToListDTO(this.repoHeladera.findAll());
  }

  public Boolean cleanAll() {

    repoHeladera.findAll().forEach(heladera -> repoHeladera.delete(heladera));

    return repoHeladera.findAll().isEmpty();
  }
}
