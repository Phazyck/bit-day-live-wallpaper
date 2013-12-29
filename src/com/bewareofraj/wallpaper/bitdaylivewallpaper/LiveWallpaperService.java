package com.bewareofraj.wallpaper.bitdaylivewallpaper;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class LiveWallpaperService extends WallpaperService {

	@Override
	public Engine onCreateEngine() {
		return new MyWallpaperEngine();
	}

	public class MyWallpaperEngine extends Engine {

		private final Handler handler = new Handler();
		private final Runnable drawRunner = new Runnable() {
			@Override
			public void run() {
				draw();
			}
		};
		private boolean visible = true;
		public Bitmap backgroundImage;
		
		// Wallpaper images
		public Bitmap afternoonImage,
						eveningImage,
						lateAfternoonImage,
						lateEveningImage,
						lateMorningImage,
						lateNightImage,
						morningImage,
						nightImage;

		public MyWallpaperEngine() {
			backgroundImage = BitmapFactory.decodeResource(getResources(),
					R.drawable.morning);
		}

		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			this.visible = visible;
			// if screen wallpaper is visible then draw the image otherwise do
			// not draw
			if (visible) {
				handler.post(drawRunner);
			} else {
				handler.removeCallbacks(drawRunner);
			}
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			this.visible = false;
			handler.removeCallbacks(drawRunner);
		}

		public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
				float yStep, int xPixels, int yPixels) {
			draw();
		}

		public void draw() {
			final SurfaceHolder holder = getSurfaceHolder();

			Canvas c = null;
			try {
				c = holder.lockCanvas();
				// clear the canvas
				c.drawColor(Color.BLACK);
				if (c != null) {
					// Bitmap resized =
					// Bitmap.createScaledBitmap(backgroundImage,
					// backgroundImage.getWidth(), c.getHeight(), true);
					Bitmap resized = createScaledImageFillHeight(backgroundImage, c.getWidth(), c.getHeight());
					// draw the background image
					int x = 0;
					c.drawBitmap(resized, x, 0, null);

					// get width of canvas
					// int width = c.getWidth();
				}
			} finally {
				if (c != null) {
					holder.unlockCanvasAndPost(c);
				}
			}

			handler.removeCallbacks(drawRunner);
			if (visible) {
				handler.postDelayed(drawRunner, 10); // delay 10 milliseconds
			}
		}

		private Bitmap createScaledImageFillWidth(Bitmap originalImage, int width, int height) {
			Bitmap scaledImage = Bitmap.createBitmap((int) width, (int) height,
					Config.ARGB_8888);
			float originalWidth = originalImage.getWidth(), originalHeight = originalImage
					.getHeight();
			Canvas canvas = new Canvas(scaledImage);
			float scale = width / originalWidth;
			float xTranslation = 0.0f, yTranslation = (height - originalHeight
					* scale) / 2.0f;
			Matrix transformation = new Matrix();
			transformation.postTranslate(xTranslation, yTranslation);
			transformation.preScale(scale, scale);
			Paint paint = new Paint();
			paint.setFilterBitmap(true);
			canvas.drawBitmap(originalImage, transformation, paint);
			return scaledImage;
		}
		
		private Bitmap createScaledImageFillHeight(Bitmap originalImage, int width, int height) {
			Bitmap scaledImage = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			float originalWidth = originalImage.getWidth(), originalHeight = originalImage
					.getHeight();
			Canvas canvas = new Canvas(scaledImage);
			float scale = height / originalHeight;
			float yTranslation = 0.0f, xTranslation = (width - originalWidth
					* scale) / 2.0f;
			Matrix transformation = new Matrix();
			transformation.postTranslate(xTranslation, yTranslation);
			transformation.preScale(scale, scale);
			Paint paint = new Paint();
			paint.setFilterBitmap(true);
			canvas.drawBitmap(originalImage, transformation, paint);
			return scaledImage;
		}
	}

}
