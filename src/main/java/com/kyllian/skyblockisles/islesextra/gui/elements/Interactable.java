package com.kyllian.skyblockisles.islesextra.gui.elements;

import com.kyllian.skyblockisles.islesextra.gui.TexturedGui;

public interface Interactable {

    int getScreenX(TexturedGui gui);
    int getScreenY(TexturedGui gui);

    int getSizeX();
    int getSizeY();

    void interact();

}
