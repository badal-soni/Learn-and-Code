package com.itt.designpatterns.uicomponent.factory;

import com.itt.designpatterns.uicomponent.product.Button;
import com.itt.designpatterns.uicomponent.product.Checkbox;
import com.itt.designpatterns.uicomponent.product.MacOSButton;
import com.itt.designpatterns.uicomponent.product.MacOSCheckbox;

public class MacOSFactory implements UIFactory {

    @Override
    public Button createButton() {
        return new MacOSButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new MacOSCheckbox();
    }

}
