package com.edu.ulab.app.web.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserSQLConstant {
    public static final String INSERT_INTO_PERSON_VALUES = "INSERT INTO PERSON(FULL_NAME, TITLE, AGE) VALUES (?,?,?)";
    public static final String UPDATE_PERSON_ID = "UPDATE PERSON SET FULL_NAME=?, TITLE=?, AGE=? WHERE ID=?";
    public static final String SELECT_FROM_PERSON_ID = "SELECT * FROM PERSON WHERE ID=?";
    public static final String DELETE_FROM_PERSON_ID = "DELETE FROM PERSON WHERE ID=?";
}
