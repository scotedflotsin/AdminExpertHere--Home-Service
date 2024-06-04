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

import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.activities.CategoryEditActivity;
import www.experthere.adminexperthere.activities.SubcatActivity;
import www.experthere.adminexperthere.dataModel.Category;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.ViewHolder> {


    Activity activity;
    ArrayList<Category> categories;

    public CatAdapter(Activity activity, ArrayList<Category> categories) {
        this.activity = activity;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.categoryTxt.setText(categories.get(position).getCategoryName());
        Glide.with(activity.getApplicationContext())
                .load(categories.get(position)
                .getCategoryImage())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.catImage);

        holder.clickCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                Gson gson = new Gson();
                String subcategoryListJson = gson.toJson(categories.get(position).getSubcategories());

                bundle.putString("subcat",subcategoryListJson);
                bundle.putString("cat_name",categories.get(position).getCategoryName());
                bundle.putString("cat_id",categories.get(position).getCategoryId());


                Intent intent = new Intent(activity, SubcatActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                activity.startActivity(intent);


            }
        });


        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(activity, CategoryEditActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("id",categories.get(position).getCategoryId());
                bundle.putString("name",categories.get(position).getCategoryName());
                bundle.putString("image",categories.get(position).getCategoryImage());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                activity.startActivity(intent);


            }
        });



    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView catImage;
        LinearLayout clickCat;

        Button editButton;

        TextView categoryTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            catImage = itemView.findViewById(R.id.catImg);
            clickCat = itemView.findViewById(R.id.cat_click_lay);
            editButton = itemView.findViewById(R.id.editCat);
            categoryTxt = itemView.findViewById(R.id.catTxt);


        }
    }
}
