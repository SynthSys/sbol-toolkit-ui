/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author jhay
 */
@RestController
public class HomeController {

    @RequestMapping(name = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        var mav = new ModelAndView();

        String message = "Greetings from Spring Boot!";

        mav.setViewName("home");
        mav.addObject("message", message);
        mav.getModelMap().addAttribute("message", message);

        return mav;
    }
}
