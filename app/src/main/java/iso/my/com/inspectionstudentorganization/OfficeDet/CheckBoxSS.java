package iso.my.com.inspectionstudentorganization.OfficeDet;

import android.widget.CheckBox;

/**
 * CheckBoxSS => CheckBoxStatusStudent
 */
public class CheckBoxSS {


    public final CheckBox chkBoxtype;
    public final CheckBox chkBoxeco;
    public final CheckBox chkBoxins;
    public final CheckBox chkBoxphone;
    public final CheckBox chkBoxaddress;

    public CheckBoxSS(
                      CheckBox ChkBoxType,
                      CheckBox ChkBoxEco,
                      CheckBox ChkBoxIns,
                      CheckBox ChkBoxPhone,
                      CheckBox ChkBoxAddress) {


        this.chkBoxtype = ChkBoxType;
        this.chkBoxeco = ChkBoxEco;
        this.chkBoxins = ChkBoxIns;
        this.chkBoxphone = ChkBoxPhone;
        this.chkBoxaddress = ChkBoxAddress;

    }

    public boolean isChkBoxType ()
    {
        return (chkBoxtype.isChecked());
    }

    public boolean isChkBoxEco ()
    {
        return (chkBoxeco.isChecked());
    }

    public boolean isChkBoxIns()
    {
        return (chkBoxins.isChecked());
    }

    public boolean isChkBoxPhone()
    {
        return (chkBoxphone.isChecked());
    }

    public boolean isChkBoxAddress()
    {
        return (chkBoxaddress.isChecked());
    }

}
