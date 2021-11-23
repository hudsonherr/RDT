import java.util.*;
import java.math.*;

public class TCPL4
{
    //The program expects 3 arguments when it is run:
    //(1) the percent of packets that have a bit error
    //(2) the max delay if a packet is delayed, and
    //(3) the percent of time a packet is delayed.

   /*
   REED
   Four functions of reliable data transfer:
      receive data
      encapsulate data prior to sending
      un-encapsulate data upon receipt
      send data
   (must be this way in both directions; Host A to Host B and Host B to Host A)
   */

   /*
   REED
   Features of Reliable Data Transfer Protocol:
      Acknowledgement (ACK) - adding an extra bit in the data segment being transmitted accomplishes this
      Timeouts - timer variable
      Congestion Control
      End-to-End
      Network Assissted
      Sequence Numbers (SEQ)
      Pipelining
    */

    //REED...
    private int ACKnum; //ACK can be 0 (not received) or 1 (recieved)
    private int timer;  //timer keeps track of how long a bit takes to send, if it reaches maxDelay, it sends bit again
    private int SEQnum; //SEQnum is used to keep track of the identity of the bits sent and let the sender know which bits were received successfully


    public TCPL4(int ack, int time, int seq)
    {
        //REED ...
        ACKnum = ack;
        timer = time;
        SEQnum = seq;
    }

    //send is effectively implemented as UDT right now. It just passes the message down to layer 3 with nothing extra.
    //Reed - in order to make send RDT, we need to implement an ACK, a Timer variable, and a sequence number (SEQ)


    //message is the message that sender is going to send
    public int[] send(int message)
    {
        //you can have an array with more than 1 int for things like times / seq nums / etc.
        int [] dataArray = new int[5];

        dataArray[0] = message;

        //if message goes through, set ACKnum to 1 or to 0 if not (NAK)
        dataArray[1] = ACKnum; //R - ACK is second index in array

        //if timer reaches maxDelay, send packet again
        dataArray[2] = timer; //R - timer is third index in array

        //used to identify which packet is which
        //checksum goes somewhere in here
        dataArray[3] = SEQnum; //R - SEQ is fourth index in array

        //dataArray[4] = TCPL3 sender;


        //Checksum
        //convert to binary
        String result = Integer.toBinaryString(message);
        //System.out.println("Message converted to binary: " + result);

        //convert with One's complement
        BigInteger twoToLength = new BigInteger("2").pow(result.length());
        String b = twoToLength.add(new BigInteger(result, 2).not()).toString(2);
        //System.out.println("One's complement: " + b);

        //send message and "b"(one's complement) to layer 3
        

        //end Checksum


        return dataArray;
    }

    //receive is effectively implemented as UDT right now. It just passes the messages it gets up to layer 5 with nothing extra.

    ///receive returns an arraylist of messages that it will send at the conclusion of the send call. The TCPL5 object is there only so you may send the messages up to layer 5. The boolean isSender indicates if the "this" object is the original sender or not.
    public ArrayList<int []> receive(ArrayList<int [] >dataArray, TCPL5 reciever, boolean isSender) //isSender is for the purpose of letting you know if this is the sender of reciever
    {
        ArrayList<int []> responses = new ArrayList<int []>();

        for(int i=0;i<dataArray.size();i++)
        {
            int [] DA = dataArray.get(i);
            reciever.giveMessage(DA[0]); //send the data up to layer 5. Since the udt messages are 1 length, the data is at 0
        }

        return null; //you can return an arraylist of messages you want to send back. You know, acks, nacks, and whatnot
    }

}
