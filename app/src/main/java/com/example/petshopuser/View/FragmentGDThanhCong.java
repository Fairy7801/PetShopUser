package com.example.petshopuser.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.petshopuser.R;
import com.example.petshopuser.adapter.XacNhanAdapter;
import com.example.petshopuser.callback.HDCTCallBack;
import com.example.petshopuser.dao.DaoHDCT;
import com.example.petshopuser.model.HDCT;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class FragmentGDThanhCong extends Fragment {
    XacNhanAdapter xacNhanAdapter;
    DaoHDCT databaseHDCT;
    RecyclerView rcvthanhcong;
    ArrayList<HDCT> arrayList;
    FirebaseUser firebaseUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentgdtc,container,false);
        rcvthanhcong= view.findViewById(R.id.rcvthanhcong);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
        rcvthanhcong.setLayoutManager(linearLayoutManager);
        databaseHDCT = new DaoHDCT(getActivity());
        arrayList=new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseHDCT.getAll(new HDCTCallBack() {
            @Override
            public void onSuccess(ArrayList<HDCT> lists) {
                arrayList.clear();
                for (int i =0;i<lists.size();i++){
                    if (lists.get(i).getUiduser().equalsIgnoreCase(firebaseUser.getUid()) && lists.get(i).isCheck() == true){
                        arrayList.add(lists.get(i));
                        xacNhanAdapter = new XacNhanAdapter(arrayList,getActivity());
                        rcvthanhcong.setAdapter(xacNhanAdapter);
                    }
                }
            }


            @Override
            public void onError(String message) {

            }
        });

        return view;
    }
}
