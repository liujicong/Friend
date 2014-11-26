package cn.yanshang.friend.borrow;

import java.util.ArrayList;
import java.util.List;

import cn.yanshang.friend.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentReasonScroll extends Fragment {

	private GridView gridView;
	// private List<Item> itemList;
	private List<String> itemList;
	private MyListAdapter adapter;
	private ItemClickEvent listener;
	private MyHandler handler;

	private float density;
	private Activity mActivity;

	private OnClickIconItemAddedListener onNewItemAddedListener;

	public interface OnClickIconItemAddedListener {
		public void onNewItemAdded(String newItem);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.borrow_reason_scroll, container,
				false);

		gridView = (GridView) view.findViewById(R.id.gridView);
		handler = new MyHandler();
		new Thread() {
			public void run() {
				itemList = new ArrayList<String>();
				for (int i = 0; i < 10; i++) {
					// Item item = new Item("北极熊生存如履薄冰" + i,"如果我们从现在开始就采取措施降低气温"
					// + i);
					itemList.add("ba_" + i);
				}
				Message msg = Message.obtain();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}.start();

		return view;
	}

	private class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if ((itemList == null) || (itemList.size() == 0)) {
					return;
				} else {

					DisplayMetrics outMetrics = new DisplayMetrics();
					mActivity.getWindowManager().getDefaultDisplay()
							.getMetrics(outMetrics);
					density = outMetrics.density; // 像素密度

					// ViewGroup.LayoutParams params =
					// gridView.getLayoutParams();
					int itemWidth = (int) (80 * density);
					int spacingWidth = (int) (10 * density);

					LayoutParams params1 = (LayoutParams) gridView
							.getLayoutParams();
					// new LayoutParams(itemList.size()*(200+6),
					// LayoutParams.WRAP_CONTENT);

					params1.width = itemWidth * itemList.size()
							+ (itemList.size() - 1) * spacingWidth;

					gridView.setLayoutParams(params1);
					gridView.setColumnWidth(itemWidth);
					gridView.setHorizontalSpacing(spacingWidth);
					gridView.setStretchMode(GridView.NO_STRETCH);
					gridView.setNumColumns(itemList.size());

					adapter = new MyListAdapter(mActivity);
					listener = new ItemClickEvent();
					gridView.setAdapter(adapter);
					gridView.setOnItemClickListener(listener);
				}
				break;
			}
		}
	}

	private class MyListAdapter extends BaseAdapter {
		private final String TAG = "MyListAdapter";
		private LayoutInflater mInflater;
		private final Context context;

		public MyListAdapter(Context context) {
			this.context = context;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return itemList.size();
		}

		public Object getItem(int position) {
			return itemList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			String item = itemList.get(position);
			CellHolder cellHolder;
			if (convertView == null) {
				cellHolder = new CellHolder();
				convertView = mInflater.inflate(R.layout.item, null);
				cellHolder.ivIcon = (ImageView) convertView
						.findViewById(R.id.ivIcon);
				cellHolder.tvTitle = (TextView) convertView
						.findViewById(R.id.tvTitle);
				// cellHolder.tvContent = (TextView)
				// convertView.findViewById(R.id.tvContent);
				convertView.setTag(cellHolder);
			} else {
				cellHolder = (CellHolder) convertView.getTag();
			}
			cellHolder.ivIcon.setImageResource(R.drawable.travel_scroll_n);// R.drawable.bear
			cellHolder.tvTitle.setText(item + "ww");// item.getTitle()
			// cellHolder.tvContent.setText(item+"gg");//item.getContent()
			return convertView;
		}
	}

	private class CellHolder {
		ImageView ivIcon;
		TextView tvTitle;
		// TextView tvContent;
	}

	private class ItemClickEvent implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

			Toast.makeText(mActivity, "id=" + id + "_position=" + position,
					Toast.LENGTH_SHORT).show();

		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mActivity = activity;
			onNewItemAddedListener = (OnClickIconItemAddedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnClickIconItemAddedListener");
		}
	}

}
