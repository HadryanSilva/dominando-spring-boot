package br.com.hadryan.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = {"v1/customers", "v1/customers/"})
public class CustomerController {

    public static final List<String> NAMES = List.of("Hadryan", "John", "Bob", "Marcus");

    @GetMapping
    public List<String> listNames() {
        return NAMES;
    }

    @GetMapping("filter")
    public List<String> filter(@RequestParam String name) {
        return NAMES.stream().filter(n -> n.equalsIgnoreCase(name)).toList();
    }

    @GetMapping("filterOptional")
    public List<String> filterOptional(@RequestParam Optional<String> name) {
        return NAMES.stream().filter(n -> n.equalsIgnoreCase(name.orElse(""))).toList();
    }

    @GetMapping("filterList")
    public List<String> filterList(@RequestParam(name = "name") List<String> names) {
        return NAMES.stream().filter(names::contains).toList();
    }

    @GetMapping("{name}")
    public String findByName(@PathVariable String name) {
        return NAMES.stream().filter(n -> n.equalsIgnoreCase(name))
                .findFirst()
                .orElse("");
    }

}
