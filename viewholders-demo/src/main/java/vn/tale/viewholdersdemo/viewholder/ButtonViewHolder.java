package vn.tale.viewholdersdemo.viewholder;

import vn.tale.viewholdersdemo.R2;
import vn.tiki.viewholders.ViewHolder;

@ViewHolder(
    layout = R2.layout.viewholder_controller_buttons,
    onClick = {R2.id.btAdd, R2.id.btRemove, R2.id.btShuffle}
)
public class ButtonViewHolder {

  public static class Model {

  }

  void bind(Model model) {

  }
}
