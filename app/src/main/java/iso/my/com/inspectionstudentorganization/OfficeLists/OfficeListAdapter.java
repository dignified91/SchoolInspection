package iso.my.com.inspectionstudentorganization.OfficeLists;


import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import iso.my.com.inspectionstudentorganization.Models.OfficeList;
import iso.my.com.inspectionstudentorganization.R;

public class OfficeListAdapter extends RecyclerView.Adapter<OfficeListAdapter.OffViewHolder>
{

    private MessageAdapterListener listener;
    private Activity activity;
    private Context context;
    List<OfficeList> officeLists;
    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;
    private int currentSelectedIndex = -1;


    public OfficeListAdapter(Context context, List<OfficeList> off, MessageAdapterListener listener)
    {

        this.context = context;

        this.officeLists = off;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
        this.listener = listener;
    }

    @Override
    public OffViewHolder onCreateViewHolder (ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.off_list_item, parent, false);
        return new OffViewHolder(view);

    }

    @Override
    public void onBindViewHolder (OffViewHolder holder, int position)
    {
        OfficeList req = officeLists.get(position);

        holder.officename.setText(req.getName());

      //  holder.delete.setOnClickListener((View.OnClickListener) v -> delete(holder.getAdapterPosition()));
        holder.itemView.setActivated(selectedItems.get(position, false));

       // applyIsRead(holder, req);
        applyClickEvents(holder, position);

    }


    private void delete (int position)
    {
        officeLists.remove(position);
        notifyItemRemoved(position);

    }


    private void resetAxisY (View v)
    {
        if (v.getRotationY() != 0)
        {
            v.setRotationY(0);
        }
    }

    public void resetAnimations ()
    {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    private void applyClickEvents (OffViewHolder holder, final int position)
    {
        holder.messageLayout.setOnClickListener(v -> listener.onMessageClicked(position));
    }



    @Override
    public int getItemCount ()
    {
        return officeLists.size();
    }

    public void toggleSelection (int pos)
    {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false))
        {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        }
        else
        {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections ()
    {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedItems ()
    {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++)
        {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData (int pos)
    {
        officeLists.remove(pos);
        resetCurrentIndex();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount ()
    {
        return selectedItems.size();
    }

    private void resetCurrentIndex ()
    {
        currentSelectedIndex = -1;
    }

    class OffViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener
    {

        TextView officename;
        LinearLayout messageLayout;

        OffViewHolder (View itemView)
        {
            super(itemView);

            setTools(itemView);


            itemView.setOnLongClickListener(this);
        }


        @Override
        public boolean onLongClick (View v)
        {
            listener.onRowLongClicked(getAdapterPosition());
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }


        private void setTools (View view)
        {
            messageLayout = view.findViewById(R.id.request_layout);
            officename = view.findViewById(R.id.officename);
           //  delete = view.findViewById(R.id.btndel);

        }
    }


    public interface MessageAdapterListener
    {
        void onMessageClicked(int position);

        void onIconImportantClicked(int position);

        void onRowLongClicked(int position);

        void onIconClicked(int position);
    }
}
