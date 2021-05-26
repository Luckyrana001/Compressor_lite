package id.zelory.compressor.sample.common;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

import id.zelory.compressor.sample.listeners.IOnFocusListenable;
import id.zelory.compressor.sample.listeners.OnListFragmentInteractionListener;

/* renamed from: id.zelory.compressor.sample.common.BaseFragment */
public abstract class BaseFragment extends Fragment implements OnClickListener, IOnFocusListenable {
    public static ProgressDialog progressDialog;
    public static Typeface typeface;
    public String TAG = "BaseFragment";
    public boolean isAllowBackStackNotify = false;
    public OnListFragmentInteractionListener onListFragmentInteractionListener;
    Menu mMenu;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArguments();
        typeface = getNewJuneRegular();
        setHasOptionsMenu(true);
        setupActionbar();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hideKeyboard();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(true);
        }
        this.mMenu = menu;
        setupActionbar();
    }

    public void showDialog(String title, String msg) {
        Builder alertDialogBuilder = new Builder(getBaseActivity());
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alertDialogBuilder.create().show();
    }

    /* access modifiers changed from: protected */
    public void setupActionbar() {
    }

    public void showToast(String message) {
        ((BaseActivity) getActivity()).showToast(message);
    }

    public void showSnackBar(String message) {
        ((BaseActivity) getActivity()).showSnackBar(message);
    }

    public void hideKeyboard() {
        try {
            if (getActivity().getCurrentFocus() != null) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDetach() {
        super.onDetach();
    }

    public String getTAG() {
        return this.TAG;
    }

    public abstract void setTAG(String str);

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public Typeface getNewJuneRegular() {
        return getBaseActivity().getNewJuneRegular();
    }

    public Typeface getFontAwesome() {
        return getBaseActivity().getFontAwesome();
    }
}
