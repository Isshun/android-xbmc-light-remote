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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlListItem;
import com.sonyericsson.extras.liveware.extension.util.control.ControlObjectClickEvent;
import com.sonyericsson.extras.liveware.extension.util.control.ControlView;
import com.sonyericsson.extras.liveware.extension.util.control.ControlViewGroup;
import com.sonyericsson.extras.liveware.extension.util.control.ControlView.OnClickListener;

import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.data.PlayingProperties;
import org.xbmc.lightremote.http.IServiceListener;
import org.xbmc.lightremote.service.PlayerService;
import org.xbmc.lightremote.smartconnect.extension.SampleExtensionService;

/**
 * GalleryTestControl displays a swipeable gallery, based on a string array.
 */
public class PlayingControl extends ManagedControlExtension {

    private ControlViewGroup mLayout;

	public PlayingControl(Context context, String hostAppPackageName, ControlManagerSmartWatch2 controlManager, Intent intent) {
        super(context, hostAppPackageName, controlManager, intent);

        setupClickables(context);
    }

    @Override
    public void onResume() {
        Log.d(SampleExtensionService.LOG_TAG, "onResume");
        showLayout(R.layout.layout_sw_playing, null);
        
    	MovieModel movie = PlayerService.getInstance().getPlaying();
    	sendText(R.id.lb_name, movie.label);
    }

    @Override
    public void onObjectClick(ControlObjectClickEvent event) {
        Log.v(SampleExtensionService.LOG_TAG, "onObjectClick");
        if (mLayout != null) {
            mLayout.onClick(event.getLayoutReference());
        }
    }
    
    @Override
    public void onSwipe(final int direction) {
        Log.v(SampleExtensionService.LOG_TAG, "onSwipe: " + direction);

        if (direction == Control.Intents.SWIPE_DIRECTION_RIGHT || direction == Control.Intents.SWIPE_DIRECTION_LEFT) {
	    	PlayerService.getInstance().reqProperties();
	    	PlayerService.getInstance().setListener(new IServiceListener() {
				@Override
				public void onActionStart(int action) {
				}
				
				@Override
				public void onActionError(int action, String message) {
				}
				
				@Override
				public void onActionCompleted(int action) {
					if (action == PlayerService.GET_PROPERTIES) {
	                	MovieModel movie = PlayerService.getInstance().getPlaying();
	                	PlayingProperties properties = PlayerService.getInstance().getProperties();
	                	if (movie != null) {
	                		PlayerService.getInstance().reqSeek(properties.percentage + (direction == Control.Intents.SWIPE_DIRECTION_RIGHT ? 1 : -1));
	                	}
					}
				}
			});
        }
    }

    private void setupClickables(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_sw_playing, null);
        mLayout = (ControlViewGroup) parseLayout(layout);
        if (mLayout != null) {
            mLayout.findViewById(R.id.lb_backward).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick() {
                	PlayerService.getInstance().reqProperties();
                	PlayerService.getInstance().setListener(new IServiceListener() {
						@Override
						public void onActionStart(int action) {
						}
						
						@Override
						public void onActionError(int action, String message) {
						}
						
						@Override
						public void onActionCompleted(int action) {
							if (action == PlayerService.GET_PROPERTIES) {
			                	MovieModel movie = PlayerService.getInstance().getPlaying();
			                	PlayingProperties properties = PlayerService.getInstance().getProperties();
			                	if (movie != null) {
			                		PlayerService.getInstance().reqSeek(properties.percentage - 1);
			                	}
							}
						}
					});
                }
            });
            mLayout.findViewById(R.id.lb_play).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick() {
                	PlayerService.getInstance().reqPlay();
                }
            });
            mLayout.findViewById(R.id.lb_forward).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick() {
                }
            });
        }
    }
}
