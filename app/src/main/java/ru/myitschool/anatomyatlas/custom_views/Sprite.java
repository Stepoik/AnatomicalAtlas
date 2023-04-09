package ru.myitschool.anatomyatlas.custom_views;

import android.graphics.Bitmap;

public class Sprite {
    private Bitmap bitmap;
    public Sprite(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    public boolean isPressed(int x, int y){
        if (x < bitmap.getWidth() && y < bitmap.getHeight() && x > 0 && y > 0) {
            return !(bitmap.getColor(x, y).alpha() < 0.5);
        }
        return false;
    }
}
