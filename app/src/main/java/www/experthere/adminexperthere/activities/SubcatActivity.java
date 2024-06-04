package www.experthere.adminexperthere.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.adapters.SubcatAdapter;
import www.experthere.adminexperthere.dataModel.Subcategory;

public class SubcatActivity extends AppCompatActivity {

    List<Subcategory> subcategories = new ArrayList<>();
    RecyclerView recyclerView;

    SubcatAdapter adapter;
    String catID, catName ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.theme_Blue, this.getTheme()));

        setContentView(R.layout.activity_subcat);

        TextView catTxt = findViewById(R.id.titleTxt);
         recyclerView = findViewById(R.id.recyclerView);

        findViewById(R.id.backBtn).setOnClickListener(v -> finish());
        findViewById(R.id.addSubCat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SubcatActivity.this, CreateSubcategoryActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("id",catID);
                bundle.putString("catName",catName);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){

            String subcategoryListJson = bundle.getString("subcat");
             catName = bundle.getString("cat_name");

            catID = bundle.getString("cat_id");
            catTxt.setText(catName);


            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Subcategory>>(){}.getType();
            ArrayList<Subcategory> receivedList = gson.fromJson(subcategoryListJson, type);

           

            if (receivedList!=null) {
                
                subcategories.addAll(receivedList);

                setUpRecyclerView(subcategories);

            }

        }





    }

    private void setUpRecyclerView(List<Subcategory> subcategories) {


        adapter = new SubcatAdapter(SubcatActivity.this,subcategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);




    }
}