package com.example.smproj;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

//import hageldave.ezfftw.dp.FFT;


public class MusicPlayerActivity extends AppCompatActivity {

    TextView titleTv,currentTimeTv,totalTimeTv;
    SeekBar seekBar;
    ImageView pausePlay,nextBtn,previousBtn,musicIcon;
    ArrayList<AudioModel> songsList;
    AudioModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        titleTv = findViewById(R.id.song_title);
        currentTimeTv = findViewById(R.id.current_time);
        totalTimeTv = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        pausePlay = findViewById(R.id.pause_play);
        nextBtn = findViewById(R.id.next);
        previousBtn = findViewById(R.id.previous);
        musicIcon = findViewById(R.id.music_icon_big);

        titleTv.setSelected(true);

        songsList = SongsList.getInstance();
        //currentSong = (int) getIntent().getSerializableExtra("POSITION");

        setResourcesWithMusic();

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeTv.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));

                    if(mediaPlayer.isPlaying()){
                        pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                        musicIcon.setRotation(x++);
                    }else{
                        pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        musicIcon.setRotation(0);
                    }

                }
                new Handler().postDelayed(this,100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    void setResourcesWithMusic(){
        if (currentSong != songsList.get(MyMediaPlayer.currentIndex)) {
            currentSong = songsList.get(MyMediaPlayer.currentIndex);

            titleTv.setText(currentSong.getTitle());

            totalTimeTv.setText(convertToMMSS(currentSong.getDuration()));

            pausePlay.setOnClickListener(v -> pausePlay());
            nextBtn.setOnClickListener(v -> playNextSong());
            previousBtn.setOnClickListener(v -> playPreviousSong());

            playMusic();
        }


    }


    private void playMusic(){

        mediaPlayer.reset();
        String path = currentSong.path;
        /*byte[] bytes1 = null;
        byte[] bytes2 = null;
        byte[] header = null;
        byte[] header2 = null;
        int bytesSamp1 = 0;
        int bytesSamp2 = 0;
        int channels = 1;
        double[] input1 = null;
        double[] input2 = null;
        Resources res = getResources();
        MediaExtractor mex = new MediaExtractor();
        try {
            mex.setDataSource(path);// the adresss location of the sound on sdcard.
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //MediaFormat mf = mex.getTrackFormat(0);

        //int bitRate = mf.getInteger(MediaFormat.KEY_BIT_RATE);
        //int sampleRate = mf.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        int byteDepth = 8;//bitRate/sampleRate/8;

        try {
            InputStream inputStream = new FileInputStream(path);
            BufferedInputStream buf = new BufferedInputStream(inputStream);
            header = new byte[44];
            buf.read(header, 0, 44);
            bytesSamp1 = (int) getLE2(header, 34, 2, true)/8;
            bytes1 = new byte[inputStream.available()];
            channels = (int) getLE2(header, 22, 2, true);
            buf.read(bytes1, 0, bytes1.length);
            buf.close();
            inputStream.close();
            //inputStream = new FileInputStream(new File(URI.create(MyMediaPlayer.impulseResp.path)));
            inputStream = res.openRawResource(R.raw.s3_r3_o_44100);
            buf = new BufferedInputStream(inputStream);
            header2 = new byte[44];
            buf.read(header2, 0, 44);
            bytesSamp2 = (int) getLE2(header2, 34, 2, true)/8;
            bytes2 = new byte[inputStream.available()];
            //buf.skip(44);
            buf.read(bytes2, 0, bytes2.length);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        input1 = new double[bytes1.length/bytesSamp1/channels];
        for (int i=0; i<input1.length; i++)
        {
            input1[i] = getLE2(bytes1, i*bytesSamp1*channels, bytesSamp1, true)/channels;
            for (int j=1; j<channels; j++)
            {
                input1[i] += getLE2(bytes1, i*bytesSamp1*channels, bytesSamp1, true)/channels;
            }
        }

        input2 = new double[bytes2.length/bytesSamp2];
        for (int i=0; i<input2.length; i++)
        {
            input2[i] = getLE2(bytes2, i*bytesSamp2, bytesSamp2, true);
        }
        double[] result = conv1D(input1, input2);
        input1 = null;
        input2 = null;


        //ByteBuffer bb = ByteBuffer.allocate(result.length * 8);
        //for(double d : result) {
        //    bb.putDouble(d);
        //}
        int bytesSampMax = (bytesSamp1 >= bytesSamp2 ? bytesSamp1 : bytesSamp2);
        putint(header, bytesSampMax, 34, 2, true);

        byte[] resultBytes = new byte[result.length * bytesSampMax];
        putlong(header, 36 + resultBytes.length, 4, 4, true);
        putlong(header, resultBytes.length, 40, 4, true);
        putint(header, 1, 22, 2, true);
        long sampleRate = getLE2(header, 24, 4, true);
        putlong(header, sampleRate*bytesSampMax,28, 4, true);
        putint(header, bytesSampMax, 32, 2, true);
        putint(header, bytesSampMax*8, 34, 2, true);
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        Log.d("Oldbyte: ", "" + Arrays.toString(bytes1));
        for (int i=0; i<result.length; i++)
        {
            putLE2(resultBytes, result[i], i*bytesSampMax, bytesSampMax, true);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            outputStream.write(header);
            outputStream.write(resultBytes);
        }
        catch(Exception e){
                System.out.println(e.getMessage());
        }

        Log.d("byte: ", "" + Arrays.toString(resultBytes));
        Log.d("resultLong: ", "" + Arrays.toString(result));*/
        try {
            mediaPlayer.setDataSource(currentSong.getPath()/*new StreamMediaDataSource(outputStream.toByteArray())*/);
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


    }

    private void playNextSong(){

        if(MyMediaPlayer.currentIndex== songsList.size()-1)
            return;
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }

    private void playPreviousSong(){
        if(MyMediaPlayer.currentIndex== 0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void pausePlay(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }


    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    public class StreamMediaDataSource extends MediaDataSource
    {
        private byte[] _data;

        public StreamMediaDataSource(byte[] data)
        {
            _data = data;
        }

        @Override
        public long getSize(){
            return _data.length;
        }
        @Override
        public void close()
        {
            _data = null;
        }
        @Override
        public int readAt(long position, byte[] buffer, int offset, int size)
        {

            if (position >= _data.length)
            {
                return -1;
            }

            if (position + size > _data.length)
            {
                size -= ((int)(position) + size) - _data.length;
            }
            System.arraycopy(_data, (int)position, buffer, offset, size);
            return size;
        }
    }

//    public double[] conv1D(double[] a, double[] b) {
//        if (a.length<b.length) {double[] temp = a;a=b;b=temp;}
//
//        int N = a.length + b.length-1;//512;
//        int resultSize = a.length + b.length-1;
//        /*while (b.length > N){
//            N*=2;
//        }
//        N*=2;*/
//        int nOverlapSize = N - b.length;
//
//        double[] kernel = new double[N];
//        double[] samples;
//        double[] result = new double[resultSize];
//        for (int i=0; i<b.length; i++)
//        {
//            kernel[i] = b[i];
//        }
//        for (int i=b.length; i<N; i++)
//        {
//            kernel[i] = 0;
//        }
//        double[] realb = new double[N];
//        double[] imb = new double[N];
//        double realbMax = 0;
//        double imbMax = 0;
//        FFT.fft(kernel, realb, imb, kernel.length);
//        for (int i=0; i<N;i++){
//            if (realb[i] > realbMax) realbMax = realb[i];
//            if (imb[i] > imbMax) imbMax = imb[i];
//        }
//        for (int i=0; i<N;i++){
//            //realb[i] /=realbMax;
//            //imb[i] /=imbMax;
//        }
//
//        double[] real = new double[N];
//        double[] im = new double[N];
//        samples = Arrays.copyOf(a, N);
//        Arrays.fill(samples, a.length, N, 0);
//        //for (int i=0; i<N; i++) samples[i]/=N;
//        FFT.fft(samples, real, im, N);
//
//        for (int j=0; j<N; j++){
//            real[j]=real[j] * realb[j];
//            im[j]=im[j] * imb[j];
//        }
//        FFT.ifft(real, im, result, N);
//
//        /*for (int i=0; i<b.length; i++){
//            result[i] = 0;
//        }*/
//        int i;
//
//        /*for (i=0; i<resultSize - N; i+=nOverlapSize)
//        {
//            samples = Arrays.copyOf(Arrays.copyOfRange(a,i, nOverlapSize), N);
//            Arrays.fill(samples, nOverlapSize, N, 0);
//            FFT.fft(samples, real, im, N);
//            for (int j=0; j<N; j++){
//                real[j]*=realb[j];
//                im[j]*=imb[j];
//            }
//            FFT.ifft(real, im, samples, N);
//            for (int j=i; j<i+b.length; j++){
//                result[j]+=samples[j];
//            }
//            for (int j=i+b.length; j<i+N; j++){
//                result[j]=samples[j];
//            }
//
//        }
//        samples = Arrays.copyOf(Arrays.copyOfRange(a,i, nOverlapSize), N);
//        Arrays.fill(samples, nOverlapSize, N, 0);
//        FFT.fft(samples, real, im, N);
//        for (int j=0; j<N; j++){
//            //real[j]*=realb[j];
//            //im[j]*=imb[j];
//        }
//        FFT.ifft(real, im, samples, N);
//        for (int j=i; j<(i+b.length < resultSize-i ? i+b.length : resultSize-i); j++){
//            result[j]+=samples[j];
//        }
//        for (int j=i+b.length; j<resultSize-i; j++){
//            result[j]=samples[j];
//        }*/
//
//        for (i=0; i<result.length; i++)
//        {
//            result[i] /= result.length;
//            //result[i]*=resmax;
//        }
//
//        return result;
//
//        /*Signal transform1 = FastFouriers.BEST.transform(new Signal(a));
//        Signal transform2 = FastFouriers.BEST.transform(new Signal(b));
//
//        for (int i=0; i<transform2.getLength(); i++)
//        {
//            transform1.setReAt(i, transform1.getReAt(i) * transform2.getReAt(i));
//            transform1.setImAt(i, transform1.getImAt(i) * transform2.getImAt(i));
//        }
//
//        return FastFouriers.BEST.inverse(transform1).getRe();*/
//        /*double[] kernel = new double[b.length];
//
//        long bmax =0;
//
//        for (long el: b) {bytes2
//            if (el > bmax) {
//                bmax = el;
//            }
//        }
//        for (int i=0; i<b.length; i++)
//        {
//            kernel[i] = (double)b[i]/(double)bmax;
//        }
//
//        int i = 0;
//        int bMaxIndex = b.length - 1;
//
//        long[] retArr = new long[a.length+bMaxIndex];
//        for(i=0;i<b.length;i++) {
//            retArr[i] = 0;
//
//            for(int j=0;j<=i; j++) {
//                retArr[i] += a[i - j] * kernel[bMaxIndex - j];
//            }
//            //retArr[i] /= i+1;
//
//        }
//
//        for(;i<retArr.length - b.length;i++) {
//            retArr[i] = 0;
//
//            for(int j=0;j<b.length;j++) {
//                retArr[i] += a[i - j] * kernel[bMaxIndex - j];
//            }
//            //retArr[i] /= b.length;
//
//        }
//
//        int last = 0;
//        for(;i<retArr.length;i++) {
//            retArr[i] = 0;
//
//            for(int j=last;j<b.length;j++) {
//                retArr[i] += a[i - j] * kernel[bMaxIndex - j];
//            }
//            //retArr[i] /= b.length - last;
//            last++;
//
//        }
//        return retArr;*/
//    }

    public long getLE2(byte[] buffer, int pos, int bytes, boolean little) {
        long val = 0;
        if (little) {
            for (int i = bytes - 1; i >= 0; i--) {
                val <<= 8;
                val += buffer[pos + i] & 0xFF;
            }
        }
        else {
            for (int i = 0; i < bytes; i++) {
                val <<= 8;
                val += buffer[pos + i] & 0xFF;
            }
        }
        /*long val = buffer[pos + 1] & 0xFF;
        val = (val << 8) + (buffer[pos] & 0xFF);*/
        return val;
    }

    public void putLE2(byte[] buffer, double val, int pos, int bytes, boolean little) {
        long lval = (long) val;

        if (little) {
            for (int i = 0; i < bytes; i++) {
                buffer[pos + i] = (byte) (lval & 0xFF);
                lval >>= 8;
            }
        }
        else {
            for (int i = bytes - 1; i >= 0; i--) {
                buffer[pos + i] = (byte) (lval & 0xFF);
                lval >>= 8;
            }
        }

        /*long lval = (long)val;

        buffer[pos] = (byte) (lval & 0xFF);
        lval>>=8;
        buffer[pos+1] = (byte) (lval & 0xFF);*/
    }

    public void putint(byte[] buffer, int val, int pos, int bytes, boolean little) {
        int lval = val;

        if (little) {
            for (int i = 0; i < bytes; i++) {
                buffer[pos + i] = (byte) (lval & 0xFF);
                lval >>= 8;
            }
        }
        else {
            for (int i = bytes-1; i >= 0; i--) {
                buffer[pos + i] = (byte) (lval & 0xFF);
                lval >>= 8;
            }
        }
    }

    public void putlong(byte[] buffer, long val, int pos, int bytes, boolean little) {
        long lval = val;

        if (little){
            for (int i=0; i<bytes; i++)
            {
                buffer[pos+i] = (byte) (lval & 0xFF);
                lval>>=8;
            }
        }
        else{
            for (int i=bytes - 1; i>=0; i--)
            {
                buffer[pos+i] = (byte) (lval & 0xFF);
                lval>>=8;
            }
        }
    }
}