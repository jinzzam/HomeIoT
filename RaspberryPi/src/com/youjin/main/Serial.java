package com.youjin.main;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Templates;

public class Serial extends Thread
{
	public static Data data = Data.getInstance();
	public static OutputStream out;
    public Serial()
    {
        super();
    }
    
    void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                
                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    
    /** */
    public static class SerialReader implements Runnable 
    {
        InputStream in;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            String temp = "";
            int len = -1;
            String []arr = null;
            int startNum;
            try
            {
                while ( ( len = this.in.read(buffer)) != -1 )
                {
                	temp += new String(buffer,0,len);
                	if(temp.contains("!")) {
                		temp = temp.substring(0,temp.length()-1);
                		arr = temp.split("\r\n");
                		System.out.println(arr.length);
                		if(arr.length == 0) continue;
                		if(arr[0].equals("")) {
                			startNum = 1;
                		}else {
                			startNum = 0;
                		}
                		//System.out.println("**********");
                		if(arr.length != 5) {
                			temp="";
                			continue;
                		}
                		try {
                    		data.setTemperature(Integer.parseInt(arr[startNum]));
                    		data.setWaterLine(Integer.parseInt(arr[startNum+1]));
                    		data.setThief(Integer.parseInt(arr[startNum+2])==1 ? true : false);
                    		data.setBtn(Integer.parseInt(arr[startNum+3])==1 ? true : false);
                    		data.setEntry(Integer.parseInt(arr[startNum+4])==1 ? true : false);	
                		} catch (NumberFormatException e) {
                			e.printStackTrace();
                		}
                		System.out.println(data.toString());
                		temp="";
                	}
                }
                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }

    /** */
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }
    
    @Override
    public void run ()
    {
        try
        {
            (new Serial()).connect("COM8");
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}