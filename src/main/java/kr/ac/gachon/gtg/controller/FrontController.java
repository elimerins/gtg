package kr.ac.gachon.gtg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {
    private String pageTitle;
    private String breadcrumb;

    @GetMapping("/")
    public String index(Model model) {
        pageTitle = "Home";
        breadcrumb = "Home";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("breadcrumb", breadcrumb);

        return "index";
    }

    @GetMapping("/login")
    public void login() {
    }
}
