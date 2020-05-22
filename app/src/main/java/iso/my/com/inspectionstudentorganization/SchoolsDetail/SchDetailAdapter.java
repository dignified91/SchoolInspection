package iso.my.com.inspectionstudentorganization.SchoolsDetail;

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

import iso.my.com.inspectionstudentorganization.Models.SchoolsDetail;
import iso.my.com.inspectionstudentorganization.R;


public class SchDetailAdapter extends RecyclerView.Adapter<SchDetailAdapter.SchViewHolder>
{

    private MessageAdapterListener listener;
    private Activity activity;
    private Context context;


    List<SchoolsDetail> schoolsDetails;
    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;
    private int currentSelectedIndex = -1;


    public SchDetailAdapter(Context context, List<SchoolsDetail> his, MessageAdapterListener listener)
    {

        this.context = context;

        this.schoolsDetails = his;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
        this.listener = listener;
    }

    @Override
    public SchViewHolder onCreateViewHolder (ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_sch_list_item, parent, false);
        return new SchViewHolder(view);

    }

    @Override
    public void onBindViewHolder (SchViewHolder holder, int position)
    {
        SchoolsDetail history = schoolsDetails.get(position);

        holder.name.setText(history.getSchoolname());
        holder.region.setText(history.getRegion());

        holder.itemView.setActivated(selectedItems.get(position, false));

        applyClickEvents(holder, position);

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

    private void applyClickEvents (SchViewHolder holder, final int position)
    {
        holder.Layout.setOnClickListener(v -> listener.onMessageClicked(position));
    }


    @Override
    public int getItemCount ()
    {
        return schoolsDetails.size();
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
        schoolsDetails.remove(pos);
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

    class SchViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener
    {

        TextView name, region;

        LinearLayout Layout;

        SchViewHolder (View itemView)
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
            Layout = view.findViewById(R.id.det_layout);
            name = view.findViewById(R.id.schoolname);
            region = view.findViewById(R.id.region);
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
