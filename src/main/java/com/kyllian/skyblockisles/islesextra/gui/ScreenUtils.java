package com.kyllian.skyblockisles.islesextra.gui;

public abstract class ScreenUtils {

    // What even is this

    public static boolean isMouseOver(double mouseX, double mouseY, int elementX, int elementY, int sizeX, int sizeY) {
        return (
                mouseX >= elementX && mouseX <= elementX + sizeX
                &&
                mouseY >= elementY && mouseY <= elementY + sizeY
                );
    }

}
