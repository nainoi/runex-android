package com.think.runex.java.Customize.Views.Toolbar;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.think.runex.R;
import com.think.runex.java.Utils.L;

public class xToolbar implements View.OnClickListener {
    /**
     * Main variables
     */
    private final String ct = "xToolbar->";

    // views
    private View toolbar;
    private ImageView toolbarOptionButton;
    private ImageView toolbarNavButton;
    private ImageView toolbarTitleIcon;
    private TextView toolbarTitleLabel;

    /**
     * Implement methods
     */
    @Override
    public void onClick(View view) {

    }

    public xToolbar(View toolbar) {
        this.toolbar = toolbar;

        toolbarOptionButton = toolbar.findViewById(R.id.toolbar_options_button);
        toolbarNavButton = toolbar.findViewById(R.id.toolbar_navigation_button);
        toolbarTitleIcon = toolbar.findViewById(R.id.toolbar_title_icon);
        toolbarTitleLabel = toolbar.findViewById(R.id.toolbar_title_label);

        toolbarOptionButton.setOnClickListener(this);
        toolbarNavButton.setOnClickListener(this);

    }

    public void setImageNavIcon(int resourceId) {
        toolbarNavButton.setImageResource(resourceId);

    }

    public void setImageOptionIcon(int resourceId) {
        toolbarOptionButton.setImageResource(resourceId);
    }

    public void bind(xToolbarProps props) {
        // prepare usage variables
        final String mtn = ct + "bind() ";

        try {
            toolbarTitleLabel.setText(props.titleLabel);
            if (props.titleImageUrl != null && !props.titleImageUrl.isEmpty()) {
                // display icon
                toolbarTitleIcon.setVisibility(View.VISIBLE);

                // display image
                Picasso.get().load(props.titleImageUrl)
                        .into(toolbarTitleIcon);

            }

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());
        }
    }
}
