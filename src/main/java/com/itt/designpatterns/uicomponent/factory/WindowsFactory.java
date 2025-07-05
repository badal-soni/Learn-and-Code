package com.itt.designpatterns.uicomponent.factory;

import com.itt.designpatterns.uicomponent.product.Button;
import com.itt.designpatterns.uicomponent.product.Checkbox;
import com.itt.designpatterns.uicomponent.product.WindowsButton;
import com.itt.designpatterns.uicomponent.product.WindowsCheckbox;

public class WindowsFactory implements UIFactory {

    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }

}
