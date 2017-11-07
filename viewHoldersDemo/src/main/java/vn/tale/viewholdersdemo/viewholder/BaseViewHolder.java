package vn.tale.viewholdersdemo.viewholder;

import android.view.View;
import butterknife.ButterKnife;

class BaseViewHolder {

  void bindView(View view) {
    ButterKnife.bind(this, view);
  }
}
