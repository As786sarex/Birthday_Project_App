package com.wildcardenter.myfab.for_jahan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wildcardenter.myfab.for_jahan.R;
import com.wildcardenter.myfab.for_jahan.models.AssistantMassageModel;

import java.util.ArrayList;

public class AssistantChatView extends RecyclerView.Adapter<AssistantChatView.AssistantViewHolder> {
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private Context context;
    private  ArrayList<AssistantMassageModel> arrayList;

    public AssistantChatView(Context context, ArrayList<AssistantMassageModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AssistantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == MSG_TYPE_LEFT) {
            return new AssistantViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.assistant_chat_item_left, viewGroup, false));
       }
        else{
            return new AssistantViewHolder(LayoutInflater.from(context)
                 .inflate(R.layout.assistant_chat_item_right, viewGroup, false));
       }
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position).isMe()) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull AssistantViewHolder viewHolder, int i) {
        AssistantMassageModel currentItem=arrayList.get(i);
        viewHolder.textView.setText(currentItem.getMassage());


    }
    public void saveChats(ArrayList<AssistantMassageModel> a){
        this.arrayList.clear();
        this.arrayList=a;
        notifyItemInserted(a.size());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AssistantViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public AssistantViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.AssistantChat);

        }
    }
}
