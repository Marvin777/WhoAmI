package de.yekta.whoami;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class KameraFragment extends Fragment
{
	private double mWidth = 0;
	private double mHeight = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.kamera_main, container, false);

		Button buttonHauptKamera = (Button) rootView.findViewById(R.id.kmHauptkameraStarten);

		addStartKameraListener(buttonHauptKamera);
		buttonHauptKamera.setVisibility(View.GONE);

		int anzahlKameras = Camera.getNumberOfCameras();

		double megapixelHaupt = 0;
		if (anzahlKameras > 0)
		{
			Camera cameraHaupt = Camera.open(0);
			megapixelHaupt = berechneMegapixel(cameraHaupt);
			ermittleWidthHeight(cameraHaupt);
			cameraHaupt.release();
		}
		double megapixelFront = 0;
		if (anzahlKameras > 1)
		{
			Camera cameraFront = Camera.open(1);
			megapixelFront = berechneMegapixel(cameraFront);
			cameraFront.release();
		}
		String ausgabeMegapixel = "";

		if (megapixelHaupt == 0 && megapixelFront == 0)
		{
			ausgabeMegapixel = "Sie haben weder eine Front- noch eine Hauptkamera";
			buttonHauptKamera.setVisibility(View.VISIBLE);
		}
		if (megapixelHaupt > 0)
		{
			ausgabeMegapixel = "Sie haben eine Hauptkamera mit: " + megapixelHaupt + " Megapixel";
			buttonHauptKamera.setVisibility(View.VISIBLE);
		}
		if (megapixelFront > 0)
		{
			ausgabeMegapixel += "\n\n\n Sie haben eine Frontkamera mit: " + megapixelFront + " Megapixel";
			buttonHauptKamera.setVisibility(View.VISIBLE);
		}

		ausgabeMegapixel += "\n\nWIDTH[" + mWidth + "] HEIGHT[" + mHeight + "]";

		TextView textView = (TextView) rootView.findViewById(R.id.kmTextView);
		textView.setText(ausgabeMegapixel);
		return rootView;

	}

	protected void addStartKameraListener(Button buttonHauptKamera)
	{
		buttonHauptKamera.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivity(intent);
			}
		});
	}

	protected void ermittleWidthHeight(Camera camera)
	{
		Parameters parameters = camera.getParameters();
		List<Size> supportedPictureSizes = parameters.getSupportedPictureSizes();

		for (Size size : supportedPictureSizes)
		{
			if (size.width > mWidth)
			{
				mWidth = size.width;
			}
			if (size.height > mHeight)
			{
				mHeight = size.height;
			}

		}

	}

	protected double berechneMegapixel(Camera camera)
	{
		Parameters parameters = camera.getParameters();
		List<Size> supportedPictureSizes = parameters.getSupportedPictureSizes();

		int width = 0;
		int height = 0;

		for (Size size : supportedPictureSizes)
		{
			if (size.width > width)
			{
				width = size.width;
			}
			if (size.height > height)
			{
				height = size.height;
			}

		}

		double widthHeight = width * height;
		double divisor = 1000000;
		widthHeight = widthHeight / divisor;
		widthHeight = Math.round(widthHeight);

		Log.i(this.getClass().getName(), "WIDTH HEIGHT: " + widthHeight);
		return widthHeight;
	}
}
