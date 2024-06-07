package ar.edu.utn.dds.k3003.mapper;

import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.model.RegistroTemperatura;

import java.util.List;
import java.util.stream.Collectors;

public class TemperaturaMapper {

  public TemperaturaDTO originToDTO(RegistroTemperatura registro, Long idHeladera) {
    return new TemperaturaDTO(Math.toIntExact(registro.getTemperatura()),
        Math.toIntExact(idHeladera), registro.getFechaMedicion());
  }

  public List<TemperaturaDTO> listOriginToListDTO(List<RegistroTemperatura> registroTemperaturas, Long heladeraId) {

    return registroTemperaturas.stream()
        .map(registroTemperatura -> originToDTO(registroTemperatura, heladeraId))
        .collect(Collectors.toList());
  }

  public RegistroTemperatura DTOtoOrigin(TemperaturaDTO temperaturaDTO) {

    return new RegistroTemperatura(temperaturaDTO.getFechaMedicion(), temperaturaDTO.getTemperatura());
  }
}
