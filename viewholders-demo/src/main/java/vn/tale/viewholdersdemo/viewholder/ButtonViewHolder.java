package vn.tale.viewholdersdemo.viewholder;

import vn.tale.viewholdersdemo.R2;
import vn.tale.viewholdersdemo.viewholder.ButtonViewHolder.Model;
import vn.tiki.viewholders.ViewHolder;

@ViewHolder(
    layout = R2.layout.viewholder_controller_buttons,
    onClick = {R2.id.btAdd, R2.id.btRemove, R2.id.btShuffle},
    bindTo = Model.class
)
public class ButtonViewHolder {

  public static class Model {

  }
}
