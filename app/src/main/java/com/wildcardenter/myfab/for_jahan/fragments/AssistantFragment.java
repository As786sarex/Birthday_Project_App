package com.wildcardenter.myfab.for_jahan.fragments;

import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.wildcardenter.myfab.for_jahan.R;
import com.wildcardenter.myfab.for_jahan.adapter.AssistantChatView;
import com.wildcardenter.myfab.for_jahan.helpers.SharedPrefHelper;
import com.wildcardenter.myfab.for_jahan.models.AssistantMassageModel;

import java.util.ArrayList;
import java.util.Locale;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import es.dmoral.toasty.Toasty;

import static com.wildcardenter.myfab.for_jahan.helpers.SharedPrefHelper.ASSSTANT_KEY;
import static com.wildcardenter.myfab.for_jahan.helpers.SharedPrefHelper.TYPE_BOOLEAN;


public class AssistantFragment extends Fragment implements AIListener{

    private static final String TAG = "AssistantFragment";

    AIService aiService;
    RecyclerView recyclerView;
    LottieAnimationView lottieAnimationView;
    TextToSpeech textToSpeech;
    private  ArrayList<AssistantMassageModel> modelArrayList;
    LiveData<ArrayList<AssistantMassageModel>> listLiveData;
    AssistantChatView adapter;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_assistant, container, false);
        recyclerView=view.findViewById(R.id.AssistantChatRecycler);
        lottieAnimationView=view.findViewById(R.id.microphoneButton);
        modelArrayList=new ArrayList<>();
        modelArrayList.add(new AssistantMassageModel("Welcome,how can I help you?",false));

        final AIConfiguration config = new AIConfiguration("a36c66a3586f4668bdd8b382f6a7d239",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        if(getContext()!=null){
        aiService = AIService.getService(getContext(), config);
        aiService.setListener(this);
        }
        lottieAnimationView.setOnClickListener(v -> aiService.startListening());

        textToSpeech=new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int resultStatus=textToSpeech.setLanguage(Locale.ENGLISH);
                if(resultStatus==TextToSpeech.LANG_MISSING_DATA||resultStatus==TextToSpeech.LANG_NOT_SUPPORTED){
                  Toasty.error(getContext(),"Language Not Supported",Toast.LENGTH_SHORT);
                }
                else {
                    Log.d(TAG,"OK");
                }

            }
            else {
                Toasty.error(getContext(), "Initialization Failed! ", Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayoutManager linearLayout=new LinearLayoutManager(getContext(),LinearLayout.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayout);
        adapter=new AssistantChatView(getContext(),modelArrayList);
        recyclerView.setAdapter(adapter);
        listLiveData=new LiveData<ArrayList<AssistantMassageModel>>() {
            @Override
            public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<ArrayList<AssistantMassageModel>> observer) {
                super.observe(owner, observer);
            }
        };
        SharedPrefHelper helper = new SharedPrefHelper(getContext());
        if (helper.getData(ASSSTANT_KEY, TYPE_BOOLEAN).equals(false)) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog)
                        .setPositiveButton("OK", null)
                        .setCancelable(false)
                        .setView(R.layout.dialog_interface_custom_music);
                builder.create().show();
                helper.setData(ASSSTANT_KEY, true, TYPE_BOOLEAN);
            }
        }

        return view;
    }





    @Override
    public void onResult(AIResponse result) {
        Result result1=result.getResult();
        modelArrayList.add(new AssistantMassageModel(result1.getResolvedQuery(),true));
        modelArrayList.add(new AssistantMassageModel(result1.getFulfillment().getSpeech(),false));

        textToSpeech.speak(result1.getFulfillment().getSpeech(),TextToSpeech.QUEUE_FLUSH,null);
        listLiveData.observe(this, assistantMassageModels -> {
            adapter.saveChats(assistantMassageModels);
            if (assistantMassageModels!=null){
            recyclerView.scrollToPosition(assistantMassageModels.size());
            }

        });
        getActionsToDo(result1);

        adapter.notifyDataSetChanged();

    }
    private void getActionsToDo(Result r){
        if(!r.getStringParameter("PornSites").isEmpty()) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + r.getStringParameter("PornSites") + ".com"));
            startActivity(i);
        }
        else if(!r.getStringParameter("Websites").isEmpty()) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+r.getStringParameter("Websites")+".com"));
            startActivity(i);
        }


    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {
      lottieAnimationView.playAnimation();
    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {
        lottieAnimationView.cancelAnimation();
    }

    @Override
    public void onDestroy() {
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        if(modelArrayList!=null){
            modelArrayList.clear();
        }

        super.onDestroy();
    }


}


