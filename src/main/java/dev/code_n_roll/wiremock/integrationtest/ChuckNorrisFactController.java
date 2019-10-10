package dev.code_n_roll.wiremock.integrationtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChuckNorrisFactController {

  private final ChuckNorrisService chuckNorrisService;

  @Autowired
  public ChuckNorrisFactController(ChuckNorrisService chuckNorrisService) {
    this.chuckNorrisService = chuckNorrisService;
  }

  @GetMapping(path = "/fact", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ChuckNorrisFact getFact(){
    return chuckNorrisService.retrieveFact();
  }
}
