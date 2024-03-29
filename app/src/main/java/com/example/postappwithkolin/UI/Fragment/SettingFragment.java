package com.example.postappwithkolin.UI.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.postappwithkolin.R;
import com.example.postappwithkolin.UI.MainActivity;
import com.example.postappwithkolin.databinding.FragmentSettingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    FragmentSettingBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    onLogOutClickListener logOutClickListener;
    onChangeUserNameClickListener changeUserNameClickListener;
    onChangeProfilePhotoClickListener changeProfilePhotoClickListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        logOutClickListener = (onLogOutClickListener) context;
        changeUserNameClickListener = (onChangeUserNameClickListener) context;
        changeProfilePhotoClickListener = (onChangeProfilePhotoClickListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        binding.profileUserName.setText(mAuth.getCurrentUser().getDisplayName());
        Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).into(binding.profilePhoto);



        binding.fragmentSettingLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.Companion.isConnected())
                    logOutClickListener.onLogOut();
            }
        });

        binding.fragmentSettingChangeUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.Companion.isConnected())
                    changeUserNameClickListener.changeUserName();
            }
        });

        binding.fragmentSettingChangeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.Companion.isConnected())
                    changeProfilePhotoClickListener.ChangeProfilePhoto();
            }
        });

        binding.profileUserName.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
        Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).into(binding.profilePhoto);
        return view;
    }


    public interface onLogOutClickListener{
        void onLogOut();
    }
    public interface onChangeUserNameClickListener{
        void changeUserName();
    }
    public interface onChangeProfilePhotoClickListener{
        void ChangeProfilePhoto();
    }
}