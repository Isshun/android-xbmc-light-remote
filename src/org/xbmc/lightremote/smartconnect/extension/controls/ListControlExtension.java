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
import java.io.File;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
 * ListControlExtension displays a scrollable list, based on a string array.
 * Tapping on list items opens a swipable detail view.
 */
public class ListControlExtension extends ManagedControlExtension {

    protected int mLastKnowPosition = 0;
	private List<MovieModel> mListContent;

    /**
     * @see ManagedControlExtension#ManagedControlExtension(Context, String,
     *      ControlManagerCostanza, Intent)
     */
    public ListControlExtension(Context context, String hostAppPackageName, ControlManagerSmartWatch2 controlManager, Intent intent) {
        super(context, hostAppPackageName, controlManager, intent);
        Log.d(SampleExtensionService.LOG_TAG, "ListControl constructor");
    }

    @Override
    public void onResume() {
        Log.d(SampleExtensionService.LOG_TAG, "onResume");
        showLayout(R.layout.layout_test_list, null);

        if (mListContent == null) {
	        final PlayerService service = PlayerService.getInstance();
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
			        sendListCount(R.id.listView, mListContent != null ? mListContent.size() : 0);
				}
			});
	        service.reqMoviesLibrary();
	    } else {
	    	sendListCount(R.id.listView, mListContent.size());
	    }

        // If requested, move to the correct position in the list.
        int startPosition = getIntent().getIntExtra(GalleryTestControl.EXTRA_INITIAL_POSITION, 0);
        mLastKnowPosition = startPosition;
        sendListPosition(R.id.listView, startPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Position is saved into Control's Intent, possibly to be used later.
        getIntent().putExtra(GalleryTestControl.EXTRA_INITIAL_POSITION, mLastKnowPosition);
    }

    @Override
    public void onRequestListItem(final int layoutReference, final int listItemPosition) {
        Log.d(SampleExtensionService.LOG_TAG, "onRequestListItem() - position " + listItemPosition);
        if (layoutReference != -1 && listItemPosition != -1 && layoutReference == R.id.listView) {
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

        if (clickType == Control.Intents.CLICK_TYPE_SHORT) {
            Intent intent = new Intent(mContext, GalleryTestControl.class);
            intent.putExtra(GalleryTestControl.EXTRA_INITIAL_POSITION, listItem.listItemPosition);
            mControlManager.startControl(intent);
        }
    }

    protected ControlListItem createControlListItem(int position) {

        ControlListItem item = new ControlListItem();
        item.layoutReference = R.id.listView;
        item.dataXmlLayout = R.layout.item_list;
        item.listItemPosition = position;
        // We use position as listItemId. Here we could use some other unique id
        // to reference the list data
        item.listItemId = position;

        String uri = Uri.fromFile(new File(mListContent.get(position).thumbnailPath)).toString();
        int icon = R.drawable.thumbnail_list_item;
        // Icon data
        Bundle iconBundle = new Bundle();
        iconBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE, R.id.thumbnail);
//        iconBundle.putString(Control.Intents.EXTRA_DATA_URI, ExtensionUtils.getUriString(mContext, icon));
//        iconBundle.putString(Control.Intents.EXTRA_DATA_URI, uri);
        
        Bitmap bitmap = BitmapFactory.decodeFile(mListContent.get(position).thumbnailPath);
        if (bitmap != null) {
        	ByteArrayOutputStream stream = new ByteArrayOutputStream();
        	bitmap.compress(CompressFormat.JPEG, 70, stream);
        	byte[] data = stream.toByteArray();
        	iconBundle.putByteArray(Control.Intents.EXTRA_DATA, data);
        }
        
//        Log.d("XBMC", "path 1 = " + ExtensionUtils.getUriString(mContext, icon));
//        Log.d("XBMC", "path 2 = " + uri);

        // Header data
        Bundle headerBundle = new Bundle();
        headerBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE, R.id.title);
        headerBundle.putString(Control.Intents.EXTRA_TEXT, mListContent.get(position).label);

        // Body data
        Bundle bodyBundle = new Bundle();
        bodyBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE, R.id.body);
        bodyBundle.putString(Control.Intents.EXTRA_TEXT, mListContent.get(position).label);

        item.layoutData = new Bundle[3];
        item.layoutData[0] = iconBundle;
        item.layoutData[1] = headerBundle;
        item.layoutData[2] = bodyBundle;

        return item;
    }

}
