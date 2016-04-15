package com.example.oranmoshe.finalmobileproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class RecycleTaskAdapterManager extends RecyclerView.Adapter<RecycleTaskAdapterManager.ViewHolder> {
    private ArrayList<RecycleTaskItem> mDataset;
    private Context context;

    private final ArrayList<Integer> selected = new ArrayList<>();


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtUserName;
        public EditText edtHeader;
        public CardView cardView;

        public ViewHolder(View v) {
            super(v);

            txtHeader = (TextView) v.findViewById(R.id.firstLine);

            txtUserName = (TextView) v.findViewById(R.id.secondLine);

            cardView = (CardView) v.findViewById(R.id.card_view);

            v.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
        }
    }

    public void add(int position, RecycleTaskItem item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(RecycleItem item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecycleTaskAdapterManager(ArrayList<RecycleTaskItem> myDataset) {
        mDataset = myDataset;
    }


    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecycleTaskAdapterManager.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        context = parent.getContext();
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_task_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final RecycleTaskItem task = mDataset.get(position);
        holder.txtHeader.setText(task.GetName());
        holder.txtUserName.setText(task.GetUserName() + " " + task.GetDate());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getLayoutPosition());
                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPosition(holder.getLayoutPosition());
            }
        });

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    // Clean all elements of the recycler
    public void clear() {
        mDataset.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(ArrayList<RecycleTaskItem> list) {
        mDataset.addAll(list);
        notifyDataSetChanged();
    }

    public RecycleTaskItem getRecyceItem(int position){
        return mDataset.get(position);
    }

}