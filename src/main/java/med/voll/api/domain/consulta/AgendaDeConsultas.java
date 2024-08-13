package med.voll.api.domain.consulta;


import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaDeConsultas {


    @Autowired
    private ConsultaRepository repository;
    @Autowired
    MedicoRepository medicoRepository;

    @Autowired
    PacienteRepository pacienteRepository;



    public void agendar(DadosAgendamentoConsulta dados){

        if(!pacienteRepository.existsById(dados.idPaciente()) ){
            throw new ValidacaoException("Id do paciente informado não existe!");
        }

        if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())){
            throw new ValidacaoException("Id do Médico informado não existe!");
        }

        var medico = escolherMedico(dados);
        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());


        var consulta = new Consulta(null,medico, paciente,dados.data() );




       repository.save(consulta);
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

}
