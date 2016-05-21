package com.ilmnuri.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ilmnuri.com.PlayActivity;
import com.ilmnuri.com.R;
import com.ilmnuri.com.event.AudioEvent;
import com.ilmnuri.com.model.AlbumModel;
import com.ilmnuri.com.model.Audio;
import com.ilmnuri.com.model.Global;
import com.ilmnuri.com.utility.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by User on 19.05.2016.
 */
public class AlbumAdpaterDemo extends RecyclerView.Adapter<AlbumAdpaterDemo.ViewHolder> {

    private Context mContext;
    private AlbumModel mAlbumModel;
    private OnItemClickListener mOnItemClickListener;
    private List<ViewHolder> mViewHolders = new ArrayList<>();
    Handler handler;
    File dir;

    public AlbumAdpaterDemo(Context context, AlbumModel albumModel, OnItemClickListener listener) {
        mContext = context;
        mAlbumModel = albumModel;
        this.mOnItemClickListener = listener;
        dir = new File(context.getExternalFilesDir(null), "audio");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public void deleteItem(int position) {
        mAlbumModel.getAudios().remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_album, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        mViewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Audio audio = getItem(position);
        boolean isDownloading = Global.getInstance().checkAudio(audio);
        if (isDownloading) {
            holder.activeSeekBar();
        } else {
            holder.unActiveSeekBar();
        }
        holder.tvAlbumTitle.setText(audio.getTrackName().replace(".mp3", "").replace("_", " "));
        holder.audioSize.setText(audio.getTrackSize());
        if (Utils.checkFileExist(dir.getPath() + "/" + mAlbumModel.getAudios().get(position).getTrackName())) {
            holder.btnDownload.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnDownload.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mAlbumModel.getAudios().size();
    }

    private Audio getItem(int position) {
        if (position >= 0 && position < mAlbumModel.getAudios().size()) {
            return mAlbumModel.getAudios().get(position);
        }
        return null;
    }

    public void onEvent(AudioEvent event) {

        switch (event.getType()) {
            case DOWNLOAD:
                for (ViewHolder vh : mViewHolders) {
                    if (vh instanceof ViewHolder) {
                        Audio audio = mAlbumModel.getAudios().get(vh.getAdapterPosition());
                        if (audio != null && audio.getTrackId() == (event.getAudio().getTrackId()) && audio.getTrackName().equals(audio.getTrackName())) {
                            vh.setSeekBarAsPlayed(true);
                        }
                    }
                }
                break;
            case STOP:
                for (ViewHolder vh : mViewHolders) {
                    if (vh instanceof ViewHolder) {
                        Audio audio = mAlbumModel.getAudios().get(vh.getAdapterPosition());
                        if (audio != null && audio.getTrackId() == (event.getAudio().getTrackId()) && audio.getTrackName().equals(audio.getTrackName())) {
                            vh.closeSeekBar(true);
                        }
                    }
                }
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @Bind(R.id.rl_item_album)
        LinearLayout mLinearLayout;

        @Nullable
        @Bind(R.id.tv_item_album)
        TextView tvAlbumTitle;

        @Nullable
        @Bind(R.id.btn_delete)
        ImageButton btnDelete;

        @Nullable
        @Bind(R.id.btn_download)
        ImageButton btnDownload;

        @Nullable
        @Bind(R.id.audioSize)
        TextView audioSize;

        @Nullable
        @Bind(R.id.progressBar)
        SeekBar mProgressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler = new Handler();
        }

        @OnClick(R.id.rl_item_album)
        void clickItem() {
            Intent intent = new Intent(mContext, PlayActivity.class);
            intent.putExtra("category", mAlbumModel.getCategory());
            intent.putExtra("url", mAlbumModel.getCategory() + "/" + mAlbumModel.getAlbum() + "/" + mAlbumModel.getAudios().get(getAdapterPosition()).getTrackName());
            mProgressBar.setVisibility(View.INVISIBLE);
            mContext.startActivity(intent);
        }

        @OnClick({R.id.btn_download, R.id.btn_delete})
        void options(View view) {
            switch (view.getId()) {
                case R.id.btn_delete:
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onDeleteListener(mAlbumModel, getAdapterPosition());
                    }
                    break;
                case R.id.btn_download:
                    mOnItemClickListener.onDownloadListener(mAlbumModel, getAdapterPosition());

                    break;
            }
        }

        public void setSeekBarAsPlayed(boolean isSeekBar) {
            if (isSeekBar) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressBar != null) {
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                    }
                });
                updateProgress();
            }
        }

        public void closeSeekBar(boolean isSeekBar) {
            if (isSeekBar) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressBar != null) {
                            mProgressBar.setVisibility(View.GONE);
                            btnDownload.setVisibility(View.GONE);
                            btnDelete.setVisibility(View.VISIBLE);
                            notifyItemChanged(getAdapterPosition());
                            Global.getInstance().setAudio(null);
                            handler.removeCallbacks(mUpdateTimeTask);
                        }
                    }
                });

            }
        }

        private Runnable mUpdateTimeTask = new Runnable() {
            public void run() {
                final int current_position = Global.getInstance().getCurrent_position();

                if (mProgressBar != null) {
                    mProgressBar.setProgress(current_position);
                }


                handler.postDelayed(this, 100);
            }
        };

        private void activeSeekBar() {

            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                }
            });

            removeCallback();
            final int position = Global.getInstance().getCurrent_position();

            if (mProgressBar != null) {
                mProgressBar.setProgress(position);
            }


            updateProgress();
        }

        private void unActiveSeekBar() {

            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                }
            });

            removeCallback();
        }

        public void removeCallback() {
            handler.removeCallbacks(mUpdateTimeTask);
        }

        public void updateProgress() {
            handler.postDelayed(mUpdateTimeTask, 100);
        }
    }

    public interface OnItemClickListener {
        void onDeleteListener(AlbumModel model, int position);

        void onDownloadListener(AlbumModel model, int position);
    }
}
