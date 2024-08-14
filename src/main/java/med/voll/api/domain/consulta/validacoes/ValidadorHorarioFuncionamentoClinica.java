package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

import java.time.DayOfWeek;

public class ValidadorHorarioFuncionamentoClinica {

    public void validar(DadosAgendamentoConsulta dados){
        var dataConsulta = dados.data();
        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var antesDaHoraDeAbertura = dataConsulta.getHour() < 7;
        var depoisDoEncerramentoDaClinica = dataConsulta.getHour() > 18;


        if (domingo || antesDaHoraDeAbertura || depoisDoEncerramentoDaClinica){
            throw new ValidacaoException("Consulta Fora Do Horário De Funcionamento Da Clinica!");
        }

    }
}
