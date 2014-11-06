package pa.myglove;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public  class MyAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int id;
    private List <String>items ;
    private int colorlist[];
    
    
    public MyAdapter(Context context, int textViewResourceId , List<String> list ) 
    {
        super(context, textViewResourceId, list);           
        mContext = context;
        id = textViewResourceId;
        items = list ;
        colorlist =new int[list.size()];
        
        for(int i =0; i<list.size(); i++){
        	colorlist[i] = 0;
        	
        }
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
     
        
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textView_inlist);

        if(items.get(position) != null )
        {
        	if( colorlist[position] != 0){
        		text.setTextColor(colorlist[position]);
        		
        	}else text.setTextColor(Color.BLACK);
        	
            text.setText(items.get(position));
            
          
        }

        return mView;
    }
    
    public void setColor(int position, int color){
    	int l = colorlist.length;
    	
    	if(position==-1){
    		for(int i=0; i<colorlist.length;i++)
    			colorlist[i] = color;
    		return;
    	}
    	
    	
    	if( position <l && position >=0){
    		colorlist[position] = color;
    	}
    }
 

}
