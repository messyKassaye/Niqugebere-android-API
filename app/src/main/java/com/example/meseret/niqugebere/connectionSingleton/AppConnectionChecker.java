package com.example.meseret.niqugebere.connectionSingleton;

/**
 * Created by meseret on 3/1/18.
 */

public class AppConnectionChecker {

    public static AppConnectionChecker checker=new AppConnectionChecker();

    private AppConnectionChecker(){

    }

    public AppConnectionChecker getInstance(){
        return  checker;
    }
}
