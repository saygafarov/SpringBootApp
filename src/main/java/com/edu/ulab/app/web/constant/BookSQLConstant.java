package com.edu.ulab.app.web.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BookSQLConstant {
    public static final String INSERT_INTO_BOOK_VALUES = "INSERT INTO BOOK(TITLE, AUTHOR, PAGE_COUNT, USER_ID) VALUES (?,?,?,?)";
    public static final String UPDATE_BOOK_ID = "UPDATE BOOK SET TITLE=?, AUTHOR=?, PAGE_COUNT=?, USER_ID=? WHERE ID=?";
    public static final String SELECT_FROM_BOOK_ID = "SELECT * FROM BOOK WHERE ID=?";
    public static final String DELETE_FROM_BOOK_ID = "DELETE FROM BOOK WHERE ID=?";
    public static final String SELECT_FROM_BOOK_USER_ID = "SELECT * FROM BOOK WHERE USER_ID=?";
}
