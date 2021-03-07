package com.exam.timetable.ui.timetableview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.exam.timetable.R;
import com.exam.timetable.model.data.MemoDBInfo;
import com.exam.timetable.ui.MemoView;
import com.exam.timetable.utils.Const;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import static com.exam.timetable.utils.GlobalFuncKt.getToday;
import static com.exam.timetable.utils.GlobalFuncKt.weekCalendar;

public class TimetableView extends LinearLayout {
    private static final int DEFAULT_ROW_COUNT = 12;
    private static final int DEFAULT_COLUMN_COUNT = 6;
    private static final int DEFAULT_CELL_HEIGHT_DP = 50;
    private static final int DEFAULT_DATE_CELL_HEIGHT_DP = 20;
    private static final int DEFAULT_SIDE_CELL_WIDTH_DP = 30;
    private static final int DEFAULT_START_TIME = 9;

    private static final int DEFAULT_SIDE_HEADER_FONT_SIZE_DP = 9;
    private static final int DEFAULT_HEADER_FONT_SIZE_DP = 15;
    private static final int DEFAULT_HEADER_HIGHLIGHT_FONT_SIZE_DP = 15;
    private static final int DEFAULT_STICKER_FONT_SIZE_DP = 10;
    private static final int DEFAULT_STICKER_SUB_FONT_SIZE_DP = 8;
    private static final String[] STICKER_COLOR = {"#f08676", "#ecc369", "#a7ca70", "#7dd1c1", "#7aa5e9", "#fbaa68", "#9f86e1", "#78cb87", "#d397ed"};
    private static final String[] TIME_STRS = {"09:00", "10:00", "11:00", "12:00", "01:00", "02:00", "03:00","04:00" , "05:00", "06:00", "07:00"};

    private int rowCount;
    private int columnCount;
    private int cellHeight;
    private int sideCellWidth;
    private String[] headerTitle;
    private String[] stickerColors;
    private int startTime;
    private int headerHighlightColor;

    private RelativeLayout stickerBox;
    TableLayout tableHeader;
    TableLayout tableHeaderDate;
    TableLayout tableBox;

    private Context context;

    HashMap<Integer, Sticker> stickers = new HashMap<Integer, Sticker>();
    private int stickerCount = -1;

    private OnStickerSelectedListener stickerSelectedListener = null;

    private HighlightMode highlightMode = HighlightMode.COLOR;
    private int headerHighlightImageSize;
    private Drawable headerHighlightImage = null;

    public TimetableView(Context context) {
        super(context, null);
        this.context = context;
    }

    public TimetableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimetableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        getAttrs(attrs);
        init();
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimetableView);
        rowCount = a.getInt(R.styleable.TimetableView_row_count, DEFAULT_ROW_COUNT) - 1;
        columnCount = a.getInt(R.styleable.TimetableView_column_count, DEFAULT_COLUMN_COUNT);
        cellHeight = a.getDimensionPixelSize(R.styleable.TimetableView_cell_height, dp2Px(DEFAULT_CELL_HEIGHT_DP));
        sideCellWidth = a.getDimensionPixelSize(R.styleable.TimetableView_side_cell_width, dp2Px(DEFAULT_SIDE_CELL_WIDTH_DP));
        int titlesId = a.getResourceId(R.styleable.TimetableView_header_title, R.array.default_header_title);
        headerTitle = a.getResources().getStringArray(titlesId);
        int colorsId = a.getResourceId(R.styleable.TimetableView_sticker_colors, R.array.default_sticker_color);
        stickerColors = a.getResources().getStringArray(colorsId);
        startTime = a.getInt(R.styleable.TimetableView_start_time, DEFAULT_START_TIME);
        headerHighlightColor = a.getColor(R.styleable.TimetableView_header_highlight_color, getResources().getColor(R.color.default_header_highlight_color));
        int highlightTypeValue = a.getInteger(R.styleable.TimetableView_header_highlight_type,0);
        if(highlightTypeValue == 0) highlightMode = HighlightMode.COLOR;
        else if(highlightTypeValue == 1) highlightMode = HighlightMode.IMAGE;
        headerHighlightImageSize = a.getDimensionPixelSize(R.styleable.TimetableView_header_highlight_image_size, dp2Px(24));
        headerHighlightImage = a.getDrawable(R.styleable.TimetableView_header_highlight_image);
        a.recycle();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_timetable, this, false);
        addView(view);

        stickerBox = view.findViewById(R.id.sticker_box);
        tableHeader = view.findViewById(R.id.table_header);
        tableHeaderDate = view.findViewById(R.id.table_header_date);
        tableBox = view.findViewById(R.id.table_box);

        createTable();
    }

    public void setOnStickerSelectEventListener(OnStickerSelectedListener listener) {
        stickerSelectedListener = listener;
    }

    /**
     * date : 2019-02-08
     * get all schedules TimetableView has.
     */
    public ArrayList<Schedule> getAllSchedulesInStickers() {
        ArrayList<Schedule> allSchedules = new ArrayList<Schedule>();
        for (int key : stickers.keySet()) {
            for (Schedule schedule : stickers.get(key).getSchedules()) {
                allSchedules.add(schedule);
            }
        }
        return allSchedules;
    }

    /**
     * date : 2019-02-08
     * Used in Edit mode, To check a invalidate schedule.
     */
    public ArrayList<Schedule> getAllSchedulesInStickersExceptIdx(int idx) {
        ArrayList<Schedule> allSchedules = new ArrayList<Schedule>();
        for (int key : stickers.keySet()) {
            if (idx == key) continue;
            for (Schedule schedule : stickers.get(key).getSchedules()) {
                allSchedules.add(schedule);
            }
        }
        return allSchedules;
    }
    public void add(ArrayList<Schedule> schedules) {
        add(schedules, -1);
    }

    private void add(final ArrayList<Schedule> schedules, int specIdx) {
        final int count = specIdx < 0 ? ++stickerCount : specIdx;
        Sticker sticker = new Sticker();
        for (Schedule schedule : schedules) {
            TextView tv = new TextView(context);
            LinearLayout container = new LinearLayout(context);
            container.setOrientation(LinearLayout.VERTICAL);

            container.setLayoutParams(createStickerParam2(schedule));

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            tv.setLayoutParams(param);
            tv.setPadding(10, 0, 10, 0);
            tv.setText(schedule.getClassTitle());
            tv.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_STICKER_FONT_SIZE_DP);
            tv.setTypeface(null, Typeface.BOLD);

            container.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(stickerSelectedListener != null)
                        stickerSelectedListener.OnStickerSelected(schedule.hashCode(), schedules);
                }
            });

            TextView tv2 = new TextView(context);
            tv2.setLayoutParams(param);
            tv2.setPadding(10, 0, 10, 0);
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_STICKER_SUB_FONT_SIZE_DP);
            tv2.setText(schedule.getClassPlace());
            container.addView(tv);
            container.addView(tv2);

            sticker.addTextView(container);

            if (schedule.getMemoDatas() != null && schedule.getMemoDatas().size() > 0) {
                for (MemoDBInfo memo : schedule.getMemoDatas()) {
                    MemoView memoView = new MemoView(context);

                    memoView.setDataListener(1, schedule.getMemoDatas().get(0),
                            new MemoView.SetClickListener() {
                                @Override
                                public void setTrashClickListener(@NotNull MemoDBInfo memo) {

                                }
                                @Override
                                public void setShowMemoClickListener(@NotNull MemoDBInfo memo) {

                                }
                            });
                    container.addView(memoView);
                }
            }

            sticker.addSchedule(schedule);
            stickers.put(schedule.hashCode(), sticker);
            stickerBox.addView(container);
        }
        setStickerColor();
        createTableHeaderDate();
    }


    public String createSaveData() {
        return SaveManager.saveSticker(stickers);
    }

    public void load(String data) {
        removeAll();
        stickers = SaveManager.loadSticker(data);
        int maxKey = 0;
        for (int key : stickers.keySet()) {
            ArrayList<Schedule> schedules = stickers.get(key).getSchedules();
            add(schedules, key);
            if (maxKey < key) maxKey = key;
        }
        stickerCount = maxKey + 1;
        setStickerColor();
    }

    public void removeAll() {
        for (int key : stickers.keySet()) {
            Sticker sticker = stickers.get(key);
            for (View tv : sticker.getView()) {
                stickerBox.removeView(tv);
            }
        }
        stickers.clear();
    }

    public void edit(int idx, ArrayList<Schedule> schedules) {
        remove(idx);
        add(schedules, idx);
    }

    public void remove(int idx) {
        Sticker sticker = stickers.get(idx);
        for (View tv : sticker.getView()) {
            stickerBox.removeView(tv);
        }
        stickers.remove(idx);
        setStickerColor();
    }

    public void setHeaderHighlightColorInit() {
        TableRow row = (TableRow) tableHeader.getChildAt(0);
        for (int i = 0 ; i < row.getChildCount(); i++) {
            View element = row.getChildAt(i);

            element.setBackgroundColor(Color.parseColor("#FFEECC"));
            ((TextView)element).setTextColor(Color.parseColor("#000000"));
            ((TextView)element).setTypeface(null, Typeface.NORMAL);
            ((TextView)element).requestLayout();
        }
        setHeaderDateHighlightColorInit();
    }

    public void setHeaderHighlight(int idx) {
        if(idx < 0)return;
        TableRow row = (TableRow) tableHeader.getChildAt(0);
        View element = row.getChildAt(idx);

        if(highlightMode == HighlightMode.COLOR) {
            TextView tx = (TextView)element;
            tx.setTextColor(Color.parseColor("#FFFFFF"));
            tx.setBackgroundColor(headerHighlightColor);
            tx.setTypeface(null, Typeface.BOLD);
            tx.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEADER_HIGHLIGHT_FONT_SIZE_DP);

            tx.requestLayout();

            setHeaderDateHighlight(idx);
        }
        else if(highlightMode == HighlightMode.IMAGE){
            RelativeLayout outer = new RelativeLayout(context);
            outer.setLayoutParams(createTableRowParam(cellHeight));
            ImageView iv = new ImageView(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(headerHighlightImageSize,headerHighlightImageSize);
            params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            row.removeViewAt(idx);
            outer.addView(iv);
            row.addView(outer,idx);

            if(headerHighlightImage != null) {
                iv.setImageDrawable(headerHighlightImage);
            }

        }
    }

    private void setHeaderDateHighlightColorInit() {
        TableRow row = (TableRow) tableHeaderDate.getChildAt(0);
        for (int i = 0 ; i < row.getChildCount(); i++) {
            View element = row.getChildAt(i);

            element.setBackgroundColor(Color.parseColor("#FFEECC"));
            ((TextView)element).setTextColor(Color.parseColor("#000000"));
            ((TextView)element).setTypeface(null, Typeface.NORMAL);
            ((TextView)element).requestLayout();
        }
    }

      public void setHeaderDateHighlight(int idx) {
          if (idx < 0) return;
          TableRow row = (TableRow) tableHeaderDate.getChildAt(0);
          View element = row.getChildAt(idx);

          if (highlightMode == HighlightMode.COLOR) {
              TextView tx = (TextView) element;
              tx.setTextColor(Color.parseColor("#FFFFFF"));
              tx.setBackgroundColor(headerHighlightColor);
              tx.setTypeface(null, Typeface.BOLD);
              tx.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEADER_HIGHLIGHT_FONT_SIZE_DP);

              tx.requestLayout();
          }
      }
    private void setStickerColor() {
        int size = stickers.size();
        int[] orders = new int[size];
        int i = 0;
        for (int key : stickers.keySet()) {
            orders[i++] = key;
        }
        Arrays.sort(orders);

        int colorSize = stickerColors.length;

        for (i = 0; i < size; i++) {
            for (View v : stickers.get(orders[i]).getView()) {
                int colorNum = new Random().nextInt(STICKER_COLOR.length -1);
                String color = STICKER_COLOR[colorNum];
                v.setBackgroundColor(Color.parseColor(color));
            }
        }

    }

    private void createTable() {
        createTableHeader();
        createTableHeaderDate();
        for (int i = 0; i < rowCount; i++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setLayoutParams(createTableLayoutParam());

            for (int k = 0; k < columnCount; k++) {
                TextView tv = new TextView(context);
                tv.setLayoutParams(createTableRowParam(cellHeight));
                if (k == 0) {
                    tv.setText(TIME_STRS[i]);
                    tv.setTextColor(getResources().getColor(R.color.colorHeaderText));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_SIDE_HEADER_FONT_SIZE_DP);
                    tv.setBackgroundColor(getResources().getColor(R.color.colorHeader));
                    tv.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                    tv.setLayoutParams(createTableRowParam(sideCellWidth, cellHeight));
                } else {
                    tv.setText("");
                    tv.setBackground(getResources().getDrawable(R.drawable.item_border));
                    tv.setGravity(Gravity.RIGHT);
                }
                tableRow.addView(tv);
            }
            tableBox.addView(tableRow);
        }
    }

    private void createTableHeader() {
        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(createTableLayoutParam());

        for (int i = 0; i < columnCount; i++) {
            TextView tv = new TextView(context);
            if (i == 0) {
                tv.setLayoutParams(createTableRowParam(sideCellWidth, cellHeight));
                tv.setVisibility(View.INVISIBLE);
            } else {
                tv.setLayoutParams(createTableRowParam(cellHeight));
            }
            tv.setTextColor(getResources().getColor(R.color.colorHeaderText));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEADER_FONT_SIZE_DP);
            tv.setText(headerTitle[i]);
            tv.setGravity(Gravity.CENTER);

            tableRow.addView(tv);
        }
        tableHeader.addView(tableRow);

    }

    private void createTableHeaderDate() {
        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(createTableLayoutParam());

        tableHeaderDate.removeAllViews();
        int dateCellHeight = dp2Px(DEFAULT_DATE_CELL_HEIGHT_DP);
        for (int i = 0; i < columnCount; i++) {
            TextView tv = new TextView(context);
            if (i == 0) {
                tv.setLayoutParams(createTableRowParam(sideCellWidth, dateCellHeight));
            } else {
                tv.setLayoutParams(createTableRowParam(dateCellHeight));
            }
            tv.setTextColor(getResources().getColor(R.color.colorHeaderText));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEADER_FONT_SIZE_DP);
            tv.setGravity(Gravity.CENTER);
            if (i == 0)
                tv.setVisibility(View.INVISIBLE);
            tableRow.addView(tv);


        }
        tableHeaderDate.addView(tableRow);
        setWeekDay(Const.TODAY);
    }

    public void setWeekDay(String day) {
        String[] week = weekCalendar(day);
        TableRow row = (TableRow)tableHeaderDate.getChildAt(0);
        setHeaderHighlightColorInit();

        for (int i = 0 ; i < row.getChildCount(); i++) {
            ((TextView)row.getChildAt(i)).setText(week[i].substring(6,8));
            if (week[i].equals(Const.TODAY))
                setHeaderHighlight(i);
        }
        tableHeaderDate.requestLayout();
    }

    private LinearLayout.LayoutParams createStickerParam2(Schedule schedule) {
        int cell_w = calCellWidth();

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(cell_w, calStickerHeightPx(schedule));
        param.setMargins(sideCellWidth + cell_w * schedule.getDay(), calStickerTopPxByTime(schedule.getStartTime()), 0, 0);

        return param;
    }

    private RelativeLayout.LayoutParams createStickerParam(Schedule schedule) {
        int cell_w = calCellWidth();

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(cell_w, calStickerHeightPx(schedule));
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        param.setMargins(sideCellWidth + cell_w * schedule.getDay(), calStickerTopPxByTime(schedule.getStartTime()), 0, 0);

        return param;
    }


    private int calCellWidth(){
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int cell_w = (size.x-getPaddingLeft() - getPaddingRight()- sideCellWidth) / (columnCount - 1);
        return cell_w;
    }

    private int calStickerHeightPx(Schedule schedule) {
        int startTopPx = calStickerTopPxByTime(schedule.getStartTime());
        int endTopPx = calStickerTopPxByTime(schedule.getEndTime());
        int d = endTopPx - startTopPx;

        return d;
    }

    private int calStickerTopPxByTime(Time time) {
        int topPx = (time.getHour() - startTime) * cellHeight + (int) ((time.getMinute() / 60.0f) * cellHeight);
        return topPx;
    }

    private TableLayout.LayoutParams createTableLayoutParam() {
        return new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
    }

    private TableRow.LayoutParams createTableRowParam(int h_px) {
        return new TableRow.LayoutParams(calCellWidth(), h_px);
    }

    private TableRow.LayoutParams createTableRowParam(int w_px, int h_px) {
        return new TableRow.LayoutParams(w_px, h_px);
    }

    private String getHeaderTime(int i) {
        int p = (startTime + i) % 24;
        int res = p <= 12 ? p : p - 12;
        return res + "";
    }

    static private int dp2Px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void onCreateByBuilder(Builder builder) {
        this.rowCount = builder.rowCount;
        this.columnCount = builder.columnCount;
        this.cellHeight = builder.cellHeight;
        this.sideCellWidth = builder.sideCellWidth;
        this.headerTitle = builder.headerTitle;
        this.stickerColors = builder.stickerColors;
        this.startTime = builder.startTime;
        this.headerHighlightColor = builder.headerHighlightColor;

        init();
    }


    public interface OnStickerSelectedListener {
        void OnStickerSelected(int idx, ArrayList<Schedule> schedules);
    }

    static class Builder {
        private Context context;
        private int rowCount;
        private int columnCount;
        private int cellHeight;
        private int sideCellWidth;
        private String[] headerTitle;
        private String[] stickerColors;
        private int startTime;
        private int headerHighlightColor;

        public Builder(Context context) {
            this.context = context;
            rowCount = DEFAULT_ROW_COUNT;
            columnCount = DEFAULT_COLUMN_COUNT;
            cellHeight = dp2Px(DEFAULT_CELL_HEIGHT_DP);
            sideCellWidth = dp2Px(DEFAULT_SIDE_CELL_WIDTH_DP);
            headerTitle = context.getResources().getStringArray(R.array.default_header_title);
            stickerColors = context.getResources().getStringArray(R.array.default_sticker_color);
            startTime = DEFAULT_START_TIME;
            headerHighlightColor = context.getResources().getColor(R.color.default_header_highlight_color);
        }

        public Builder setRowCount(int n) {
            this.rowCount = n;
            return this;
        }

        public Builder setColumnCount(int n) {
            this.columnCount = n;
            return this;
        }

        public Builder setCellHeight(int dp) {
            this.cellHeight = dp2Px(dp);
            return this;
        }

        public Builder setSideCellWidth(int dp) {
            this.sideCellWidth = dp2Px(dp);
            return this;
        }

        public Builder setHeaderTitle(String[] titles) {
            this.headerTitle = titles;
            return this;
        }

        public Builder setStickerColors(String[] colors) {
            this.stickerColors = colors;
            return this;
        }

        public Builder setStartTime(int t) {
            this.startTime = t;
            return this;
        }

        public Builder setHeaderHighlightColor(int c) {
            this.headerHighlightColor = c;
            return this;
        }

        public TimetableView build() {
            TimetableView timetableView = new TimetableView(context);
            timetableView.onCreateByBuilder(this);
            return timetableView;
        }
    }
}
