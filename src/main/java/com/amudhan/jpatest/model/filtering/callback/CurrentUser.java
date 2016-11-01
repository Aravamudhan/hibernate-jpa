package com.amudhan.jpatest.model.filtering.callback;

public class CurrentUser extends ThreadLocal<User>{

	public static CurrentUser INSTANCE = new CurrentUser();

    private CurrentUser() {
    }
}
