package com.example.sanjeevani.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanjeevani.Interface.ItemClickListner;
import com.example.sanjeevani.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtproductName,txtproductDescription,txtProductPrice;
    public ImageView productImage;
    public ItemClickListner listner;



    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productImage= (ImageView) itemView.findViewById(R.id.product_Photos1);
        txtproductName=(TextView) itemView.findViewById(R.id.product_Name1);
        txtproductDescription=(TextView) itemView.findViewById(R.id.product_Description1);
        txtProductPrice=(TextView) itemView.findViewById(R.id.product_Price1);


    }

    @Override
    public void onClick(View view)
    {

        listner.onClick(view,getAdapterPosition(),false);
    }




    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner=listner;
    }


}
