package med.voll.api.controller;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@AutoConfigureJsonTesters
class MedicoControllerTest {

    DadosEndereco dadosEndereco;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJacksonTester;

    @Autowired
    private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJacksonTester;

    @MockBean
    private MedicoRepository repositorio;

    @Test
    @DisplayName("Deveria devolver código 201 e o local correto quando as informações estão válidas")
    void cadastrar_cenarioValido() throws Exception {
        // Configuração dos dados de entrada
        var endereco = new DadosEndereco("Rua A", "Bairro B", "12345678", "Cidade C", "Estado D", "Complemento E", "123");
        var dadosCadastro = new DadosCadastroMedico("Dr. John Doe", "john.doe@example.com", "123456789", "1234", Especialidade.CARDIOLOGIA, endereco);

        // Criando o objeto Medico com ID simulado
        var medico = new Medico(dadosCadastro);
        medico.setId(1L); // Simulação do ID gerado pelo banco de dados

        // Simulação do comportamento do repositório
        when(repositorio.save(any(Medico.class))).thenAnswer(invocation -> {
            Medico savedMedico = invocation.getArgument(0);
            savedMedico.setId(1L); // Simular a geração de ID pelo banco de dados
            return savedMedico;
        });

        // Execução da requisição POST
        var response = mvc.perform(post("/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroMedicoJacksonTester.write(dadosCadastro).getJson())
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verificação do status da resposta
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        // Verificação do cabeçalho Location com a URI completa
        assertThat(response.getHeader(HttpHeaders.LOCATION)).isEqualTo("http://localhost/medicos/1");

        // Verificação do conteúdo da resposta
        var jsonEsperado = dadosDetalhamentoMedicoJacksonTester.write(new DadosDetalhamentoMedico(medico)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    @WithMockUser
    void cadastrar_cenario1() throws Exception {
        var response = mvc
                .perform(post("/medicos"))
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informacoes estao validas")
    @WithMockUser
    void cadastrar_cenario2() throws Exception {
        var dadosCadastro = new DadosCadastroMedico(
                "Medico",
                "medico@voll.med",
                "61999999999",
                "123456",
                Especialidade.CARDIOLOGIA,
                dadosEndereco()
                );

        when(repositorio.save(any())).thenReturn(new Medico(dadosCadastro));

        var response = mvc
                .perform(post("/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroMedicoJacksonTester.write(dadosCadastro).getJson()))
                .andReturn().getResponse();

        var dadosDetalhamento = new DadosDetalhamentoMedico(
                null,
                dadosCadastro.nome(),
                dadosCadastro.email(),
                dadosCadastro.crm(),
                dadosCadastro.telefone(),
                dadosCadastro.especialidade(),
                new Endereco(dadosCadastro.endereco())
        );
        var jsonEsperado = dadosDetalhamentoMedicoJacksonTester.write(dadosDetalhamento).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);


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