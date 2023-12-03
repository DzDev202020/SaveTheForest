package com.constantine2.student.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.constantine2.student.R;
import com.constantine2.student.Utils;
import com.constantine2.student.databinding.ItemFireBinding;
import com.constantine2.student.model.Fire;

import java.util.List;

public class FireListAdapter extends RecyclerView.Adapter<FireListAdapter.FireViewHolder> {


    public interface OnItemClickListener {
        void onItemCLick(int position);
    }

    public interface OnLongClickListener {
        void onItemLongCLick(int position);
    }

    List<Fire> fireList;
    Context context;
    FireListAdapter.OnItemClickListener onItemClickListener;
    FireListAdapter.OnLongClickListener onLongClickListener;

    int oldSelected = -1;
    int newSelected = -1;

    public FireListAdapter(Context context, FireListAdapter.OnItemClickListener onItemClickListener, FireListAdapter.OnLongClickListener onLongClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public FireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new FireViewHolder(ItemFireBinding.inflate(inflater, parent, false), onItemClickListener, onLongClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FireViewHolder holder, int position) {
        Fire fire = fireList.get(position);

        holder.bind.fireAddress.setText(fire.getCity() + ", " + fire.getStreet() + ", " + fire.getZipcode());


        holder.bind.fireCreatedDate.setText(Utils.convertTimeStampToDate(fire.getCreatedDate().getTime()));
        if (fire.getControlledAt() != null)
            holder.bind.fireControlledDate.setText(Utils.convertTimeStampToDate(fire.getControlledAt().getTime()));
        else
            holder.bind.fireControlledDate.setText(context.getString(R.string.not_controlled_yet));

        if (fire.getrScales() != null)
            holder.bind.fireConfirmationCount.setText("X" + fire.getrScales().size());
        else
            holder.bind.fireConfirmationCount.setText("X" + 0);

        if (position == newSelected) {

            holder.bind.selectBorder.setVisibility(View.VISIBLE);
        } else if (position == oldSelected && newSelected == oldSelected) {
            holder.bind.selectBorder.setVisibility(View.VISIBLE);
        } else {
            holder.bind.selectBorder.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return fireList.size();
    }


    public void setFireList(List<Fire> fireList) {
        Log.e("TAG", "Adapter: setFireList: start" );
        newSelected = -1;
        oldSelected = -1;
        this.fireList = fireList;
        notifyDataSetChanged();
        Log.e("TAG", "Adapter: setFireList: end" );
    }

    public void setSelectedFire(int position) {
        if (oldSelected == position)
            return;
        newSelected = position;
        notifyItemChanged(oldSelected);
        notifyItemChanged(newSelected);
        oldSelected = position;
    }


    public static class FireViewHolder extends RecyclerView.ViewHolder {
        ItemFireBinding bind;

        public FireViewHolder(@NonNull ItemFireBinding bind,
                              FireListAdapter.OnItemClickListener onItemClickListener,
                              FireListAdapter.OnLongClickListener onLongClickListener) {
            super(bind.getRoot());
            this.bind = bind;

            bind.getRoot().setOnClickListener(v -> {
                onItemClickListener.onItemCLick(getAdapterPosition());
            });
            bind.getRoot().setOnLongClickListener(v -> {
                onLongClickListener.onItemLongCLick(getAdapterPosition());
                return true;
            });
        }
    }
}
