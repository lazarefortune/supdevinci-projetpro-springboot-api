package com.mobilishop.api.utils;

import java.util.function.Predicate;

public class EmailValidator implements Predicate<String> {
    @Override
    public boolean test(String email) {
        return email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }
}
