package med.voll.api.controller;


import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("medicos")
public class MedicoController {


    @Autowired
    private MedicoRepository  repositorio;

    @PostMapping
    @Transactional
                                                                                  // encapsula o endeço da uri
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder){
        var medico = new Medico(dados);
        repositorio.save(medico);
           // criando a uri automaticamente
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));

    }

    @GetMapping
    public ResponseEntity< Page<DadosListagemMedico>> listar(Pageable pageable){

        var page = repositorio.findAllByAtivoTrue(pageable).map(DadosListagemMedico::new );
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){

        var medico = repositorio.getReferenceById(dados.id());
        medico.atulizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }



    @DeleteMapping("/{id}")
    @Transactional

    //classe do spring para personalizar as respostas do protocolo http
    public ResponseEntity excluir(@PathVariable Long id){
        var medico = repositorio.getReferenceById(id);
        medico.excluir();
        //método da classe
      return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    //classe do spring para personalizar as respostas do protocolo http
    public ResponseEntity detalhar (@PathVariable Long id){
        var medico = repositorio.getReferenceById(id);

        //método da classe
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }


}
