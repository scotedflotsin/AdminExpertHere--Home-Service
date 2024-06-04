package www.experthere.adminexperthere.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.dataModel.Category;
import www.experthere.adminexperthere.dataModel.Subcategory;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private ArrayList<Category> categories;
    private Activity activity;
    private OnCategorySelectedListener categorySelectedListener;

    public CategoryAdapter(ArrayList<Category> categories, Activity activity, OnCategorySelectedListener listener) {
        this.categories = categories;
        this.activity = activity;
        this.categorySelectedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.radioButton.setText(categories.get(position).getCategoryName());
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catName = categories.get(position).getCategoryName();
                List<Subcategory> subCatName = categories.get(position).getSubcategories();
                String catId = String.valueOf(categories.get(position).getCategoryId());



                if (categorySelectedListener != null) {
                    categorySelectedListener.onCategorySelected(catId,catName,subCatName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioItem);
        }
    }

    // Interface to define the callback method
    public interface OnCategorySelectedListener {
        void onCategorySelected(String id,String categoryName,List<Subcategory> subCatName);
    }
}
