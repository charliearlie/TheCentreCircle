package uk.ac.prco.plymouth.thecentrecircle.classes;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.expanx.SmartItem;
import com.marshalchen.ultimaterecyclerview.expanx.Util.easyTemplateParent;

/**
 * Created by charliewaite on 19/04/2016.
 */
public class StatisticCategory extends easyTemplateParent<SmartItem, RelativeLayout, TextView> {
    public StatisticCategory(View itemView) {
        super(itemView);
    }
}