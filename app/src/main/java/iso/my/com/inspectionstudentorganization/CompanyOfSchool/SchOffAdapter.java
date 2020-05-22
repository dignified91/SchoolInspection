package iso.my.com.inspectionstudentorganization.CompanyOfSchool;

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

import iso.my.com.inspectionstudentorganization.Models.OfficeDetSchool;
import iso.my.com.inspectionstudentorganization.R;


public class SchOffAdapter extends RecyclerView.Adapter<SchOffAdapter.SchOffViewHolder>
{

    private MessageAdapterListenerr listener;
    private Activity activity;
    private Context context;


    List<OfficeDetSchool> officesDetails;
    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;
    private int currentSelectedIndex = -1;


    public SchOffAdapter(List<OfficeDetSchool> off, MessageAdapterListenerr listener)
    {
        this.officesDetails = off;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
        this.listener = listener;
    }

    @Override
    public SchOffViewHolder onCreateViewHolder (ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offsch_list_item, parent, false);
        return new SchOffViewHolder(view);

    }

    @Override
    public void onBindViewHolder (SchOffViewHolder holder, int position)
    {
        OfficeDetSchool officedet = officesDetails.get(position);

        holder.tv_office.setText(officedet.getName());

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

    private void applyClickEvents (SchOffViewHolder holder, final int position)
    {
        holder.Layout.setOnClickListener(v -> listener.onMessageClicked(position));
    }


    @Override
    public int getItemCount ()
    {
        return officesDetails.size();
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
        officesDetails.remove(pos);
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

    class SchOffViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener
    {

        TextView  tv_office;

        LinearLayout Layout;

        SchOffViewHolder (View itemView)
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
            tv_office = view.findViewById(R.id.officename);


        }
    }

    public interface MessageAdapterListenerr
    {
        void onMessageClicked(int position);

        void onIconImportantClicked(int position);

        void onRowLongClicked(int position);

        void onIconClicked(int position);
    }
}
