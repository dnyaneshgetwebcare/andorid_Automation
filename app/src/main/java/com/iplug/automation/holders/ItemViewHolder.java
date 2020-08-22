package com.iplug.automation.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iplug.automation.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView device_name, room_type,brightness_percent;
    public ImageView device_img;
    public RelativeLayout relativeLayout;
    public ImageButton device_status,btn_schedual;
    public ProgressBar progressBar;
    public RelativeLayout rl_container;
    public LinearLayout ll_brightness;
    public SeekBar sb_brightness;


    public ItemViewHolder(View itemView) {
        super(itemView);
        this.device_name = (TextView) itemView.findViewById(R.id.deveice_name);
        this.room_type = (TextView) itemView.findViewById(R.id.room_type);
        this.device_status = (ImageButton) itemView.findViewById(R.id.btn_change);
        this.device_img = (ImageView) itemView.findViewById(R.id.img_vw);
        this.btn_schedual=(ImageButton) itemView.findViewById(R.id.btn_schedual);
        this.progressBar = (ProgressBar) itemView.findViewById(R.id.prgress_bar_power);
        relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
        this.rl_container = (RelativeLayout) itemView.findViewById(R.id.rv_contain_holder);
        this.ll_brightness = (LinearLayout) itemView.findViewById(R.id.ll_brightness);
        this.sb_brightness = (SeekBar) itemView.findViewById(R.id.sb_brightness);
        this.brightness_percent = (TextView) itemView.findViewById(R.id.brightness_per);
    }
}