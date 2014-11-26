package cn.yanshang.friend.utils;

import cn.yanshang.friend.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProgressUtil {

	public static AlertDialog.Builder showDialog(Context context, String title,
			String message) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(message);

		builder.setTitle(title);
		builder.setPositiveButton(context.getString(R.string.dialog_btn_ok),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		// builder.setNegativeButton("取消", new OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// }
		// });
		builder.create().show();

		return builder;
	}

	public static AlertDialog.Builder showDialog(Context context, String title,
			String message, OnClickListener okListener) {

		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(message);

		builder.setTitle(title);
		builder.setPositiveButton(context.getString(R.string.dialog_btn_ok),
				okListener);

		builder.setNegativeButton(
				context.getString(R.string.login_register_cancel),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();

		return builder;
	}

	public static ProgressDialog show(Context context, String title,
			String message) {
		try {
			ProgressDialog pd = new ProgressDialog(context);
			pd.setTitle(title);
			pd.setMessage(message);
			pd.setCancelable(true);
			pd.show();
			return pd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ProgressDialog show(Context context, int titleResId,
			int messageResId, OnCancelListener cancelListener) {
		try {
			ProgressDialog pd = new ProgressDialog(context);
			pd.setTitle(titleResId);
			pd.setMessage(context.getText(messageResId));
			pd.setCancelable(true);
			pd.setOnCancelListener(cancelListener);
			pd.show();
			return pd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ProgressDialog show(Context context,
			OnCancelListener cancelListener) {
		try {
			ProgressDialog pd = new ProgressDialog(context);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.setOnCancelListener(cancelListener);
			pd.show();
			return pd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 链接中对话框
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);

		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);

		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);

		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);

		// loading_dialog
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		// loadingDialog.setOnCancelListener(cancelListener);

		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

		return loadingDialog;
	}

	public static void setText(ProgressDialog pd, String title, String message,
			OnCancelListener cancelListener) {
		if (pd == null)
			return;

		if (cancelListener != null)
			pd.setOnCancelListener(cancelListener);

		if (title != null)
			pd.setTitle(title);

		if (message != null)
			pd.setMessage(message);
	}

	public static void dismiss(ProgressDialog pd) {
		if (pd == null)
			return;

		if (pd.isShowing() && pd.getWindow() != null) {
			try {
				pd.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
