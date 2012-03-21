package com.write.Quill.ImageEditor;

import com.write.Quill.ActivityBase;
import com.write.Quill.R;
import com.write.Quill.data.Book;
import com.write.Quill.data.Bookshelf;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;

public class ImageActivity 
	extends ActivityBase {
	
	private static final String TAG = "ImageActivity";

	private View layout;
	private LinearLayout linearLayout;
	private Menu menu;
	
	private Bookshelf bookshelf = null;
	private Book book = null;

    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      	bookshelf = Bookshelf.getBookshelf();
      	book = Bookshelf.getCurrentBook();
      	
		layout = getLayoutInflater().inflate(R.layout.image_editor, null);
		setContentView(layout);
		
		linearLayout = (LinearLayout) findViewById(R.id.image_editor_linear_layout);
		
        ActionBar bar = getActionBar();
        bar.setTitle(R.string.image_editor_title);
        bar.setDisplayHomeAsUpEnabled(true);

        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu mMenu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_editor, mMenu);
        menu = mMenu;
        return true;
    }
	
    
    @Override
    protected void onResume() {
    	super.onResume();
    }

}
