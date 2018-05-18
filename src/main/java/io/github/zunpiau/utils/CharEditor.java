package io.github.zunpiau.utils;

import java.beans.PropertyEditorSupport;

public class CharEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null)
            this.setValue(null);
        else if (text.length() > 1)
            throw new IllegalArgumentException("[" + text + "] has too mach character");
        else
            this.setValue(text.charAt(0));
    }

}
