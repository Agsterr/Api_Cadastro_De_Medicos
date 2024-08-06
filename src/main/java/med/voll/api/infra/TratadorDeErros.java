package med.voll.api.infra;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class TratadorDeErros {



    //traramento de erros
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404(){

        return ResponseEntity.notFound().build();

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)

    //recendo a exception como parametro no método
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex){

     var erros = ex.getFieldErrors();
                                              //referencia o construtor
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());

    }
private record DadosErroValidacao(String campo, String mensagem){

        public DadosErroValidacao(FieldError fieldError){
            this(fieldError.getField(), fieldError.getDefaultMessage());
        }



}

}
