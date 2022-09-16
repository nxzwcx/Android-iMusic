// Generated by view binder compiler. Do not edit!
package cn.edu.seu.cose.imusic.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import cn.edu.seu.cose.imusic.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentMvBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button button;

  @NonNull
  public final RadioGroup groupMv;

  @NonNull
  public final LinearLayout linearLayout;

  @NonNull
  public final LinearLayout linearLayout3;

  @NonNull
  public final RadioButton radioButton1;

  @NonNull
  public final RadioButton radioButton2;

  @NonNull
  public final RadioButton radioButton3;

  @NonNull
  public final RadioButton radioButton4;

  @NonNull
  public final TextView textMv;

  @NonNull
  public final TextView textView3;

  @NonNull
  public final WebView webviewMv;

  private FragmentMvBinding(@NonNull ConstraintLayout rootView, @NonNull Button button,
      @NonNull RadioGroup groupMv, @NonNull LinearLayout linearLayout,
      @NonNull LinearLayout linearLayout3, @NonNull RadioButton radioButton1,
      @NonNull RadioButton radioButton2, @NonNull RadioButton radioButton3,
      @NonNull RadioButton radioButton4, @NonNull TextView textMv, @NonNull TextView textView3,
      @NonNull WebView webviewMv) {
    this.rootView = rootView;
    this.button = button;
    this.groupMv = groupMv;
    this.linearLayout = linearLayout;
    this.linearLayout3 = linearLayout3;
    this.radioButton1 = radioButton1;
    this.radioButton2 = radioButton2;
    this.radioButton3 = radioButton3;
    this.radioButton4 = radioButton4;
    this.textMv = textMv;
    this.textView3 = textView3;
    this.webviewMv = webviewMv;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentMvBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentMvBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_mv, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentMvBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.button;
      Button button = ViewBindings.findChildViewById(rootView, id);
      if (button == null) {
        break missingId;
      }

      id = R.id.group_mv;
      RadioGroup groupMv = ViewBindings.findChildViewById(rootView, id);
      if (groupMv == null) {
        break missingId;
      }

      id = R.id.linearLayout;
      LinearLayout linearLayout = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout == null) {
        break missingId;
      }

      id = R.id.linearLayout3;
      LinearLayout linearLayout3 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout3 == null) {
        break missingId;
      }

      id = R.id.radioButton1;
      RadioButton radioButton1 = ViewBindings.findChildViewById(rootView, id);
      if (radioButton1 == null) {
        break missingId;
      }

      id = R.id.radioButton2;
      RadioButton radioButton2 = ViewBindings.findChildViewById(rootView, id);
      if (radioButton2 == null) {
        break missingId;
      }

      id = R.id.radioButton3;
      RadioButton radioButton3 = ViewBindings.findChildViewById(rootView, id);
      if (radioButton3 == null) {
        break missingId;
      }

      id = R.id.radioButton4;
      RadioButton radioButton4 = ViewBindings.findChildViewById(rootView, id);
      if (radioButton4 == null) {
        break missingId;
      }

      id = R.id.text_mv;
      TextView textMv = ViewBindings.findChildViewById(rootView, id);
      if (textMv == null) {
        break missingId;
      }

      id = R.id.textView3;
      TextView textView3 = ViewBindings.findChildViewById(rootView, id);
      if (textView3 == null) {
        break missingId;
      }

      id = R.id.webview_mv;
      WebView webviewMv = ViewBindings.findChildViewById(rootView, id);
      if (webviewMv == null) {
        break missingId;
      }

      return new FragmentMvBinding((ConstraintLayout) rootView, button, groupMv, linearLayout,
          linearLayout3, radioButton1, radioButton2, radioButton3, radioButton4, textMv, textView3,
          webviewMv);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
