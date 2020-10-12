package ee.energia.metal.endpoint;

import ee.energia.metal.entity.BakeSaleException;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BakeSaleException.class)
    @ResponseBody
    public org.springframework.hateoas.mediatype.problem.Problem handleItemsErrors(BakeSaleException ex) {
        return Problem.create()
                .withTitle("Error")
                .withStatus(HttpStatus.BAD_REQUEST)
                .withDetail(ex.getMessage());
    }

}