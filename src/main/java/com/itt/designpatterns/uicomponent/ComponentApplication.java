package com.itt.designpatterns.uicomponent;

import com.itt.designpatterns.uicomponent.factory.MacOSFactory;
import com.itt.designpatterns.uicomponent.factory.UIFactory;
import com.itt.designpatterns.uicomponent.factory.WindowsFactory;
import com.itt.designpatterns.uicomponent.product.Button;
import com.itt.designpatterns.uicomponent.product.Checkbox;

public class ComponentApplication {

    public static void main(String[] args) {
        UIFactory windowsFactory = new WindowsFactory();
        Button windowsButton = windowsFactory.createButton();
        Checkbox windowsCheckbox = windowsFactory.createCheckbox();
        windowsButton.render();
        windowsCheckbox.render();

        UIFactory macFactory = new MacOSFactory();
        Button macButton = macFactory.createButton();
        Checkbox macCheckbox = macFactory.createCheckbox();
        macButton.render();
        macCheckbox.render();
    }

}
