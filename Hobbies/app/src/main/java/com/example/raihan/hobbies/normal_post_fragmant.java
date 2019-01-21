package com.example.raihan.hobbies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class normal_post_fragmant extends android.support.v4.app.Fragment {

    private EditText postET;
    private String post;
    private ImageButton profileImage;
    private Button postSubmit;
    private static final int Gallery_Reques = 1;
    private Uri SelectImgaeUri = null;
    private StorageReference dataStorage;
    private DatabaseReference profileDatabase;
    private DatabaseReference userImageInfo;
    private DatabaseReference globalpost;
    String userImage;
    String node;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.normal_post,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle bundle = this.getArguments();
        if(bundle!=null)
        {
            node = bundle.getString("user").trim();
        }

        profileImage = (ImageButton) view.findViewById(R.id.ProfileImage);

        profileDatabase = FirebaseDatabase.getInstance().getReference();
        userImageInfo = FirebaseDatabase.getInstance().getReference("Users_info");
        globalpost = FirebaseDatabase.getInstance().getReference("global_post");




        postET = (EditText) view.findViewById(R.id.postEditText);




        dataStorage = FirebaseStorage.getInstance().getReference();

        postSubmit = (Button) view.findViewById(R.id.submitPost);


        userImageInfo.child(node).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile_info pi = dataSnapshot.getValue(profile_info.class);
                userImage = pi.getRegister_ImageUri();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Reques);
            }
        });
        postSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });



    }

    public void startPosting()
    {
        //  mProgress.setMessage("Posting ....");
        // mProgress.show();


        post = postET.getText().toString().trim();


        if(SelectImgaeUri != null)
        {

            StorageReference filePath = dataStorage.child("Post Image").child(SelectImgaeUri.getLastPathSegment());
            final ProgressDialog Dialog = new ProgressDialog(getActivity());

            filePath.putFile(SelectImgaeUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    Dialog.setMessage("Uploading...");
                    Dialog.show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    Toast.makeText(getActivity(),downloadUri.toString(),Toast.LENGTH_LONG).show();


                    normal_post_object  npobj = new normal_post_object(downloadUri.toString(),post,node,userImage);
                    profileDatabase.child("post").child(node).push().setValue(npobj);
                    globalpost.push().setValue(npobj);

                    Dialog.dismiss();

                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Gallery_Reques && resultCode == RESULT_OK)
        {
            SelectImgaeUri = data.getData();
            profileImage.setImageURI(SelectImgaeUri);
        }
    }

}

class normal_post_object{


    private String imgaeUrl,post_text,user,user_imageUri;
    public normal_post_object(String imageUrl,String post_text,String user,String user_imageUri)
    {
        this.imgaeUrl = imageUrl;
        this.post_text = post_text;
        this.user = user;
        this.user_imageUri = user_imageUri;

    }

    public normal_post_object(){}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser_imageUri() {
        return user_imageUri;
    }

    public void setUser_imageUri(String user_imageUri) {
        this.user_imageUri = user_imageUri;
    }

    public String getImgaeUrl() {
        return imgaeUrl;
    }

    public void setImgaeUrl(String imgaeUrl) {
        this.imgaeUrl = imgaeUrl;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }
}

