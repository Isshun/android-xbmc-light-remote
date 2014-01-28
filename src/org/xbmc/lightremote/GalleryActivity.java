package org.xbmc.lightremote;

import org.xbmc.lightremote.service.ImageService;
import org.xbmc.lightremote.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class GalleryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gallery);
		String url = getIntent().getExtras().getString("img");
		ImageView image = (ImageView)findViewById(R.id.img);
		ImageService.getInstance().showFullscreen(image, url);
		
		//(float) size.x / 2, size.y / 2
		
//		Display display = getWindowManager().getDefaultDisplay();
//		Point size = new Point();
//		display.getSize(size);
//		int width = size.x;
//		int height = size.y;
		
//		Matrix matrix = new Matrix();
//		image.setScaleType(ScaleType.MATRIX);   //required
//		matrix.postRotate(90, size.x / 2, size.y / 2);
//		image.setImageMatrix(matrix);
//		
//		image.setLayoutParams(new FrameLayout.LayoutParams(size.x, size.y));
	}

}
