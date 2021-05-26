package net.etfbl.router.controllers;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class RouterController {

   private final WebClient webClient;
   @Autowired
   public RouterController(@Value("${end_route}") String endRoute) {
      webClient = WebClient.builder().baseUrl(endRoute).build();
   }

   @GetMapping("/")
   @PostMapping("/")
   public ResponseEntity<?> forward(HttpServletRequest request) throws IOException {

      Optional<?> object = webClient.method(HttpMethod.valueOf(request.getMethod()))
            .uri("?" + request.getRequestURI().replaceAll("^.*(\\?|$)", ""))
            .bodyValue(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())))
            .retrieve()
            .bodyToMono(String.class)
            .blockOptional();
      return ResponseEntity.of(object);
   }

   @GetMapping("/hyo")
   public String hyo() {
      return "hiyaaaa!";
   }

}
