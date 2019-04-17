package com.wildcardenter.myfab.for_jahan.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.wildcardenter.myfab.for_jahan.models.SwipeCardModel;
import com.wildcardenter.myfab.for_jahan.R;
import com.wildcardenter.myfab.for_jahan.models.SwipeImageModel;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class SwipeCardAdapter extends RecyclerView.Adapter<SwipeCardAdapter.SwipeCardViewHolder>{
    private Context context;
    private List<SwipeImageModel> swipeCardModelList;

    public SwipeCardAdapter(Context context, List<SwipeImageModel> swipeCardModelList) {
        this.context = context;
        this.swipeCardModelList = swipeCardModelList;

    }

    @NonNull
    @Override
    public SwipeCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SwipeCardViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_content,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SwipeCardViewHolder swipeCardViewHolder, int i) {
        SwipeImageModel currentItem=swipeCardModelList.get(i);
        String photoAdd=currentItem.getImageUrl();
        String title=currentItem.getImageTitle();
        String desc=currentItem.getImageMessage();
        Picasso.with(context).load(photoAdd).centerCrop().fit()
                .into(swipeCardViewHolder.photoAdd);

        swipeCardViewHolder.title.setText(title);
        swipeCardViewHolder.desc.setText(desc);
        swipeCardViewHolder.removeBtn.setOnClickListener(v->{
            new AlertDialog.Builder(context).setTitle("Delete Card??")
                    .setIcon(R.drawable.random)
                    .setMessage("You're about to delete one card.\nAre You Sure?")
                    .setPositiveButton("Delete", (dialog, which) ->{
                        FirebaseDatabase.getInstance().getReference().child("Added_Img").child(currentItem.getPostId()).removeValue();
                        FirebaseStorage.getInstance().getReference("Added_Img").child(currentItem.getImageName()).delete();
                        Toasty.success(context,"Successfully Deleted", Toast.LENGTH_SHORT,true);
                    })
                    .setNegativeButton("Cancel",null)
                    .create()
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return swipeCardModelList.size();
    }

    public class SwipeCardViewHolder extends RecyclerView.ViewHolder{
        public KenBurnsView photoAdd;
        public TextView title;
        public TextView desc;
        private ImageView removeBtn;
        public SwipeCardViewHolder(@NonNull View itemView) {
            super(itemView);
            photoAdd=itemView.findViewById(R.id.item_image);
            RandomTransitionGenerator generator=new RandomTransitionGenerator();
            generator.setTransitionDuration(5000);
            photoAdd.setTransitionGenerator(generator);
            title=itemView.findViewById(R.id.card_title);
            desc=itemView.findViewById(R.id.card_desc);
            removeBtn=itemView.findViewById(R.id.removeCardBtn);

        }
    }
}
