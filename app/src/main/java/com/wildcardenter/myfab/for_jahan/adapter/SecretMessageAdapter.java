package com.wildcardenter.myfab.for_jahan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wildcardenter.myfab.for_jahan.R;
import com.wildcardenter.myfab.for_jahan.models.SecretItemModel;

import java.util.List;

public class SecretMessageAdapter extends RecyclerView.Adapter<SecretMessageAdapter.SecretMessageViewHolder> {

    private static final int TYPE_SECRET_IMG = 0;
    private static final int TYPE_SECRET_TEXT = 1;

    private Context context;
    private List<SecretItemModel> itemModelList;

    public SecretMessageAdapter(Context context, List<SecretItemModel> itemModelList) {
        this.context = context;
        this.itemModelList = itemModelList;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemModelList.get(position).getSecret_type() == 2) {
            return TYPE_SECRET_TEXT;
        } else {
            return TYPE_SECRET_IMG;
        }
    }

    @NonNull
    @Override
    public SecretMessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == TYPE_SECRET_TEXT) {
            return new SecretMessageViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.secret_text_item_blueprint, viewGroup, false));
        } else {
            return new SecretMessageViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.secret_img_item_blueprint, viewGroup, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull SecretMessageViewHolder secretMessageViewHolder, int i) {
        SecretItemModel currentItem = itemModelList.get(i);
        if (secretMessageViewHolder.secretImage != null) {
            Picasso.with(context).load(currentItem.getSecret_img_url()).into(secretMessageViewHolder.secretImage);
        }
        secretMessageViewHolder.secretTitle.setText(currentItem.getSecret_title());
        secretMessageViewHolder.secretDesc.setText(currentItem.getSecret_desc());
    }

    @Override
    public int getItemCount() {
        if (itemModelList != null) {
            return itemModelList.size();
        } else {
            return 0;
        }
    }

    class SecretMessageViewHolder extends RecyclerView.ViewHolder {

        ImageView secretImage;
        TextView secretTitle, secretDesc;

        SecretMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            secretImage = itemView.findViewById(R.id.secret_item_image);
            secretTitle = itemView.findViewById(R.id.secret_item_title);
            secretDesc = itemView.findViewById(R.id.secret_item_desc);
        }
    }
}
