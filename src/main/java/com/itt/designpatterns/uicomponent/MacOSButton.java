package com.itt.designpatterns.uicomponent;

public class MacOSButton implements Button {

    @Override
    public void render() {
        System.out.println("MacOS button rendered");
    }

}
