package edu.cos80023;

import java.io.Serializable;

public class SomeClass implements Serializable{

    private String name=null;
    public void setName(String string) {
        this.name=string;
    }

    public Object getName() {
        return this.name;
    }

}
