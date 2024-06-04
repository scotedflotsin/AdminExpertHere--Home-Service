package www.experthere.adminexperthere.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.activities.CategoryEditActivity;
import www.experthere.adminexperthere.activities.EditSubcatActivity;
import www.experthere.adminexperthere.activities.SubcatActivity;
import www.experthere.adminexperthere.dataModel.Category;
import www.experthere.adminexperthere.dataModel.Subcategory;

public class SubcatAdapter extends RecyclerView.Adapter<SubcatAdapter.ViewHolder> {


    Activity activity;
    List<Subcategory> subcategories;

    public SubcatAdapter(Activity activity, List<Subcategory> subcategories) {
        this.activity = activity;
        this.subcategories = subcategories;
    }

    @NonNull
    @Override
    public SubcatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcat_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubcatAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.categoryTxt.setText(subcategories.get(position).getSubcategoryName());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, EditSubcatActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("id",subcategories.get(position).getSubcategoryId());
                bundle.putString("name",subcategories.get(position).getSubcategoryName());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return subcategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        Button editButton;

        TextView categoryTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            editButton = itemView.findViewById(R.id.editCat);
            categoryTxt = itemView.findViewById(R.id.catTxt);


        }
    }
}
