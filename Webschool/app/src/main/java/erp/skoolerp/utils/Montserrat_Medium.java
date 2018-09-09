package erp.skoolerp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Technosoft on 5/1/2017.
 */

public class Montserrat_Medium extends android.support.v7.widget.AppCompatTextView
{
    public Montserrat_Medium(Context context)
    {
        super(context);
        applyCustomFont(context);
    }

    public Montserrat_Medium(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        applyCustomFont(context);
    }

    public Montserrat_Medium(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context)
    {
        Typeface customFont = FontCache.getTypeface("fonts/Montserrat_Medium.ttf", context);
        setTypeface(customFont);
    }

}
