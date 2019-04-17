package com.wildcardenter.myfab.for_jahan.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wildcardenter.myfab.for_jahan.R;
import com.wildcardenter.myfab.for_jahan.adapter.SwipeCardAdapter;
import com.wildcardenter.myfab.for_jahan.helpers.SharedPrefHelper;
import com.wildcardenter.myfab.for_jahan.models.SwipeImageModel;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;
import java.util.List;

import static com.wildcardenter.myfab.for_jahan.helpers.SharedPrefHelper.SWIPECARD_KEY;
import static com.wildcardenter.myfab.for_jahan.helpers.SharedPrefHelper.TYPE_BOOLEAN;

public class SwipeImageFragment extends Fragment implements CardStackListener {
    private CardStackLayoutManager manager;
    private SwipeCardAdapter adapter;
    private CardStackView cardStackView;
    private List<SwipeImageModel> mCardList;
    private DatabaseReference reference;
    private ValueEventListener listener;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_swipe_image, container, false);
        mCardList = new ArrayList<>();
        SharedPrefHelper helper = new SharedPrefHelper(getContext());

        manager = new CardStackLayoutManager(getContext(), this);
        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.85f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        adapter = new SwipeCardAdapter(getContext(), mCardList);
        loadImages();
        cardStackView = v.findViewById(R.id.card_stack_view);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        if (helper.getData(SWIPECARD_KEY, TYPE_BOOLEAN).equals(false)) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog)
                        .setPositiveButton("OK", null)
                        .setCancelable(false)
                        .setView(R.layout.dialog_interface_custom_swipecard);
                builder.create().show();
                helper.setData(SWIPECARD_KEY, true, TYPE_BOOLEAN);
            }
        }

        return v;
    }

    private void loadImages() {

        reference = FirebaseDatabase.getInstance().getReference("Added_Img");
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCardList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SwipeImageModel sellItems = snapshot.getValue(SwipeImageModel.class);
                    mCardList.add(sellItems);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error in database fetching", Toast.LENGTH_SHORT).show();

            }
        }

        ;
        reference.addValueEventListener(listener);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        reference.removeEventListener(listener);
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        // if (manager.getTopPosition() == adapter.getItemCount() - 5) {
        // paginate();

    }

    /*  private void paginate() {
          List<SwipeCardModel> oldList = adapter.g
          List<Spot> newList = new ArrayList<Spot>() {{
              addAll(adapter.getSpots());
              addAll(createSpots());
      *+    }};
          SpotDiffCallback callback = new SpotDiffCallback(oldList, newList);
          DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
          adapter.setSpots(newList);
          result.dispatchUpdatesTo(adapter);
      }
  */
    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }


}
