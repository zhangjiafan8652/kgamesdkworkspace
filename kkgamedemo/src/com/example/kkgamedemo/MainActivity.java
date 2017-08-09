package com.example.kkgamedemo;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.yayawan.callback.YYWAnimCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.Kgame;

public class MainActivity extends Activity {

	private TextView tv_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("oncreate");

		Kgame.getInstance().initSdk(this);
		Kgame.getInstance().onCreate(this);

		final LinearLayout mLinearLayout = new LinearLayout(this);

		mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mLinearLayout.setPadding(10, 10, 10, 10);

		Button animButton = new Button(this);
		animButton.setText("anim");
		animButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				anim(mLinearLayout);

			}
		});

		mLinearLayout.addView(animButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button updateButton = new Button(this);
		updateButton.setText("update");
		updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				upDate();
			}

		});

		Button lgoinButton = new Button(this);
		lgoinButton.setText("login");
		lgoinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				login(mLinearLayout);

			}
		});
		mLinearLayout.addView(lgoinButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button payButton = new Button(this);
		payButton.setText("pay");
		payButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pay(mLinearLayout);

			}
		});
		mLinearLayout.addView(payButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button extButton = new Button(this);
		extButton.setText("exit");
		extButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exit(mLinearLayout);

			}
		});
		mLinearLayout.addView(extButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(mLinearLayout);

		Button versionButton = new Button(this);
		versionButton.setText("��ȡsdk�汾��");
		versionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getversion(mLinearLayout);

			}

		});

		Button inintButton = new Button(this);
		inintButton.setText("������ʱ��init�ӿ�");
		inintButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initsdk(mLinearLayout);

			}

		});
		mLinearLayout.addView(versionButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button logoutButton = new Button(this);
		logoutButton.setText("ע���˺�");
		logoutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Logout();

			}

		});
		mLinearLayout.addView(logoutButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		tv_view = new TextView(this);
		// logoutButton.setText("ע���˺�");
		mLinearLayout.addView(tv_view, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		setContentView(mLinearLayout);

	}

	public void Demologin() {

	}

	/**
	 * ע���˺�...ʹ�ó���,�����Ϸ����ע���˺ŵĹ��ܰ�ť,������ť��,.���ô˷���ע���˺�...
	 * �ص��ɹ�����onlogout�н��лص���¼ҳ��Ĳ���
	 */
	protected void Logout() {
		// TODO Auto-generated method stub
		Kgame.getInstance().logout(null, new YYWUserCallBack() {

			@Override
			public void onLoginSuccess(YYWUser paramUser, Object paramObject) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoginFailed(String paramString, Object paramObject) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLogout(Object paramObject) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}

		});
	}

	/**
	 * ���½ӿ�
	 */
	public void upDate() {

	}

	public void anim(View v) {

		//Kgame.getInstance().setData(this, "", "", "", "", "", "", "");

		Kgame.getInstance().anim(this, new YYWAnimCallBack() {

			@Override
			public void onAnimSuccess(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "���Ŷ����ص�", Toast.LENGTH_SHORT)
						.show();

			}

			@Override
			public void onAnimFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void login(View v) {
		System.out.println("��¼");
		Kgame.getInstance().login(this, new YYWUserCallBack() {

			@Override
			public void onLogout(Object arg0) {
				Toast.makeText(MainActivity.this, "�˳�", Toast.LENGTH_SHORT)
						.show();

			}

			@Override
			public void onLoginSuccess(YYWUser user, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("��¼�ɹ�" + user);
				Toast.makeText(MainActivity.this, "��¼�ɹ�" + user,
						Toast.LENGTH_SHORT).show();
				// textxinx=user.toString()+"/n/r";
				// tv_view.setText(textxinx);
				// ��¼�ɹ������ý�ɫ����
				Kgame.getInstance().setData(MainActivity.this, user.uid,
						user.userName, "11", "1", "征服之海",
						System.currentTimeMillis() / 1000 + "", "1");
			}

			@Override
			public void onLoginFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("ʧ��");
				Toast.makeText(MainActivity.this, "ʧ��", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				System.out.println("ȡ��");
				Toast.makeText(MainActivity.this, "ȡ��", Toast.LENGTH_SHORT)
						.show();

			}

		});
	}

	public void pay(View v) {

		YYWOrder order = new YYWOrder(UUID.randomUUID().toString(), "���ޱ�",
				10l, "");

		Kgame.getInstance().pay(this, order, new YYWPayCallBack() {
			@Override
			public void onPaySuccess(YYWUser arg0, YYWOrder arg1, Object arg2) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "��ֵ�ɹ��ص�", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onPayFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("֧��ʧ��");
			}

			@Override
			public void onPayCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("֧���˳�");
			}
		});
	}

	/**
	 * ��ȡsdk�汾��
	 * 
	 * @param mLinearLayout
	 */
	private void getversion(LinearLayout mLinearLayout) {
		// TODO Auto-generated method stub

	}

	/**
	 * �˳��ӿ�
	 * 
	 * @param v
	 */
	public void exit(View v) {
		System.out.println("��¼");

		Kgame.getInstance().exit(this, new YYWExitCallback() {

			@Override
			public void onExit() {
				Toast.makeText(MainActivity.this, "�˳��ص�", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	/**
	 * �޷�������ʱ��init�ӿ�
	 * 
	 * @param mLinearLayout
	 */
	private void initsdk(LinearLayout mLinearLayout) {
		// TODO Auto-generated method stub
		Kgame.getInstance().initSdk(this);
	}

	// ��Ϸ�е���sdkС���ֿ�ѡ
	public void accountManage(View v) {
		Kgame.getInstance().manager(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		System.out.println("onrestart");
		Kgame.getInstance().onRestart(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onresume");
		Kgame.getInstance().onResume(this);
		// Kgame.getInstance().onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Kgame.getInstance().onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Kgame.getInstance().onStop(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Kgame.getInstance().onDestroy(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// Yayalog.loger("demoonActivityResult");
		Kgame.getInstance().onActivityResult(this, requestCode, resultCode,
				data);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Kgame.getInstance().onNewIntent(intent);
	}

	public void onWindowFocusChanged(boolean hasFocus) {

	};

}
