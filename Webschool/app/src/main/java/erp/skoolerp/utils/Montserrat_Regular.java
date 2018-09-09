package erp.skoolerp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Technosoft on 5/1/2017.
 */

public class Montserrat_Regular extends android.support.v7.widget.AppCompatTextView
{
    public Montserrat_Regular(Context context)
    {
        super(context);
        applyCustomFont(context);
    }

    public Montserrat_Regular(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        applyCustomFont(context);
    }

    public Montserrat_Regular(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context)
    {
        Typeface customFont = FontCache.getTypeface("fonts/Montserrat_Regular.ttf", context);
        setTypeface(customFont);
    }
}

