package com.wildcardenter.myfab.for_jahan.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wildcardenter.myfab.for_jahan.R;
import com.wildcardenter.myfab.for_jahan.adapter.SecretMessageAdapter;
import com.wildcardenter.myfab.for_jahan.models.SecretItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecretFragment extends Fragment {

    private SecretMessageAdapter adapter;
    private List<SecretItemModel> models;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_secret, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.secret_recycler);
        models=new ArrayList<>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new SecretMessageAdapter(getContext(),models);
        recyclerView.setAdapter(adapter);
        populateList();
        return view;
    }

    private void populateList() {
        FirebaseDatabase.getInstance().getReference("secret_items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    SecretItemModel model=snapshot.getValue(SecretItemModel.class);
                    models.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
