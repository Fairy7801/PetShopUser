package com.example.petshopuser.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopuser.R;
import com.example.petshopuser.model.ModelItem;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterViewPayer extends SliderViewAdapter<AdapterViewPayer.SliderAdapterVH> {
    private Context context;
    private List<ModelItem> modelItems = new ArrayList<>();

    public AdapterViewPayer(Context context) {
        this.context = context;
    }

    public void ViewPagerAdapter(List<ModelItem> modelItems) {
        this.modelItems = modelItems;
        notifyDataSetChanged();
    }

    public void deleteitem(int postion){
        this.modelItems.remove(postion);
        notifyDataSetChanged();
    }
    public void addItem(ModelItem sliderItem) {
        this.modelItems.add(sliderItem);
        notifyDataSetChanged();
    }
    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.imgae_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        ModelItem sliderItem = modelItems.get(position);

        viewHolder.textViewDescription.setText(sliderItem.getDescription());
        viewHolder.textViewDescription.setTextSize(16);
        viewHolder.textViewDescription.setTextColor(Color.WHITE);
        Picasso.get()
                .load(sliderItem.getImageurl())
                .fit()  // Tùy chỉnh phương thức fit() tương đương với fitCenter() trong Glide
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return modelItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}