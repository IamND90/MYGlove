package fragments;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;


import external.Gesture;
import external.UploadGesture;

import pa.myglove.MainActivity;
import pa.myglove.MyAdapter;
import pa.myglove.ProfileManager;
import pa.myglove.R;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public  class F_Gestures extends Fragment {

	View rootView;
	public static Button b_record;
	static ImageButton b_newprofile,b_deleteprofile, b_expandchart,b_updateGraph,b_save,b_load;
	
	static ListView list_commands;
	static Spinner spinner_profile;
	static MyAdapter adapter;
	ArrayAdapter<String> madapter;
	
	ProfileManager pm;
	int selectedPosition_Profile,selectedPosition_Gesture;
	

	public static GraphicalView mChart;
	public static XYSeries serie1[];
	boolean chart_expanded;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_gestures, container,false);
		
		pm= new ProfileManager();
		
		X_findID();
		X_init();
		X_onCLick();
		
		chart_expanded = true;
		selectedPosition_Profile =0;
		selectedPosition_Gesture=0;
		
		return rootView;
	}


	private void X_init() {
		// TODO Auto-generated method stub
		adapter= new  MyAdapter(getActivity(), R.layout.custom_list1, 
				MainActivity.mybt.com.Gp.Commands);
		
		list_commands.setAdapter(adapter);
		
		madapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item,
				pm.gesturesPrefs);
		spinner_profile.setAdapter(madapter);
		
		initChart((LinearLayout)rootView.findViewById(R.id.linelayout_placeholder_graph));
	}


	private void X_onCLick() {
		// TODO Auto-generated method stub
		b_newprofile.setOnClickListener(new ImageButton.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						pm.createNewProfile();
						madapter.notifyDataSetChanged();
					}
				});
		b_deleteprofile.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pm.deleteProfile(selectedPosition_Profile);
				madapter.notifyDataSetChanged();
			}
		});
		b_record.setOnClickListener(new ImageButton.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						MainActivity.mybt.com.RecordGesture(MainActivity.mybt.com.Gp.Commands.get(selectedPosition_Gesture));
					}
		});
		b_expandchart.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				expandChart(!chart_expanded);
				
			}
		});
		b_updateGraph.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				boolean yes = MainActivity.mybt.com.enableGraphUpdate();
				
				if( yes)
					b_updateGraph.setBackgroundResource(R.drawable.custom_btn_genoa);
				else
					b_updateGraph.setBackgroundResource(R.drawable.custom_btn_arsenic);
					
			}
		});
		
		b_save.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MainActivity.mybt.com.Gp.saveAllGestures();
			}
		});
		b_deleteprofile.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MainActivity.mybt.com.Gp.deleteALLGestures();
			}
		});
		
		
		list_commands.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				adapter.setColor(selectedPosition_Gesture, Color.BLACK);
				selectedPosition_Gesture = position;
				adapter.setColor(position, Color.BLUE);
				
				Gesture G= MainActivity.mybt.com.Gp.getGesture(position);
				updateChart1(G, true);
			
				list_commands.setAdapter(adapter);
			}
		});
		
		spinner_profile.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int index, long arg3) {
				// TODO Auto-generated method stub
				//String selected = pm.gesturesPrefs.get(index);
				pm.select(index);
				selectedPosition_Profile = index;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				selectedPosition_Profile = spinner_profile.getSelectedItemPosition();
			}
		});
	
	}


	private void X_findID() {
		// TODO Auto-generated method stub
	
		b_newprofile= (ImageButton) rootView.findViewById(R.id.imageButton_new_profile);
		b_deleteprofile= (ImageButton) rootView.findViewById(R.id.imageButton_delete_profile);
		b_record= (Button) rootView.findViewById(R.id.button_recordGesture);
		b_save= (ImageButton) rootView.findViewById(R.id.imageButton_saveGestures);
		b_load= (ImageButton) rootView.findViewById(R.id.imageButton_deleteGestures);
		b_expandchart= (ImageButton) rootView.findViewById(R.id.imageButton_graph_expand);
		b_updateGraph= (ImageButton) rootView.findViewById(R.id.imageButton_graph_update);
		
		list_commands = (ListView) rootView.findViewById(R.id.listView_Gestures);
		spinner_profile = (Spinner) rootView.findViewById(R.id.spinner_gesture_profiles);
	}
	
	static void initChart(LinearLayout lin) {

		XYSeriesRenderer render1, render2, render3, render4, render5, render6;
		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

		serie1 = new XYSeries[6];

		render1 = new XYSeriesRenderer();
		render1.setColor(Color.BLUE);
		render1.setLineWidth(5.f);
		render2 = new XYSeriesRenderer();
		render2.setColor(Color.RED);
		render2.setLineWidth(5.f);
		render3 = new XYSeriesRenderer();
		render3.setColor(Color.GREEN);
		render3.setLineWidth(5.f);
		render4 = new XYSeriesRenderer();
		render4.setColor(Color.BLACK);
		render4.setLineWidth(4.f);
		render5 = new XYSeriesRenderer();
		render5.setColor(Color.GRAY);
		render5.setLineWidth(4.f);
		render6 = new XYSeriesRenderer();
		render6.setColor(Color.DKGRAY);
		render6.setLineWidth(4.f);

		for (int i = 0; i < 6; i++) {
			serie1[i] = new XYSeries("1_" + i);
			mDataset.addSeries(serie1[i]);
		}

		mRenderer.addSeriesRenderer(render1);
		mRenderer.addSeriesRenderer(render2);
		mRenderer.addSeriesRenderer(render3);
		mRenderer.addSeriesRenderer(render4);
		mRenderer.addSeriesRenderer(render5);
		mRenderer.addSeriesRenderer(render6);

		mRenderer.setYAxisMax(125 + 1.0);
		mRenderer.setYAxisMin(-125 - 1.0);

		mRenderer.setPointSize(100.f);

		mChart = ChartFactory.getCubeLineChartView(MainActivity.GETCONTEXT, mDataset,
				mRenderer, 0.5f);

		lin.addView(mChart);
		lin.setClickable(false);
		mChart.setClickable(false);
	
		
	}
	public static void updateChart1(long time, int[] in) {
		// TODO Auto-generated method stub
		// LinearLayout lin = (LinearLayout)
		// findViewById(R.id.linearLayout_graph);
		if (in.length == 6) {

			for (int i = 0; i < 6; i++) {
				while (serie1[i].getItemCount() > 40) {
					serie1[i].remove(0);
				}
			}

			for (int x = 0; x < 6; x++) {
				serie1[x].add(time, in[x]);

			}

			mChart.repaint();

		}
	}

	public static void updateChart1(Gesture g, boolean delete) {
		// TODO Auto-generated method stub
		// LinearLayout lin = (LinearLayout)
		// findViewById(R.id.linearLayout_graph);
		if (g.GestureName.equals(""))
			return;
		if (delete) {

			for (int i = 0; i < 6; i++)
				serie1[i].clear();

		}

		for (int x = 0; x < 3; x++) {
			for (int i = 0; i < g.PointList.size(); i++) {

				serie1[x].add(i, g.PointList.get(i).GyroValue[x]);
				serie1[x + 3].add(i, g.PointList.get(i).LinaccValue[x]);
			}

		}

		mChart.repaint();
	}

	
	
	
	public void expandChart(boolean in){
		
		LinearLayout L =(LinearLayout)rootView.findViewById(R.id.linelayout_placeholder_graph);
		
		int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, MainActivity.GETACTIVITY.getResources().getDisplayMetrics());
		
		if(in){
			L.getLayoutParams().height =  height;
			mChart.setVisibility(View.VISIBLE);
			
		}
		
		else{
			L.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
			mChart.setVisibility(View.GONE);
			
			
		}
		chart_expanded = in;
	}
	
	
	public static void UploadAllGestures() {

		final ArrayList<Gesture> G = new ArrayList<Gesture>(
				MainActivity.mybt.com.Gp.Gestures);
		
		MainActivity
		.addInfoText("Upload started: " + G.size() + " Gestures..");
		
			int size = G.size();

			Gesture ges[] = new Gesture[size];
		
			for (int i = 0; i<size; i++) {
				ges[i] = G.get(i);

			}
			new UploadGesture().execute(ges);
			
	} 
	
}
