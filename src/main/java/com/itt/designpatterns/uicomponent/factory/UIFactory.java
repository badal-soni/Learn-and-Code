package com.itt.designpatterns.uicomponent.factory;

import com.itt.designpatterns.uicomponent.product.Button;
import com.itt.designpatterns.uicomponent.product.Checkbox;

public interface UIFactory {

    Button createButton();
    Checkbox createCheckbox();

}
