package app.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Api(tags = "Main")
@Tag(name = "Main", description = "Получение различных view")
@RestController
@Slf4j
public class MainController {

    @GetMapping("/")
    @ApiOperation(value = "Get view \"s7\"")
    public ModelAndView index() {
        log.info("index: logging example");
        return new ModelAndView("s7");
    }

    @ApiOperation(value = "Get view \"admin\"")
    @GetMapping("/admin")
    public ModelAndView adminPage() {
        return new ModelAndView("admin");
    }

    @ApiOperation(value = "Get view \"manager\"")
    @GetMapping("/manager")
    public ModelAndView managerPage() {
        return new ModelAndView("manager");
    }

    @ApiOperation(value = "Get view \"passenger\"")
    @GetMapping("/passenger")
    public ModelAndView passengerPage() {
        return new ModelAndView("passenger");
    }
}
