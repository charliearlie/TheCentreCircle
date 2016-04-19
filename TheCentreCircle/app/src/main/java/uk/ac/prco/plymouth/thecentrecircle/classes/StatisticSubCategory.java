package uk.ac.prco.plymouth.thecentrecircle.classes;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.expanx.SmartItem;
import com.marshalchen.ultimaterecyclerview.expanx.Util.easyTemplateChild;

/**
 * Created by charliewaite on 19/04/2016.
 */
public class StatisticSubCategory extends easyTemplateChild<SmartItem, TextView, RelativeLayout> {
    public StatisticSubCategory(View itemView) {
        super(itemView);
    }
}
