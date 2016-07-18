package me.curzbin.library;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BottomDialog {

    public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;

    public static final int VERTICAL = OrientationHelper.VERTICAL;

    private CustomDialog customDialog;

    public BottomDialog(Context context) {
        customDialog = new CustomDialog(context);
    }

    public BottomDialog title(String title) {
        customDialog.title(title);
        return this;
    }

    public BottomDialog title(int title) {
        customDialog.title(title);
        return this;
    }

    public BottomDialog background(int res) {
        customDialog.background(res);
        return this;
    }

    public BottomDialog inflateMenu(int menu) {
        customDialog.inflateMenu(menu);
        return this;
    }

    public BottomDialog orientation(int orientation) {
        customDialog.orientation(orientation);
        return this;
    }

    public BottomDialog addItems(List<Item> items) {
        customDialog.addItems(items);
        return this;
    }

    public BottomDialog itemClick(RxBus rxBus) {
        customDialog.setItemClick(rxBus);
        return this;
    }

    public BottomDialog itemClick(OnItemClickListener listener) {
        customDialog.setItemClick(listener);
        return this;
    }

    public void show() {
        customDialog.show();
    }

    private final class CustomDialog extends Dialog {
        private LinearLayout background;
        private LinearLayout container;
        private TextView titleView;

        private DialogAdapter adapter;

        private int padding;
        private int topPadding;
        private int leftPadding;
        private int topIcon;
        private int leftIcon;

        private int orientation;

        public CustomDialog(Context context) {
            super(context, R.style.BottomDialog);

            init();
        }

        private void init() {
            padding = getContext().getResources().getDimensionPixelSize(R.dimen.app_normal_margin);
            topPadding = getContext().getResources().getDimensionPixelSize(R.dimen.app_tiny_margin);
            leftPadding = getContext().getResources().getDimensionPixelSize(R.dimen.app_normal_margin);
            topIcon = getContext().getResources().getDimensionPixelSize(R.dimen.bottom_dialog_top_icon);
            leftIcon = getContext().getResources().getDimensionPixelSize(R.dimen.bottom_dialog_left_icon);

            setContentView(R.layout.bottom_dialog);
            setCancelable(true);
            setCanceledOnTouchOutside(true);
            getWindow().setGravity(Gravity.BOTTOM);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            background = (LinearLayout) findViewById(R.id.background);
            titleView = (TextView) findViewById(R.id.title);
            container = (LinearLayout) findViewById(R.id.container);
            findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }

        public void addItems(List<Item> items) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), orientation, false);
            adapter = new DialogAdapter(items, orientation);

            RecyclerView recyclerView = new RecyclerView(getContext());
            recyclerView.setLayoutParams(params);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);

            container.addView(recyclerView);
        }

        public void title(int title) {
            title(getContext().getString(title));
        }

        public void title(String title) {
            titleView.setText(title);
            titleView.setVisibility(View.VISIBLE);
        }

        public void orientation(int orientation) {
            this.orientation = orientation;
            if (adapter != null) adapter.setOrientation(orientation);
        }

        public void background(int res) {
            background.setBackgroundResource(res);
        }

        public void inflateMenu(int menu) {
            MenuInflater menuInflater = new SupportMenuInflater(getContext());
            MenuBuilder menuBuilder = new MenuBuilder(getContext());
            menuInflater.inflate(menu, menuBuilder);
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < menuBuilder.size(); i++) {
                MenuItem menuItem = menuBuilder.getItem(i);
                items.add(new Item(menuItem.getItemId(), menuItem.getTitle().toString(), menuItem.getIcon()));
            }
            addItems(items);
        }

        public void setItemClick(RxBus rxBus) {
            adapter.setItemClick(rxBus);
        }

        public void setItemClick(OnItemClickListener onItemClickListener) {
            adapter.setItemClick(onItemClickListener);
        }

        /**
         * recycler view adapter, provide HORIZONTAL and VERTICAL item style
         */
        private class DialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

            private List<Item> mItems = Collections.emptyList();
            private RxBus rxBus;
            private OnItemClickListener itemClickListener;

            private int orientation;

            public DialogAdapter(List<Item> mItems, int orientation) {
                setList(mItems);
                this.orientation = orientation;
            }

            private void setList(List<Item> items) {
                mItems = items == null ? new ArrayList<Item>() : items;
            }

            public void setItemClick(RxBus rxBus) {
                this.rxBus = rxBus;
            }

            public void setItemClick(OnItemClickListener onItemClickListener) {
                this.itemClickListener = onItemClickListener;
            }

            public void setOrientation(int orientation) {
                this.orientation = orientation;
                notifyDataSetChanged();
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if (orientation == HORIZONTAL)
                    return new HorizontalHolder(new LinearLayout(parent.getContext()));
                else return new VerticalHolder(new LinearLayout(parent.getContext()));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                final Item item = mItems.get(position);

                HorizontalHolder horizontalHolder;
                VerticalHolder verticalHolder;

                if (orientation == HORIZONTAL) {
                    horizontalHolder = (HorizontalHolder) holder;

                    horizontalHolder.item.setText(item.getTitle());
                    horizontalHolder.item.setCompoundDrawablesWithIntrinsicBounds(null, horizontalHolder.icon(item.getIcon()), null, null);
                    horizontalHolder.item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (rxBus != null && rxBus.hasObservers()) rxBus.send(item);
                            if (itemClickListener != null) itemClickListener.click(item);
                        }
                    });
                } else {
                    verticalHolder = (VerticalHolder) holder;

                    verticalHolder.item.setText(item.getTitle());
                    verticalHolder.item.setCompoundDrawablesWithIntrinsicBounds(verticalHolder.icon(item.getIcon()), null, null, null);
                    verticalHolder.item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (rxBus != null && rxBus.hasObservers()) rxBus.send(item);
                            if (itemClickListener != null) itemClickListener.click(item);
                        }
                    });
                }
            }

            @Override
            public int getItemCount() {
                return mItems.size();
            }

            /**
             * horizontal item adapter
             */
            public class HorizontalHolder extends RecyclerView.ViewHolder {
                private TextView item;

                public HorizontalHolder(View view) {
                    super(view);

                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.width = Utils.getScreenWidth(getContext()) / 5;

                    item = new TextView(view.getContext());
                    item.setLayoutParams(params);
                    item.setMaxLines(1);
                    item.setEllipsize(TextUtils.TruncateAt.END);
                    item.setGravity(Gravity.CENTER);
                    item.setTextColor(ContextCompat.getColor(view.getContext(), R.color.gray_font_dark));
                    item.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.font_small));
                    item.setCompoundDrawablePadding(topPadding);
                    item.setPadding(0, padding, 0, padding);

                    TypedValue typedValue = new TypedValue();
                    view.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
                    item.setBackgroundResource(typedValue.resourceId);

                    ((LinearLayout) view).addView(item);
                }

                private Drawable icon(Drawable drawable) {
                    if (drawable != null) {
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        @SuppressWarnings("SuspiciousNameCombination") Drawable resizeIcon = new BitmapDrawable(getContext().getResources(), Bitmap.createScaledBitmap(bitmap, topIcon, topIcon, true));
                        Drawable.ConstantState state = resizeIcon.getConstantState();
                        resizeIcon = DrawableCompat.wrap(state == null ? resizeIcon : state.newDrawable().mutate());
                        return resizeIcon;
                    }
                    return null;
                }
            }

            public class VerticalHolder extends RecyclerView.ViewHolder {
                private TextView item;

                public VerticalHolder(View view) {
                    super(view);

                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    view.setLayoutParams(params);
                    item = new TextView(view.getContext());
                    item.setLayoutParams(params);
                    item.setMaxLines(1);
                    item.setEllipsize(TextUtils.TruncateAt.END);
                    item.setGravity(Gravity.CENTER_VERTICAL);
                    item.setTextColor(ContextCompat.getColor(view.getContext(), R.color.black));
                    item.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.font_normal));
                    item.setCompoundDrawablePadding(leftPadding);
                    item.setPadding(padding, padding, padding, padding);

                    TypedValue typedValue = new TypedValue();
                    view.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
                    item.setBackgroundResource(typedValue.resourceId);

                    ((LinearLayout) view).addView(item);
                }

                private Drawable icon(Drawable drawable) {
                    if (drawable != null) {
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        @SuppressWarnings("SuspiciousNameCombination") Drawable resizeIcon = new BitmapDrawable(getContext().getResources(), Bitmap.createScaledBitmap(bitmap, leftIcon, leftIcon, true));
                        Drawable.ConstantState state = resizeIcon.getConstantState();
                        resizeIcon = DrawableCompat.wrap(state == null ? resizeIcon : state.newDrawable().mutate());
                        return resizeIcon;
                    }
                    return null;
                }
            }
        }
    }
}
