package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SuppressWarnings("unused")
public class IndexController {
    @GetMapping({"/", "/index"})
    public String getIndex(Model model) {
        return "index";
    }
}

