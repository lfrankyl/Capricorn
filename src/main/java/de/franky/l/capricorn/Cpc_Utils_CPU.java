package de.franky.l.capricorn;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.util.Log;

import static de.franky.l.capricorn.Cpc_Utils.CpcPref;


final class Cpc_Utils_CPU {

    private static  int CPU_WINDOW = 2500;

    private static final int CPU_REFRESH_RATE = 100; // Warning: anything but 0

    private static HandlerThread mHandlerThread;

    public static float fCPU = 0;

    private static boolean monitorCpu;

    /** Start CPU monitoring */
    public static boolean startCpuMonitoring() {
    	monitorCpu = true;
		Log.d("Cpc_Utils_CPU","start CpuMonitoring");
        mHandlerThread = new HandlerThread("CPU monitoring"); //$NON-NLS-1$
        mHandlerThread.start();

        Handler handler = new Handler(mHandlerThread.getLooper());
        handler.post(new Runnable() {

            @Override
            public void run() 
            {
            	Context myContext;
            	myContext = Cpc_Application.getContext();
            	if (myContext != null)
            	{
    	    		String sIntervall = CpcPref.getString("pref_Key_Intervall", "2500") ;
    	    		CPU_WINDOW = Cpc_Utils.SaveParseInt(sIntervall,2500);
            		
	            	while (monitorCpu)
	            	{
	
	          		    // Log.d("startCpuMonitoring","CPU_WINDOW geladen");
	      				
	          		  	Cpc_Utils_Linux linuxUtils = new Cpc_Utils_Linux();
	
	                    int pid = android.os.Process.myPid();
	                    String cpuStat1 = linuxUtils.readSystemStat();
	                    String pidStat1 = linuxUtils.readProcessStat(pid);
	
	                    try
						{
	                        Thread.sleep(CPU_WINDOW);
	                    }
						catch (Exception e)
						{
							Log.e("startCpuMonitoring","Execption in Thread.sleep");
	                    }
	
	                    String cpuStat2 = linuxUtils.readSystemStat();
	                    String pidStat2 = linuxUtils.readProcessStat(pid);
	
	                    float cpu = linuxUtils.getSystemCpuUsage(cpuStat1, cpuStat2);
	                    if (cpu >= 0.0f) {
	                    	fCPU = cpu;
	                    	 //Log.d("Thread CPU", Float.toString(fCPU));
	                    }
						long cpu1 = -1;
						long cpu2 = -2;
	                    String[] toks;
						if (cpuStat1!=null ) {
							toks = cpuStat1.split(" ");
							cpu1 = linuxUtils.getSystemUptime(toks);
						}

						if (cpuStat2!=null ) {
							toks = cpuStat2.split(" ");
							cpu2 = linuxUtils.getSystemUptime(toks);
						}
	
	                    cpu = linuxUtils.getProcessCpuUsage(pidStat1, pidStat2,
	                            cpu2 - cpu1);
	                    //if (cpu >= 0.0f)
	                    //{
	                    	//fCPU = cpu;
	                    	//Log.d("Thread CPU", Float.toString(fCPU));
	                    //}
	
	                    try {
	                        synchronized (this) {
	                            wait(CPU_REFRESH_RATE);
	                        }
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                        return;
	                    }
                   }
            	
               }
                // Log.i("THREAD CPU", "Finishing");
            }

        });

        return Cpc_Utils_CPU.monitorCpu;
    }

    /** Stop CPU monitoring */
    public static void stopCpuMonitoring() {
        if (mHandlerThread != null)
		{
            monitorCpu = false;
            mHandlerThread.quit();
            mHandlerThread = null;
			Log.d("Cpc_Utils_CPU","stopCpuMonitoring");
        }
    }

	public static boolean CPU_Monitoring_Runs()
	{
		return monitorCpu;
	}

    /** Dispose of the object and release the resources allocated for it */
    public void dispose() {

        monitorCpu = false;

    }


}