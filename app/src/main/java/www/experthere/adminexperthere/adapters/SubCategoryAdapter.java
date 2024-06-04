package www.experthere.adminexperthere.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.dataModel.Subcategory;


public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private List<Subcategory> subCategoryList;
    private Activity activity;
    private OnSubCategorySelectedListener categorySelectedListener;

    public SubCategoryAdapter(List<Subcategory> subCategoryList, Activity activity, OnSubCategorySelectedListener categorySelectedListener) {
        this.subCategoryList = subCategoryList;
        this.activity = activity;
        this.categorySelectedListener = categorySelectedListener;
    }

    @NonNull
    @Override
    public SubCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.radioButton.setText(subCategoryList.get(position).getSubcategoryName());

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(activity, "sub cat name  - "+subCategoryList.get(position).getSubcategoryName(), Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, "sub cat id  - "+subCategoryList.get(position).getSubcategoryId(), Toast.LENGTH_SHORT).show();








                if (categorySelectedListener != null) {
                    categorySelectedListener.onSubCategorySelected(subCategoryList.get(position).getSubcategoryId(),subCategoryList.get(position).getSubcategoryName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioItem);
        }
    }

    // Interface to define the callback method
    public interface OnSubCategorySelectedListener {
        void onSubCategorySelected(String id,String subCatName);
    }
}
