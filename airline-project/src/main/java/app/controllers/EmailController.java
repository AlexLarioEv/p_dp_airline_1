package app.controllers;

import app.services.MailSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Email")
@Tag(name = "Email", description = "Отправка сообщений на email")
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final MailSender mailSender;

    @GetMapping(value = "/simple-email/{user-email}")
    @ApiOperation(value = "Send email to User")
    public @ResponseBody ResponseEntity<String> sendSimpleEmail(
            @ApiParam(
                    name = "email",
                    value = "User's email"
            )
            @PathVariable("user-email") String email) {

        try {
            mailSender.send(email, "Welcome", "This is a welcome email for your!!");
        } catch (MailException mailException) {
            return new ResponseEntity<>("Unable to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Please check your inbox", HttpStatus.OK);
    }
}
