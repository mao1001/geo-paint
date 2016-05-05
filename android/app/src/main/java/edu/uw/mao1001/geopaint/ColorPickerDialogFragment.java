package edu.uw.mao1001.geopaint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.thebluealliance.spectrum.SpectrumDialog;

/**
 * Created by Nick on 5/4/2016.
 */
public class ColorPickerDialogFragment extends DialogFragment {

    private static final String TAG = "ColorPickerDialogFragment";
//    private static final SpectrumDialog.OnColorSelectedListener colorListener;

    //-----------------------------//
    //   C O N S T R U C T O R S   //
    //-----------------------------//

    /**
     * Required blank constructor
     */
    public ColorPickerDialogFragment() {}

    public static ColorPickerDialogFragment newInstance(SpectrumDialog.OnColorSelectedListener colorListener) {
        ColorPickerDialogFragment fragment = new ColorPickerDialogFragment();
//        this.colorListener = colorListener;
        return fragment;
    }

    //-----------------------------------------//
    //   F R A G M E N T   O V E R R I D E S   //
    //-----------------------------------------//


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SpectrumDialog.Builder builder = new SpectrumDialog.Builder(getActivity());

        //builder.setOnColorSelectedListener(colorListener);

        return null;
    }
}
