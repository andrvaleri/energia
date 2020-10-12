package ee.energia.metal.endpoint;

import ee.energia.metal.entity.BakeSaleException;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestResponseExceptionHandlerTest {
    RestResponseExceptionHandler restResponseExceptionHandler = new RestResponseExceptionHandler();

    @Test
    void testHandleItemsErrors() {
        String message = "message";
        Problem result = restResponseExceptionHandler.handleItemsErrors(new BakeSaleException(message));
        assertEquals(message, result.getDetail());
        assertEquals("Error", result.getTitle());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
    }
}
