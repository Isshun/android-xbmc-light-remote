/*
Copyright (c) 2011, Sony Ericsson Mobile Communications AB
Copyright (c) 2011-2013, Sony Mobile Communications AB

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * Neither the name of the Sony Ericsson Mobile Communications AB / Sony Mobile
 Communications AB nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.xbmc.lightremote.smartconnect.extension.controls;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.Log;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlListItem;
import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.http.IServiceListener;
import org.xbmc.lightremote.service.PlayerService;
import org.xbmc.lightremote.smartconnect.extension.SampleExtensionService;

/**
 * GalleryTestControl displays a swipeable gallery, based on a string array.
 */
public class GalleryTestControl extends ManagedControlExtension {

    protected int mLastKnowPosition = 0;
    public final static String EXTRA_INITIAL_POSITION = "EXTRA_INITIAL_POSITION";

	private List<MovieModel> mListContent;

    /**
     * @see ManagedControlExtension#ManagedControlExtension(Context, String,
     *      ControlManagerCostanza, Intent)
     */
    public GalleryTestControl(Context context, String hostAppPackageName, ControlManagerSmartWatch2 controlManager, Intent intent) {
        super(context, hostAppPackageName, controlManager, intent);
    }

    @Override
    public void onResume() {
        Log.d(SampleExtensionService.LOG_TAG, "onResume");
        showLayout(R.layout.layout_test_gallery, null);
        
        final PlayerService service = PlayerService.getInstance();

        mListContent = service.getMovies();
        if (mListContent == null) {
        service.setListener(new IServiceListener() {
				
				@Override
				public void onActionStart(int action) {
				}
				
				@Override
				public void onActionError(int action, String message) {
				}
				
				@Override
				public void onActionCompleted(int action) {
			        mListContent = service.getMovies();
			        sendListCount(R.id.gallery, mListContent.size());
				}
			});
	        service.reqMoviesLibrary();
	    } else {
	    	sendListCount(R.id.gallery, mListContent.size());
	    }

        // If requested, move to the correct position in the list.
        int startPosition = getIntent().getIntExtra(EXTRA_INITIAL_POSITION, 0);
        mLastKnowPosition = startPosition;
        sendListPosition(R.id.gallery, startPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Position is saved into Control's Intent, possibly to be used later.
        getIntent().putExtra(EXTRA_INITIAL_POSITION, mLastKnowPosition);
    }

    @Override
    public void onRequestListItem(final int layoutReference, final int listItemPosition) {
        Log.d(SampleExtensionService.LOG_TAG, "onRequestListItem() - position " + listItemPosition);
        if (layoutReference != -1 && listItemPosition != -1 && layoutReference == R.id.gallery) {
            ControlListItem item = createControlListItem(listItemPosition);
            if (item != null) {
                sendListItem(item);
            }
        }
    }

    @Override
    public void onListItemSelected(ControlListItem listItem) {
        super.onListItemSelected(listItem);
        // We save the last "selected" position, this is the current visible
        // list item index. The position can later be used on resume
        mLastKnowPosition = listItem.listItemPosition;
    }

    @Override
    public void onListItemClick(final ControlListItem listItem, final int clickType, final int itemLayoutReference) {
        Log.d(SampleExtensionService.LOG_TAG, "Item clicked. Position " + listItem.listItemPosition
                + ", itemLayoutReference " + itemLayoutReference + ". Type was: "
                + (clickType == Control.Intents.CLICK_TYPE_SHORT ? "SHORT" : "LONG"));
        
        MovieModel movie = mListContent.get(listItem.listItemPosition);
        
        PlayerService service = PlayerService.getInstance();
        service.setListener(new IServiceListener() {
			
			@Override
			public void onActionStart(int action) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onActionError(int action, String message) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onActionCompleted(int action) {
				// TODO Auto-generated method stub
				
			}
		});
        service.reqOpen(movie.getFile());
    }

    protected ControlListItem createControlListItem(int location) {

    	MovieModel movie = mListContent.get(location);
    	
        ControlListItem item = new ControlListItem();
        item.layoutReference = R.id.gallery;
        item.dataXmlLayout = R.layout.item_gallery;
        item.listItemId = location;
        item.listItemPosition = location;

        // Header data
        Bundle headerBundle = new Bundle();
        headerBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE, R.id.title);
        headerBundle.putString(Control.Intents.EXTRA_TEXT, movie.getTitle());

        // Body data
        Bundle bodyBundle = new Bundle();
        bodyBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE, R.id.body);
        bodyBundle.putString(Control.Intents.EXTRA_TEXT, mListContent.get(location).getFile());

//        Bitmap bitmap = BitmapFactory.decodeFile(movie.thumbnailPath);
//        if (bitmap != null) {
//	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//	        bitmap.compress(CompressFormat.JPEG, 70, stream);
//	        byte[] data = stream.toByteArray();
//	        Bundle iconBundle = new Bundle();
//	        iconBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE, R.id.thumbnail);
//	        iconBundle.putByteArray(Control.Intents.EXTRA_DATA, data);
//        }
        
        item.layoutData = new Bundle[2];
        item.layoutData[0] = headerBundle;
        item.layoutData[1] = bodyBundle;

        return item;
    }

}
