package util;

public class TwoComplement {

	public static double to2complement(byte b) {
		if(b < 0)
			//return (double) (0x00FF & b);
			return (double) ((short) b + (short) 256);
		return (double) b;
	}
	
	public static byte from2complement(double d) {
		short s = (short) d;
		if(s < 128)
			//return (byte) (s - 127);
			//return (byte) (~(0x00FF & s) + 1);
			return (byte) s;
		return (byte) (s - 256);
		
		
	}
}
