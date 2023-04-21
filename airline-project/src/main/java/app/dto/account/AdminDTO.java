package app.dto.account;


import app.entities.account.Admin;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.NoArgsConstructor;

@JsonTypeName(value = "admin")
@NoArgsConstructor
public class AdminDTO extends AccountDTO {


    public AdminDTO(Admin admin) {
        super(admin);
    }

    @Override
    public Admin convertToEntity() {
        Admin admin = new Admin();
        admin.setId(getId());
        admin.setEmail(getEmail());
        admin.setPassword(getPassword());
        admin.setSecurityQuestion(getSecurityQuestion());
        admin.setAnswerQuestion(getAnswerQuestion());
        admin.setRoles(getRoles());
        return admin;
    }

}
