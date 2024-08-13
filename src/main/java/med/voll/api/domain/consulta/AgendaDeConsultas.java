package med.voll.api.domain.consulta;


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



        var medico = medicoRepository.findById(dados.idMedico()).get();
        var paciente = pacienteRepository.findById(dados.idPaciente()).get();


        var consulta = new Consulta(null,medico, paciente,dados.data() );




       repository.save(consulta);
    }

}
