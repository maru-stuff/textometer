package marustuff.textometer.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import marustuff.textometer.model.Metering;
import marustuff.textometer.repository.MeteringRepository;
import marustuff.textometer.model.Vs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {
    @NonNull
    @Autowired
    private final MeteringRepository repository;
    //@GetMapping("/{word}")
    @GetMapping("/test")
    //public String ServeMetering(@PathVariable String word){
    public String TestMetering(Model model){
        //Metering metering = new Metering();
        model.addAttribute("currentWords",repository.findAll());
        return "test";
    }

    @GetMapping("/{word}")
    public String ServeMetering(@PathVariable String word, Model model) {
        //Metering metering = new Metering();
        if(repository.existsById(word)&&isItFresh(repository.findById(word).get())){
            //metering = repository.findById(word).get();
            model.addAttribute("currentWord",repository.findById(word).get());

            return "request";
        }
        else{

            return "redirect:/poll/"+word;
        }

    }

    @GetMapping("/vs/{word}/{word2}")
    public String ServeVsMetering(@PathVariable("word") String word, @PathVariable("word2") String word2, Model model){
        if((repository.existsById(word)&&isItFresh(repository.findById(word).get()))&&(repository.existsById(word2)&&isItFresh(repository.findById(word2).get()))){
            Vs vs = new Vs(repository.findById(word).get(),repository.findById(word2).get());
            model.addAttribute("currentVs",vs);
            return "vs";
        }
         return "redirect:/poll/"+word+"/"+word2;
    }

    private boolean isItFresh(Metering metering){
        Instant fresh= Instant.now().minus(1, ChronoUnit.MINUTES);
        if(metering.getCreatedAt().toEpochMilli()>fresh.toEpochMilli()){
            return true;
        } else{
            return false;
        }

    }


}