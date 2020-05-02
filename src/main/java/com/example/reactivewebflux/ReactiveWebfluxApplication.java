package com.example.reactivewebflux;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
interface UserCrudRepository extends ReactiveCrudRepository<User, String> {

}

@SpringBootApplication
public class ReactiveWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveWebfluxApplication.class, args);
    }

}

@RestController
class FluxController {

    @Autowired
    UserCrudRepository repository;

    @GetMapping(value = "/users/{id}")
    public Mono<User> getUserById(@PathVariable String id) {
        return repository.findById(id);
    }

    @GetMapping(value = "/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> getAll() {
        Flux<User> flux = repository.findAll();
        return flux.delayElements(Duration.ofSeconds(1));
    }

    @PostMapping("users")
    public Mono<User> saveUser(@RequestBody User user) {
        return repository.save(user);
    }

    @DeleteMapping("/users/{id}")
    public Mono<Void> deleteUser(@PathVariable String id) {
        return repository.deleteById(id);
    }

    @DeleteMapping("/users")
    public Mono<Void> deleteAllUser() {
        return repository.deleteAll();
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class User {

    @Id
    private String id;
    private String fName;
    private String lName;
    private int age;

}