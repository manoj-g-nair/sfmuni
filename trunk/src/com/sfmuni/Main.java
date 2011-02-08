/**
 * 
 */
package com.sfmuni;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author fangfang
 *
 */
public class Main extends Activity {

@Override
public void onCreate(Bundle savedInstanceState)
{
	super.onCreate(savedInstanceState);
	
	setContentView(R.layout.main);
	Button btnFindStop=(Button)findViewById(R.id.findStop);
	btnFindStop.setOnClickListener(ShowRoutes);
	Button btnSearch=(Button)findViewById(R.id.search);
	btnSearch.setOnClickListener(showSearch);
	Button btnNearest=(Button)findViewById(R.id.findNearest);
	btnNearest.setOnClickListener(findNear);
	Button btnExit=(Button)findViewById(R.id.exit);
	btnExit.setOnClickListener(exit);
	
}
private OnClickListener exit=new OnClickListener()
{
	@Override
	public void onClick(View v)
	{
		Main.this.finish();
	}
	};
private OnClickListener findNear=new OnClickListener()
{
	@Override
	public void onClick(View v)
	{
		Intent intent=new Intent();
		intent.setClass(Main.this, NearestStopMap.class);
		startActivity(intent);
	}
};
private OnClickListener showSearch=new OnClickListener()
{
	@Override
	public void onClick(View v)
	{
		Intent intent=new Intent();
		intent.setClass(Main.this, SearchMain.class);
		startActivity(intent);
	}
	};
	private  OnClickListener ShowRoutes=new OnClickListener()
	{

	@Override
	public void onClick(View v) {
		
		
		Intent intent=new Intent();
		intent.setClass(Main.this,RouteChooser.class);
	
		startActivity(intent);
		

	}




	};
	


}



