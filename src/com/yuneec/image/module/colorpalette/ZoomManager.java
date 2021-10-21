package com.yuneec.image.module.colorpalette;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Global;
import com.yuneec.image.utils.YLog;
import javafx.geometry.Rectangle2D;

public class ZoomManager {

    static int[][] rect = {
            {0, 0, 640, 512}, {5, 4, 630, 504}, {10, 8, 620, 496}, {15, 12, 610, 488},
            {20, 16, 600, 480}, {25, 20, 590, 472}, {30, 24, 580, 464}, {35, 28, 570, 456},
            {40, 32, 560, 448}, {45, 36, 550, 440}, {50, 40, 540, 432}, {55, 44, 530, 424},
            {60, 48, 520, 416}, {65, 52, 510, 408}, {70, 56, 500, 400}, {75, 60, 490, 392},
            {80, 64, 480, 384}, {85, 68, 470, 376}, {90, 72, 460, 368}, {95, 76, 450, 360},
            {100, 80, 440, 352}, {105, 84, 430, 344}, {110, 88, 420, 336}, {115, 92, 410, 328},
            {120, 96, 400, 320}, {125, 100, 390, 312}, {130, 104, 380, 304}, {135, 108, 370, 296},
            {140, 112, 360, 288}, {145, 116, 350, 280}, {150, 120, 340, 272}, {155, 124, 330, 264},
            {160, 128, 320, 256}, {165, 132, 310, 248}, {170, 136, 300, 240}, {175, 140, 290, 232},
            {180, 144, 280, 224}, {185, 148, 270, 216}, {190, 152, 260, 208}, {195, 156, 250, 200},
            {200, 160, 240, 192}, {205, 164, 230, 184}, {210, 168, 220, 176}, {215, 172, 210, 168},
            {220, 176, 200, 160}, {225, 180, 190, 152}, {230, 184, 180, 144}, {235, 188, 170, 136},
            {240, 192, 160, 128}, {245, 196, 150, 120}, {250, 200, 140, 112}, {255, 204, 130, 104},
            {260, 208, 120, 96}, {265, 212, 110, 88}, {270, 216, 100, 80}, {275, 220, 90, 72},
            {280, 224, 80, 64}};

    public static int[] zoomRect;

    public static ZoomManager instance;

    public static ZoomManager I() {
        if (instance == null) {
            instance = new ZoomManager();
        }
        return instance;
    }

    public int[] getRect(){
        int index = (int) (Global.dzoom * 8) - 8;
        zoomRect = rect[index];
        return zoomRect;
    }

    public void zoomImage(){
//        YLog.I("ZoomManager rect : " + rect[0] + "," + rect[1] + "," + rect[2] + "," + rect[3] +
//                " ,PaletteParam.currentPalette:" + PaletteParam.currentPalette +
//                " ,Global.imagePaletteType:" + Global.imagePaletteType);
        if (PaletteParam.currentPalette != Global.imagePaletteType){
            CenterPane.getInstance().imageView.setViewport(new Rectangle2D(zoomRect[0],zoomRect[1],zoomRect[2],zoomRect[3]));
        }else {
            CenterPane.getInstance().imageView.setViewport(new Rectangle2D(0, 0, 640, 512));
        }
    }
}
