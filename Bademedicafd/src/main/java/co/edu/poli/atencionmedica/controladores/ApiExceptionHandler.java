package co.edu.poli.atencionmedica.controladores;

import co.edu.poli.atencionmedica.servicios.DaoException;
import co.edu.poli.atencionmedica.servicios.ValidacionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<ApiError> manejarValidacion(ValidacionException exception) {
        return ResponseEntity.badRequest().body(new ApiError(exception.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> manejarJsonInvalido(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest().body(new ApiError("El cuerpo JSON no es válido."));
    }

    @ExceptionHandler(DaoException.class)
    public ResponseEntity<ApiError> manejarErrorDeDatos(DaoException exception) {
        LOGGER.error("Falló una operación del API de pacientes.", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiError("No fue posible completar la operación. Verifica la conexión con la base de datos."));
    }

    public record ApiError(String error) { }
}
