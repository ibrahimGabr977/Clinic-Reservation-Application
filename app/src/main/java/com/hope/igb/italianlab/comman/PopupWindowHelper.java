package com.hope.igb.italianlab.comman;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PopupWindowHelper {


    private final View windowRoot;
    private final PopupWindowListener listener;
    private final int animation_resource;
    private  PopupWindow popupWindow;


    public interface PopupWindowListener{
        void onPopupViewRootCreated(View windowRoot, String view_type, Object displayed_object);
        //displayed_object represent the items that needed to displayed on popup window
        //like the image reference, request class, reservation class and so on
        //view_type represent the required popup window and the required displayed_object
        //like reservation window, request window, view achievement image window and so on
    }

    public PopupWindowHelper(Activity activity, int container_resource, int animation_resource, PopupWindowListener listener) {
        this.listener = listener;
        this.animation_resource = animation_resource;

        //initialize popup window view
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        windowRoot = inflater.inflate(container_resource,null, false);

    }


    //for setting up create post popup window
    public void displayWindow(double screenHeight, double screenWidth, String view_type, Object displayed_object){


        listener.onPopupViewRootCreated(windowRoot, view_type, displayed_object);


         popupWindow= new PopupWindow(windowRoot, (int) (screenWidth),
                (int) (screenHeight));

        popupWindow.setAnimationStyle(animation_resource); //show from down animation
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update(0,0,ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);







        //show popup window
        popupWindow.showAtLocation(windowRoot, Gravity.CENTER, 0, 0);


    }

    public void dismissWindow(){
        if (popupWindow != null)
            popupWindow.dismiss();
    }

}
