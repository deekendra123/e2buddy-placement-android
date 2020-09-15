package com.placement.prepare.e2buddy.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.activity.PlacementInstructionActivity;
import com.placement.prepare.e2buddy.fragmentdialog.PlacementCategorySheet;
import com.placement.prepare.e2buddy.object.CategoryData;
import com.placement.prepare.e2buddy.preference.SessionManager;

import java.util.List;

public class PlacementSubCategoryAdapter extends RecyclerView.Adapter<PlacementSubCategoryAdapter.PaperHolder> {

    Context mCtx;
    List<CategoryData> list;

    public PlacementSubCategoryAdapter(Context mCtx, List<CategoryData> list) {
        this.mCtx = mCtx;
        this.list = list;
    }

    @NonNull
    @Override
    public PaperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.subcategory_list_items, parent, false);
        return new PaperHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaperHolder holder, int position) {

        CategoryData categoryData = list.get(position);
        holder.tvQuiz.setText(""+categoryData.getCategoryName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacementCategorySheet.placementCategorySheet.dismiss();
               mCtx.startActivity(new Intent(mCtx, PlacementInstructionActivity.class));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaperHolder extends RecyclerView.ViewHolder {

        TextView tvQuiz;

        public PaperHolder(View itemView) {
            super(itemView);
            tvQuiz = itemView.findViewById(R.id.tvQuiz);
        }
    }
}
