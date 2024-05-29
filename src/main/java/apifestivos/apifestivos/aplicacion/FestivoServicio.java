package apifestivos.apifestivos.aplicacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import apifestivos.apifestivos.core.DTOs.FestivoDTO;
import apifestivos.apifestivos.core.dominio.Festivo;
import apifestivos.apifestivos.core.interfaces.repositorios.IFestivoRepositorio;
import apifestivos.apifestivos.core.interfaces.servicios.IFestivoServicio;

@Service
public class FestivoServicio implements IFestivoServicio {

    private IFestivoRepositorio repositorio;

    public FestivoServicio(IFestivoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    private static final int[] M_VALUES = { 22, 23, 23, 24, 24, 25 };
    private static final int[] N_VALUES = { 2, 3, 4, 5, 6, 0 };

    private Date obtenerDomingoPascua(int año) {
        int sigloIndex = Math.min(Math.max((año - 1583) / 100, 0), M_VALUES.length - 1);
        int M = M_VALUES[sigloIndex];
        int N = N_VALUES[sigloIndex];

        int A = año % 19;
        int B = año % 4;
        int C = año % 7;
        int D = (19 * A + M) % 30;
        int E = (2 * B + 4 * C + 6 * D + N) % 7;

        int dia = (D + E < 10) ? D + E + 22 : D + E - 9;
        int mes = (D + E < 10) ? 3 : 4;

        if ((dia == 26 && mes == 4) || (dia == 25 && mes == 4 && D == 28 && E == 6 && A > 10)) {
            dia -= 7;
        }

        return new Date(año - 1900, mes - 1, dia);
    }

    private Date agregarDias(Date fecha, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.DATE, dias);
        return cal.getTime();
    }

    private Date siguienteLunes(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int daysToAdd = (dayOfWeek == Calendar.SUNDAY) ? 1 : (Calendar.SATURDAY - dayOfWeek + 2) % 7;
        return agregarDias(fecha, daysToAdd);
    }

    private List<FestivoDTO> calcularFestivos(int año) {
        List<Festivo> festivos = repositorio.findAll();

        if (festivos == null)
            return null;

        List<FestivoDTO> fechasFestivas = new ArrayList<>();

        Date pascua = obtenerDomingoPascua(año);

        for (Festivo festivo : festivos) {
            Date fecha;
            switch (festivo.getTipo().getId()) {
                case 1:
                    fecha = new Date(año - 1900, festivo.getMes() - 1, festivo.getDia());
                    break;
                case 2:
                    fecha = siguienteLunes(new Date(año - 1900, festivo.getMes() - 1, festivo.getDia()));
                    break;
                case 3:
                    fecha = agregarDias(pascua, festivo.getDiasPascua());
                    break;
                case 4:
                    fecha = siguienteLunes(agregarDias(pascua, festivo.getDiasPascua()));
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de festivo no soportado");
            }
            fechasFestivas.add(new FestivoDTO(festivo.getNombre(), fecha));
        }
        return fechasFestivas;
    }

    @Override
    public List<FestivoDTO> obtenerFestivos(int año) {
        return calcularFestivos(año);
    }

    @Override
    public boolean esFestivo(Date fecha) {
        List<FestivoDTO> festivos = calcularFestivos(fecha.getYear() + 1900);

        for (FestivoDTO festivo : festivos) {
            if (festivo.getFecha().equals(fecha)) {
                return true;
            }
        }
        return false;
    }

}
