package com.webrtc.boyj.extension.databinding;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.webrtc.boyj.api.boyjrtc.BoyjMediaStream;
import com.webrtc.boyj.extension.custom.BoyjSurfaceView;
import com.webrtc.boyj.extension.custom.SplitLayout;
import com.webrtc.boyj.presentation.call.CallAdapter;

import org.webrtc.MediaStream;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;

public class BindingAdapters {
    @SuppressWarnings("unchecked")
    @BindingAdapter({"items"})
    public static <T, VH extends RecyclerView.ViewHolder> void setItems(
            @NonNull final RecyclerView recyclerView,
            @Nullable final List<T> items) {
        final ListAdapter<T, VH> adapter = (ListAdapter<T, VH>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.submitList(items == null ? null : new ArrayList<>(items));
        }
    }

    @SuppressLint("DefaultLocale")
    @BindingAdapter({"callTime"})
    public static void setCallTime(@NonNull final TextView textView, final int time) {
        if (time >= 0) {
            int min = time / 60;
            int sec = time % 60;
            textView.setText(String.format("%02d:%02d", min, sec));
        }
    }

    @BindingAdapter({"localStream"})
    public static void bindMediaStream(@NonNull final BoyjSurfaceView surfaceView,
                                       @Nullable final MediaStream mediaStream) {
        if (mediaStream != null) {
            final VideoTrack track = mediaStream.videoTracks.get(0);
            track.addSink(surfaceView);
        }
    }

    @BindingAdapter({"remoteStreams"})
    public static void bindMediaStreams(@NonNull final SplitLayout splitLayout,
                                        @Nullable final List<BoyjMediaStream> mediaStreams) {
        final CallAdapter adapter = (CallAdapter) splitLayout.getAdapter();
        if (adapter != null) {
            adapter.submitMediaStreams(
                    mediaStreams == null ?
                            new ArrayList<>() :
                            new ArrayList<>(mediaStreams));
        }
    }

    @BindingAdapter({"callAnimation"})
    public static void callAnimation(@NonNull final BoyjSurfaceView view,
                                     final boolean isCalling) {
        if (isCalling) {
            view.startAnimation(new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.width = dp2Px(80);
                    params.height = dp2Px(120);
                    params.endToEnd = ConstraintLayout.LayoutParams.UNSET;
                    params.bottomToBottom = ConstraintLayout.LayoutParams.UNSET;
                    params.setMargins(dp2Px(24), dp2Px(24), 0, 0);
                    view.setLayoutParams(params);
                }
            });
        }
    }

    private static int dp2Px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
