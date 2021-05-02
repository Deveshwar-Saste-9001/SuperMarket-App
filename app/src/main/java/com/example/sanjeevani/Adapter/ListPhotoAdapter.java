package com.example.sanjeevani.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sanjeevani.Model.ListPhotoModel;
import com.example.sanjeevani.R;
import com.example.sanjeevani.ViewPhotoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListPhotoAdapter extends RecyclerView.Adapter<ListPhotoAdapter.ViewHolder> {


    public static final int LIST_TYPE_LEFT = 0;
    public static final int LIST_TYPE_RIGHT = 1;
    private Context context;
    private List<ListPhotoModel> listModelList;
    private static boolean show_imagesss;

    FirebaseUser user;

    public ListPhotoAdapter(Context context, List<ListPhotoModel> chatModelList) {
        this.context = context;
        this.listModelList = chatModelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LIST_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_list_adapter, parent, false);
            show_imagesss = true;
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            show_imagesss = false;
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String message = listModelList.get(position).getMessage();
        String messasgeId = listModelList.get(position).getMessageId();
        holder.setData(message, messasgeId, position);


    }

    @Override
    public int getItemCount() {
        return listModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView show_image;
        public TextView show_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        private void setData(final String message, final String messasgeId, int index) {
            if (show_imagesss) {
                show_image = itemView.findViewById(R.id.list_PHOTO);
                if (!message.equals("")) {
                    Glide.with(context).load(message).into(show_image);
                }
                show_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent viewPhoto = new Intent(context, ViewPhotoActivity.class);
                        viewPhoto.putExtra("messageid", messasgeId);
                        viewPhoto.putExtra("imageURL", message);
                        context.startActivity(viewPhoto);
                    }
                });

            } else {
                show_message = itemView.findViewById(R.id.show_Messege);
                show_message.setText(message);
            }


        }
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (listModelList.get(position).getSender().equals(user.getUid())) {
            return LIST_TYPE_RIGHT;
        } else {
            return LIST_TYPE_LEFT;
        }
    }
}
