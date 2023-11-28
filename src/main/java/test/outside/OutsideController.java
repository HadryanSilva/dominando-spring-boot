package test.outside;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OutsideController {

    @GetMapping("outside")
    public String test() {
        return "Testing endpoint outside br.com.hadryan";
    }

}
