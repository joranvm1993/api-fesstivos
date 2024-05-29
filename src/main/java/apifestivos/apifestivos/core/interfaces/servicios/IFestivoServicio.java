package apifestivos.apifestivos.core.interfaces.servicios;

import java.util.Date;
import java.util.List;

import apifestivos.apifestivos.core.DTOs.FestivoDTO;

public interface IFestivoServicio {

    public List<FestivoDTO> obtenerFestivos(int año);

    public boolean esFestivo(Date fecha);

}
