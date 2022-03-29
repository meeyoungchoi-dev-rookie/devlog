package com.blog.devlog.domain.blogpage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class BlogController {

    @GetMapping("/")
    public String intro() {
        return "index";
    }
}
