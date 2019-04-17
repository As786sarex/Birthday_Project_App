package com.wildcardenter.myfab.for_jahan.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.wildcardenter.myfab.for_jahan.R;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class AddPhotoActivity extends AppCompatActivity {

    StorageTask uploadTask;
    StorageReference storageReference;
    private Uri imageUri;
    private ImageView uploadImgPrev, cancelBtn;
    private EditText addImgTitle, addImgMessage;
    private TextView uploadBtn;
    private String nameInStorage;
    private String myimageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        uploadImgPrev = findViewById(R.id.AddImgImg);
        addImgTitle = findViewById(R.id.addimgTitle);
        addImgMessage = findViewById(R.id.AddimgMessage);
        cancelBtn = findViewById(R.id.AddImgCancel);
        uploadBtn = findViewById(R.id.AddImgUpload);
        storageReference = FirebaseStorage.getInstance().getReference("Added_Img");
        uploadBtn.setOnClickListener(v -> {
            String title = addImgTitle.getText().toString();
            String message = addImgMessage.getText().toString();
            if (!title.isEmpty() && !message.isEmpty()) {
                uploadImg(title, message);
            } else {
                Toasty.warning(AddPhotoActivity.this, "Fields Required",
                        Toast.LENGTH_SHORT, true).show();
            }
        });
        cancelBtn.setOnClickListener(v -> {
            startActivity(new Intent(AddPhotoActivity.this, MainActivity.class));
            finish();
        });

        CropImage.activity()
                .setActivityTitle("Select Image")
                .start(AddPhotoActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            uploadImgPrev.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Cancelled!!!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddPhotoActivity.this, MainActivity.class));
            finish();
        }
    }

    private void uploadImg(final String title, final String message) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading..");
        progressDialog.show();
        if (imageUri != null) {
            nameInStorage = System.currentTimeMillis() + ".jpeg";
            final StorageReference file = storageReference.child(nameInStorage);

            uploadTask = file.putFile(imageUri);
            uploadTask.continueWithTask((Continuation) task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return file.getDownloadUrl();
            }).addOnCompleteListener(AddPhotoActivity.this, (OnCompleteListener<Uri>) task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    myimageUri = downloadUri.toString();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Added_Img");

                    String postId = reference.push().getKey();

                    HashMap<String, Object> objectHashMap = new HashMap<>();
                    objectHashMap.put("postId", postId);
                    objectHashMap.put("ImageUrl", myimageUri);
                    objectHashMap.put("imageTitle", title);
                    objectHashMap.put("imageMessage", message);
                    objectHashMap.put("imageName", nameInStorage);
                    reference.child(postId).setValue(objectHashMap);
                    progressDialog.dismiss();
                    startActivity(new Intent(AddPhotoActivity.this, MainActivity.class));
                    finish();

                } else {
                    Toast.makeText(AddPhotoActivity.this, "Failed to upload!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(AddPhotoActivity.this, e -> {
                Toast.makeText(AddPhotoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            });
        } else {
            Toast.makeText(this, "No Images Selected!!", Toast.LENGTH_SHORT).show();
        }
    }


}
