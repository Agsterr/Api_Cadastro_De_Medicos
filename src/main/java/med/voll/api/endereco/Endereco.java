package med.voll.api.endereco;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    private String logradouro;
    private String bairro;
    private String cep;
    private String numero;
    private String complemento;
    private String cidade;
    private String uf;

    public Endereco(DadosEndereco endereco) {

        this.bairro = endereco.bairro();
        this.cep = endereco.cep();
        this.numero = endereco.numero();
        this.complemento = endereco.complemento();
        this.cidade = endereco.cidade();
        this.uf = endereco.uf();
        this.logradouro = endereco.logradouro();
    }

    public void atulizarEndereco(DadosEndereco dadosEndereco) {
        if (dadosEndereco.logradouro() != null) {
            this.logradouro = dadosEndereco.logradouro();
        }
        if (dadosEndereco.bairro() != null) {
            this.bairro = dadosEndereco.bairro();
        }
        if (dadosEndereco.cep() != null) {
            this.cep = dadosEndereco.cep();
        }
        if (dadosEndereco.uf() != null) {
            this.uf = dadosEndereco.uf();
        }
        if (dadosEndereco.cidade() != null) {
            this.cidade = dadosEndereco.cidade();
        }
        if (dadosEndereco.numero() != null) {
            this.numero = dadosEndereco.numero();
        }
        if (dadosEndereco.complemento() != null) {
            this.complemento = dadosEndereco.complemento();

        }
    }
}
