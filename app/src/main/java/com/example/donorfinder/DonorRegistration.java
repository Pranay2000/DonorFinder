package com.example.donorfinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorRegistration extends AppCompatActivity {

    private TextView backButton;
    private CircleImageView profile_image;
    private TextInputEditText registerfullname,registerIdNumber,registerPhoneNumber,registeremail,registerpassword;
    private Spinner bloodGroupsSpinner;
    private Button registerbutton;
    private Uri resultUri;
    private ProgressDialog loader;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatasbaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_registration);

        backButton=findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DonorRegistration.this, login.class);
                startActivity(intent);
            }
        });
        profile_image=findViewById(R.id.profile_image);
        registerfullname=findViewById(R.id.registerfullname);
        registerIdNumber=findViewById(R.id.registerIdNumber);
        registerPhoneNumber=findViewById(R.id.registerPhoneNumber);
        registeremail=findViewById(R.id.registeremail);
        registerpassword=findViewById(R.id.registerpassword);
        bloodGroupsSpinner=findViewById(R.id.bloodGroupSpinner);
        registerbutton=findViewById(R.id.registertbutton);
        loader=new ProgressDialog(this);

        mAuth =FirebaseAuth.getInstance();

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email=registeremail.getText().toString().trim();
                final  String password=registerpassword.getText().toString().trim();
                final String fullname=registerfullname.getText().toString().trim();
                final String idnumber=registerIdNumber.getText().toString().trim();
                final String phonenumber=registerPhoneNumber.getText().toString().trim();
                final String bloodgroup=bloodGroupsSpinner.getSelectedItem().toString();

                if(TextUtils.isEmpty(email)){
                    registeremail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    registerpassword.setError("Password is required");
                    return;
                }
                if(TextUtils.isEmpty(fullname)){
                    registerfullname.setError("Name is required");
                    return;
                }
                if(TextUtils.isEmpty(idnumber)){
                    registerIdNumber.setError("ID is required");
                    return;
                }
                if(TextUtils.isEmpty(phonenumber)){
                    registerPhoneNumber.setError("Number is required");
                    return;
                }
                if(bloodgroup.equals("Select your blood group")){
                    Toast.makeText(DonorRegistration.this, "Select Blood Group", Toast.LENGTH_SHORT).show();
                    return;
                }

                else{

                    loader.setMessage("Registering you...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                String error=task.getException().toString();
                                Toast.makeText(DonorRegistration.this, "Error"+error, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String currentUserId=mAuth.getCurrentUser().getUid();
                                userDatasbaseRef= FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(currentUserId);
                                HashMap userInfo=new HashMap();
                                userInfo.put("Id",currentUserId);
                                userInfo.put("name",fullname);
                                userInfo.put("email",email);
                                userInfo.put("idnumber",idnumber);
                                userInfo.put("phonenumber",phonenumber);
                                userInfo.put("bloodgroup",bloodgroup);
                               //userInfo.put("profileimage",profile_image);
                                userInfo.put("type","donor");
                                userInfo.put("search","donor"+bloodgroup);

                                userDatasbaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(DonorRegistration.this, "Data Set Successfully", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(DonorRegistration.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        finish();
                                        //loader.dismiss();
                                    }
                                });

                                if(resultUri!=null){
                                    final StorageReference filePath= FirebaseStorage.getInstance().getReference()
                                            .child("profile images").child(currentUserId);
                                    Bitmap bitmap=null;

                                    try{

                                        bitmap= MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);

                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
                                    byte[] data=byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask=filePath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DonorRegistration.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if(taskSnapshot.getMetadata()!=null && taskSnapshot.getMetadata().getReference()!=null){
                                                Task<Uri> result=taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl=uri.toString();
                                                        Map newImageMap=new HashMap();
                                                        newImageMap.put("profilepictureurl",imageUrl);

                                                        userDatasbaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(DonorRegistration.this, "Image url added to database successfully", Toast.LENGTH_SHORT).show();
                                                                }else{
                                                                    Toast.makeText(DonorRegistration.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });;
                                                        finish();
                                                    }
                                                });

                                                Intent intent=new Intent(DonorRegistration.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                                loader.dismiss();

                                            }
                                        }
                                    });
                                }

                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            resultUri=data.getData();
            profile_image.setImageURI(resultUri);
        }
    }
}