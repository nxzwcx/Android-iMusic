// Generated by view binder compiler. Do not edit!
package cn.edu.seu.cose.imusic.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import cn.edu.seu.cose.imusic.R;
import cn.edu.seu.cose.imusic.lrcview.LrcView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ViewPlayLrcBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final LinearLayout ll1;

  @NonNull
  public final LrcView lrcView;

  @NonNull
  public final TextView textLrcUrl;

  private ViewPlayLrcBinding(@NonNull ConstraintLayout rootView, @NonNull LinearLayout ll1,
      @NonNull LrcView lrcView, @NonNull TextView textLrcUrl) {
    this.rootView = rootView;
    this.ll1 = ll1;
    this.lrcView = lrcView;
    this.textLrcUrl = textLrcUrl;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ViewPlayLrcBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ViewPlayLrcBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.view_play_lrc, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ViewPlayLrcBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ll1;
      LinearLayout ll1 = ViewBindings.findChildViewById(rootView, id);
      if (ll1 == null) {
        break missingId;
      }

      id = R.id.lrc_view;
      LrcView lrcView = ViewBindings.findChildViewById(rootView, id);
      if (lrcView == null) {
        break missingId;
      }

      id = R.id.text_lrcUrl;
      TextView textLrcUrl = ViewBindings.findChildViewById(rootView, id);
      if (textLrcUrl == null) {
        break missingId;
      }

      return new ViewPlayLrcBinding((ConstraintLayout) rootView, ll1, lrcView, textLrcUrl);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
