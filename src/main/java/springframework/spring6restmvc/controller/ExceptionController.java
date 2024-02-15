package springframework.spring6restmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice//it's global for all controllers, not like previous handler only for Beer
public class ExceptionController {
    //moved from BeerController to be global handler for all controllers now
    @ExceptionHandler(NotFoundException.class)//again annotation to have this handled by the framework
    public ResponseEntity handleNotFoundException() {
        return ResponseEntity.notFound().build();
        //if any NFE occurs it will be handled by this handler method
        //I return only simple response, but it could be something more complex
    }
}
