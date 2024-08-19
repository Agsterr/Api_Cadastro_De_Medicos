package med.voll.api.domain.consulta;


import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.cancelamento.ValidadorCancelamentoDeConsulta;
import med.voll.api.domain.consulta.validacoes.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultas {


    @Autowired
    private ConsultaRepository repository;
    @Autowired
    MedicoRepository medicoRepository;

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    ConsultaRepository consultaRepository;



    //spring injeta todos validadores na lista
    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validadores;

    @Autowired List<ValidadorCancelamentoDeConsulta> cancelamentoDeConsultas;



    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados){

        if(!pacienteRepository.existsById(dados.idPaciente()) ){
            throw new ValidacaoException("Id do paciente informado não existe!");
        }

        if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())){
            throw new ValidacaoException("Id do Médico informado não existe!");
        }

        //injetar todos validadores desingn pattern>strategy
        validadores.forEach(v -> v.validar(dados));

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        var medico = escolherMedico(dados);
      if (medico == null){

          throw new ValidacaoException("Não Existe Médico Disponivel Nesta Data!");

      }


        var consulta = new Consulta(null,medico, paciente,dados.data(), null );

       repository.save(consulta);

       return new DadosDetalhamentoConsulta(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
     if (dados.idMedico() != null){
         return medicoRepository.getReferenceById(dados.idMedico());
     }

     if (dados.especialidade() == null){
         throw new ValidacaoException("Epecialidade é obrigatória quando médico não for escolhido!");
     }

     return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(),dados.data());

    }

    public void cancelar(DadosCancelamentoConsulta dados) {
        if (!consultaRepository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }

        cancelamentoDeConsultas.forEach(v -> v.validar(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
    }

}
