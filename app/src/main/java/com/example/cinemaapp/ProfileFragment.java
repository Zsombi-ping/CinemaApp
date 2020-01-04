package com.example.cinemaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {


    private EditText oldPass;
    private EditText newPass;
    private Button changeButton;
    private Button logoutButt;
    private Button camereBtn;
    private ImageView img;
    private Context context;


    public ProfileFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        super.onViewCreated(v, savedInstanceState);
        oldPass = (EditText) v.findViewById(R.id.profileCurentPassword);
        newPass = (EditText) v.findViewById(R.id.profileChangePassword);
        changeButton = v.findViewById(R.id.profileSaveButton);
        camereBtn = v.findViewById(R.id.CameraBtn);
        img = v.findViewById(R.id.imageView);

        camereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);

            }
        });


        logoutButt = v.findViewById(R.id.LogoutButton);
        changePass();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        img.setImageBitmap(bitmap);
    }

    public void changePass() {

        changeButton.setOnClickListener(v -> {

            DatabaseHelper db = new DatabaseHelper(v.getContext());


            String oldp = oldPass.getText().toString().trim();
            String newp = newPass.getText().toString().trim();

            SharedPreferences preferences = getContext().getSharedPreferences("CONTAINER", MODE_PRIVATE);
            String Activ_email = preferences.getString("EMAIL", "email");


            if (db.changePass(Activ_email, oldp, newp)) {
                Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Password change failed", Toast.LENGTH_LONG).show();
            }
        });
        logoutButt.setOnClickListener(v -> {

            Intent i = new Intent(getActivity(), LoginFragment.class);
            startActivity(i);
            ((Activity) getActivity()).overridePendingTransition(0, 0);

        });

    }


}
