package iso.my.com.inspectionstudentorganization.OfficeDet;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import iso.my.com.inspectionstudentorganization.Models.OfficesDetail;
import iso.my.com.inspectionstudentorganization.R;

public class OffDetailAdapter extends RecyclerView.Adapter<OffDetailAdapter.OffViewHolder>
{

    private MessageAdapterListener listener;
    private Activity activity;
    private Context context;


    List<OfficesDetail> officesDetails;
    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;
    private int currentSelectedIndex = -1;


    public OffDetailAdapter(Context context, List<OfficesDetail> off, MessageAdapterListener listener)
    {

        this.context = context;

        this.officesDetails = off;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
        this.listener = listener;
    }

    @Override
    public OffViewHolder onCreateViewHolder (ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_off_list_item, parent, false);
        return new OffViewHolder(view);

    }

    @Override
    public void onBindViewHolder (OffViewHolder holder, int position)
    {
        OfficesDetail officedet = officesDetails.get(position);

        holder.tv_officename.setText(officedet.getOfficename());
        holder.tv_officetype.setText(officedet.getOfficetype());
        holder.tv_insurance.setText(officedet.getInsurance());
        holder.tv_economic.setText(officedet.getEconomic());
        holder.tv_phone.setText(officedet.getPhone());
        holder.tv_address.setText(officedet.getAddress());
        holder.tv_manager.setText(officedet.getName()+" "+officedet.getLastname());
        holder.tv_sub.setText(officedet.getSubno());
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

    private void applyClickEvents (OffViewHolder holder, final int position)
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

    class OffViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener
    {

        TextView  tv_officename, tv_insurance,tv_economic,tv_phone,tv_officetype,tv_address,tv_manager,tv_sub;

        LinearLayout Layout;
CheckBox chtype,chins,checo,chaddress,chphone;

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
            Layout = view.findViewById(R.id.det_layout);
            tv_officename = view.findViewById(R.id.officename);
            tv_officetype = view.findViewById(R.id.officetype);
            tv_insurance = view.findViewById(R.id.insurance);
            tv_economic = view.findViewById(R.id.economic);
            tv_phone = view.findViewById(R.id.phone);
            tv_address = view.findViewById(R.id.address);

            chtype = view.findViewById(R.id.checktype);
            chaddress = view.findViewById(R.id.checkaddress);
            checo = view.findViewById(R.id.checkeco);
            chins = view.findViewById(R.id.checkins);
            chphone = view.findViewById(R.id.checkphone);

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
