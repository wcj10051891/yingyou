package daogen;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class TableNameMap
{
    private String mapFilePath;

    private static Properties map = new Properties();

    public TableNameMap(String mapFilePath)
    {
    	this.mapFilePath = mapFilePath;
    }
    
    public Properties load(boolean refresh)
    {
        if (refresh)
        {
            map.clear();
            
            BufferedInputStream bin = null;
            try
            {
                bin = new BufferedInputStream(new FileInputStream(mapFilePath));
                map.load(bin);
            }
            catch (Exception e)
            {
                throw new RuntimeException("map file load error.", e);
            }
            finally
            {
                if (bin != null)
                    try
                    {
                        bin.close();
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException("file stream close error.", e);
                    }
            }
        }
        return map;
    }

    public void store()
    {
        BufferedWriter bw = null;
        try
        {
            bw = new BufferedWriter(new FileWriter(new File(mapFilePath)));
            synchronized (map)
            {
                for (Enumeration<?> e = map.keys(); e.hasMoreElements();)
                {
                    String key = (String) e.nextElement();
                    String val = (String) map.get(key);
                    key = saveConvert(key, true, false);
                    /*
                     * No need to escape embedded and trailing spaces for value, hence pass false to flag.
                     */
                    val = saveConvert(val, false, false);
                    bw.write(key + "=" + val);
                    bw.newLine();
                }
            }
            bw.flush();
        }
        catch (Exception e)
        {
            throw new RuntimeException("map file write error.", e);
        }
        finally
        {
            if (bw != null)
                try
                {
                    bw.close();
                }
                catch (IOException e)
                {
                    throw new RuntimeException("file stream close error.", e);
                }
        }

    }

    private static String saveConvert(String theString, boolean escapeSpace, boolean escapeUnicode)
    {
        int len = theString.length();
        int bufLen = len * 2;
        if (bufLen < 0)
        {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuffer outBuffer = new StringBuffer(bufLen);

        for (int x = 0; x < len; x++)
        {
            char aChar = theString.charAt(x);
            // Handle common case first, selecting largest block that
            // avoids the specials below
            if ((aChar > 61) && (aChar < 127))
            {
                if (aChar == '\\')
                {
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch (aChar) {
            case ' ':
                if (x == 0 || escapeSpace)
                    outBuffer.append('\\');
                outBuffer.append(' ');
                break;
            case '\t':
                outBuffer.append('\\');
                outBuffer.append('t');
                break;
            case '\n':
                outBuffer.append('\\');
                outBuffer.append('n');
                break;
            case '\r':
                outBuffer.append('\\');
                outBuffer.append('r');
                break;
            case '\f':
                outBuffer.append('\\');
                outBuffer.append('f');
                break;
            case '=': // Fall through
            case ':': // Fall through
            case '#': // Fall through
            case '!':
                outBuffer.append('\\');
                outBuffer.append(aChar);
                break;
            default:
                if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode)
                {
                    outBuffer.append('\\');
                    outBuffer.append('u');
                    outBuffer.append(toHex((aChar >> 12) & 0xF));
                    outBuffer.append(toHex((aChar >> 8) & 0xF));
                    outBuffer.append(toHex((aChar >> 4) & 0xF));
                    outBuffer.append(toHex(aChar & 0xF));
                }
                else
                {
                    outBuffer.append(aChar);
                }
            }
        }
        return outBuffer.toString();
    }

    private static char toHex(int nibble)
    {
        return hexDigit[(nibble & 0xF)];
    }

    /** A table of hex digits */
    private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
}
