package com.example.sanjeevani.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sanjeevani.Model.NotificationModel;
import com.example.sanjeevani.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationModel> notificationModelList;

    public NotificationAdapter(List<NotificationModel> notificationModelList) {
        this.notificationModelList = notificationModelList;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {

        String image = notificationModelList.get(position).getNotificationImage();
        String body = notificationModelList.get(position).getNotificationBody();
        boolean readed = notificationModelList.get(position).isReaded();
        holder.setData(image, body, readed);

    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView notiImage;
        private TextView notiBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notiImage = itemView.findViewById(R.id.notification_Image);
            notiBody = itemView.findViewById(R.id.notification_textView);
        }

        private void setData(String image, String body, boolean readed) {
            Glide.with(itemView.getContext()).load(image).into(notiImage);
            if (readed) {
                notiBody.setAlpha(0.5f);
            } else {
                notiBody.setAlpha(1f);
            }
            notiBody.setText(body);


        }
    }
}
