package rune.logging;

import android.util.Log;

import rune.formatting.StringExpert;

/**
 * A logger for outputting log messages with class name
 */
public class Logger {

    // Fields

    private Class mCaller;

    // Constructors

    public Logger(Class caller) {
        mCaller = caller;
    }

    // Methods

    public void v(String str, Object... args) {
        doLog(LogLevel.Verbose, str, args);
    }

    public void d(String str, Object... args) {
        doLog(LogLevel.Debug, str, args);
    }

    public void i(String str, Object... args) {
        doLog(LogLevel.Info, str, args);
    }

    public void e(String str, Object... args) {
        doLog(LogLevel.Error, str, args);
    }

    public void f(String str, Object... args) {
        doLog(LogLevel.Fatal, str, args);
    }

    private void doLog(LogLevel logLevel, String str, Object[] args) {
        if (args.length > 0)
            str = StringExpert.format(str, args);

        String prefix = StringExpert.format(
            "*** [{0}, {1}]:",
            logLevel.toString().toUpperCase(),
            mCaller.getSimpleName()
            );

        if (logLevel == LogLevel.Verbose)
            Log.v(prefix, str);
        else
        if (logLevel == LogLevel.Debug)
            Log.d(prefix, str);
        else
        if (logLevel == LogLevel.Info)
            Log.i(prefix, str);
        else
        if (logLevel == LogLevel.Error)
            Log.e(prefix, str);
        else
        if (logLevel == LogLevel.Fatal)
            Log.wtf(prefix, str);
        else
            throw new RuntimeException("bad arg");
    }
}
