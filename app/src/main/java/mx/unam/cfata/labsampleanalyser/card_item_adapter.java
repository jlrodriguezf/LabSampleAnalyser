package mx.unam.cfata.labsampleanalyser;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by José Luis Rodríguez Fragoso.
 */

public class card_item_adapter extends RecyclerView.Adapter<card_item_adapter.card_item_viewholder> {
    private List<card_item> item;
    private Context Ctx;

    public static class  card_item_viewholder extends RecyclerView.ViewHolder {
        public CardView card_item;
        public RoundedImageView thumbnail;
        public TextView sample_name;
        public ImageButton popup_menu;

        public card_item_viewholder(View view){
            super(view);
            card_item = (CardView) view.findViewById(R.id.card_item);
            thumbnail = (RoundedImageView) view.findViewById(R.id.thumbnail);
            sample_name = (TextView) view.findViewById(R.id.sample_name);
            popup_menu = (ImageButton) view.findViewById(R.id.popup_menu);
        }
    }

    @Override
    public card_item_adapter.card_item_viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new card_item_viewholder(view);


    }

    @Override
    public void onBindViewHolder(final card_item_viewholder viewHolder, int position) {
        viewHolder.thumbnail.setImageResource(item.get(position).getThumbnail());
        viewHolder.sample_name.setText(item.get(position).getSample_name());
        viewHolder.popup_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(Ctx, viewHolder.popup_menu);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem){
                        switch (menuItem.getItemId()){
                            case R.id.quantifications:
                                return true;
                            case R.id.rename:
                                return true;
                            case R.id.delete:
                                return true;
                            default:
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public card_item_adapter(List<card_item> item, Context Ctx){
        this.item = item;
        this.Ctx = Ctx;
    }
}
