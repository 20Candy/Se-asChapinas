package com.screens.flows.dictionary.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.components.dictionary.CategoryDictionary;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private List<String> categories;
    private Context context;
    private OnCardClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnCardClickListener {
        void onCardClicked(boolean isFavorite, String category);
    }

    public CategoriesAdapter(Context context, List<String> categories, OnCardClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryDictionary categoryView = new CategoryDictionary(context);
        return new ViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String category = categories.get(position);
        holder.bind(category);

        boolean isFavorite = "Favoritos".equals(category);
        holder.categoryView.findViewById(com.components.R.id.heart).setVisibility(isFavorite ? View.VISIBLE : View.GONE);

        // Cambiar el estado visual dependiendo si está seleccionado o no
        holder.categoryView.setSelected(selectedPosition == position);

        holder.categoryView.setOnClickListener(v -> {
            RecyclerView recyclerView = (RecyclerView) holder.itemView.getParent();
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

            // Si la categoría seleccionada es la misma, deseleccionarla
            if (selectedPosition == holder.getAdapterPosition()) {
                // Deseleccionar la categoría
                selectedPosition = RecyclerView.NO_POSITION;
                notifyItemChanged(holder.getAdapterPosition());
                listener.onCardClicked(isFavorite, "");

                // Mover el scroll a la primera posición cuando no hay nada seleccionado
                if (layoutManager instanceof LinearLayoutManager) {
                    ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(0, 0);
                }
            } else {
                // Seleccionar una nueva categoría
                int previousSelectedPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();

                // Notificar cambios en los elementos seleccionados y deseleccionados
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition);

                listener.onCardClicked(isFavorite, category);

                // Mover el scroll al nuevo elemento seleccionado con desplazamiento preciso
                if (layoutManager instanceof LinearLayoutManager) {
                    ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(selectedPosition, 0);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CategoryDictionary categoryView;

        public ViewHolder(@NonNull CategoryDictionary itemView) {
            super(itemView);
            categoryView = itemView;
        }

        public void bind(String category) {
            TextView tvCategory = categoryView.findViewById(com.components.R.id.tvCategory);
            tvCategory.setText(category);
        }
    }
}
