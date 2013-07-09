package us.ba3.altusdemo.demos;
import android.util.Log;
import us.ba3.me.*;
import android.content.Context;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DemoWithTimer extends Demo implements Runnable {
	protected ScheduledThreadPoolExecutor _threadPool;
	public float interval = 1.0f;
	
	@Override
	public void start(MapView mapView, Context context) {
		super.start(mapView, context);
		this.startTimer();
	}
	
	@Override
	public void stop(){
		super.stop();
		this.stopTimer();
	}
	
	public void startTimer(){
		_threadPool = new ScheduledThreadPoolExecutor(1);
		int millis = (int)(this.interval * 1000);
		_threadPool.scheduleAtFixedRate(this, 0, millis, TimeUnit.MILLISECONDS);
	}

	public void stopTimer(){
		_threadPool.shutdown();
	}

	public void run() {
		timerTick();
	}
	
	public void timerTick(){
		Log.w(this._name, "timerTick(): Please override.");
	}
}
