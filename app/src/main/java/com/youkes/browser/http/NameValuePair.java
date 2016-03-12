package com.youkes.browser.http;

/**
 * Created by xuming on 2015/9/11.
 */
public class NameValuePair {

    private String name="";

    public NameValuePair(String name, String value) {
        this.name=name;
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String value="";

}
