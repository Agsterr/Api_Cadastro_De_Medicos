package med.voll.api.medico;

import jakarta.validation.constraints.NotNull;
import med.voll.api.endereco.DadosEndereco;

public record DadosAtualizacaoMedico(Long id,

                                     @NotNull
                                     String nome,
                                     String telefone,
                                     DadosEndereco dadosEndereco) {
}
