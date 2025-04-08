package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.services.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloControleur {

    private final HelloService helloService;

    // Injection par constructeur
    @Autowired
    public HelloControleur(HelloService helloService){
        this.helloService=helloService;
    }

    @GetMapping
    public String direHello (){
        return helloService.salutations();
    }
}
