package app.exceptions;

import java.sql.SQLException;

public class ViolationOfForeignKeyConstraintException extends SQLException {

    public ViolationOfForeignKeyConstraintException(String message) {
        super(message);
    }

}
