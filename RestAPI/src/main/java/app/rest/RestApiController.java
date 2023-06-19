package app.rest;

import app.model.Word;
import app.persistance.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("controller")
public class RestApiController {

    private final WordRepository wordRepository;

    @Autowired
    public RestApiController(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Word> getAll(){
        System.out.println("Get all words ...");
        return wordRepository.getAll();
    }

}
