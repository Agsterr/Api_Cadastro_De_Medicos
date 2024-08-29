package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.paciente.DadosPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//indicar para o spring boot que eu quero que ele use o banco de dados da propria aplicação
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE )
//application-test.properties indicando para o spring usar esse para test
//tem que criar a tabela no banco de dados
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    MedicoRepository  repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Deveria delvolver null quando unico medico cadastrado nao esta disponivel na data ")
    void escolherMedicoAleatorioLivreNaDataCenario1() {

        //given ou arrange

        var proximaSegundaAS10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);


        var medico = cadastrarMedico("medico", "medico@voll.med", "123456", Especialidade.CARDIOLOGIA);
        var paciente = cadastrarPaciente("paciente", "paciente", "09999999999");
         cadastrarConsulta(medico, paciente, proximaSegundaAS10 );

        //when ou act
        var medicoLivre = repository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAS10);

        //then ou assert
        assertThat(medicoLivre).isNull();

    }


    @Test
    @DisplayName("Deveria delvolver medico cadastrado se ele estiver  disponivel na data ")
    void escolherMedicoAleatorioLivreNaDataCenario2() {

        //given ou arrange
        var proximaSegundaAS10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);

        var medico = cadastrarMedico("medico", "medico@voll.med", "123456", Especialidade.CARDIOLOGIA);

        //when ou act
        var medicoLivre = repository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAS10);

        //then ou assert
        assertThat(medicoLivre).isEqualTo(medico);

    }


    private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data) {
            em.persist(new Consulta(null, medico, paciente, data));
        }

        private Medico cadastrarMedico(String nome, String email, String crm, Especialidade especialidade) {
            var medico = new Medico(dadosMedico(nome, email, crm, especialidade));
            em.persist(medico);
            return medico;
        }

        private Paciente cadastrarPaciente(String nome, String email, String cpf) {
            var paciente = new Paciente(dadosPaciente(nome, email, cpf));
            em.persist(paciente);
            return paciente;
        }

        private DadosCadastroMedico dadosMedico(String nome, String email, String crm, Especialidade especialidade) {
            return new DadosCadastroMedico(
                    nome,
                    email,
                    "61999999999",
                    crm,
                    especialidade,
                    dadosEndereco()
            );
        }

        private DadosPaciente dadosPaciente(String nome, String email, String cpf) {
            return new DadosPaciente(
                    nome,
                    email,
                    "61999999999",
                    cpf,
                    dadosEndereco()
            );
        }

        private DadosEndereco dadosEndereco() {
            return new DadosEndereco(
                    "rua xpto",
                    "bairro",
                    "00000000",
                    "Brasilia",
                    "DF",
                    null,
                    null
            );
        }











}