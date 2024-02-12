package aman.three;

import android.content.Context;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


// Set the custom uncaught exception handler in the main file
// Set the custom uncaught exception handler in the main file
// Set the custom uncaught exception handler in the main file
// Set the custom uncaught exception handler in the main file


//  Thread.setDefaultUncaughtExceptionHandler(new CaptureCrash(getApplicationContext()));



public class CaptureCrash implements Thread.UncaughtExceptionHandler {

    private static final String LOG_FILE_NAME = "crash_log.log";
    private final Context context;

    public CaptureCrash(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        // Save crash log to a file
        saveCrashLog(throwable);

        // Terminate the app or perform any other necessary action
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void saveCrashLog(Throwable throwable) {
        try {
            File externalFilesDir = context.getExternalFilesDir(null);

            if (externalFilesDir != null) {
                File logFile = new File(externalFilesDir, LOG_FILE_NAME);
                FileWriter writer = new FileWriter(logFile, true);

                writer.append(getCurrentDateTime()).append(":\t");
                printFullStackTrace(throwable, new PrintWriter(writer));

                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printFullStackTrace(Throwable throwable, PrintWriter printWriter) {
        printWriter.println(throwable.toString());
        for (StackTraceElement element : throwable.getStackTrace()) {
            printWriter.print("\t " + element + "\n");
        }
        Throwable cause = throwable.getCause();
        if (cause != null) {
            printWriter.print("Caused by:\t");
            printFullStackTrace(cause, printWriter);
        }
        printWriter.print("\n");
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
}
