package med.voll.api.medico;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.endereco.DadosEndereco;

public record DadosCadastroMedico(

        @NotNull
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @Pattern(regexp ="\\d{4,6}")
        String crm,

        @NotNull

        Especialidade especialidade,

        @NotNull @Valid

        DadosEndereco endereco ) {

}


