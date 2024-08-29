package med.voll.api.controller;

import med.voll.api.domain.consulta.AgendaDeConsultas;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
//teste unitario usando mock tem colocar essa anotação se não, não da pra injetar a classe mockmvc
@AutoConfigureMockMvc
//ignora o login
@WithMockUser
//para injetar json tester
@AutoConfigureJsonTesters
class ConsultaControllerTest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta>dadosAgendamentoConsultaJacksonTester;

    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta>dadosDetalhamentoConsultaJacksonTester;


    //não fazer as consultas no banco de dados
    @MockBean
    private AgendaDeConsultas consultas;

    @Test
    @DisplayName("Deveria devolver codigo 400 quando informaçães estão invalidas")
    void agendar_cenario1() throws Exception {
        var response = mvc.perform(post("/consultas")).andReturn().getResponse();
assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @DisplayName("Deveria devolver codigo 200 quando informaçães estão validas")
    void agendar_cenario2() throws Exception {
        var data = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.CARDIOLOGIA;
        //métodos do mokito
         var dadosDetalhamento = new DadosDetalhamentoConsulta(null,2l,5l,data );
        when(consultas.agendar(any())).thenReturn(dadosDetalhamento);

        var response = mvc.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosAgendamentoConsultaJacksonTester
                        .write(new DadosAgendamentoConsulta(2l,5l, data, especialidade)).getJson())
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = dadosDetalhamentoConsultaJacksonTester
                .write(dadosDetalhamento ).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }
}