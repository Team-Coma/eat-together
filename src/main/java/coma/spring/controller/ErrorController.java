package coma.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error/")
public class ErrorController {

	
	@RequestMapping("partyfullError")
	public String toPartyfull() {
		return "/error/partyfull";
	}
}
